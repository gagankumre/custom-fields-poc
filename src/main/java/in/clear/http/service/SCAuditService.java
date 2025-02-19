package in.clear.http.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.flipkart.zjsonpatch.DiffFlags;
import com.flipkart.zjsonpatch.JsonDiff;
import in.clear.http.dto.audit.SCAuditEventRequest;
import in.clear.http.model.audit.AuditEventTemplates;
import in.clear.http.model.audit.AuditEventType;
import in.clear.http.model.audit.FieldTemplate;
import in.clear.http.model.audit.SCAuditEvent;
import in.clear.http.repository.AuditEventTemplatesRepository;
import in.clear.http.repository.AuditEventTypeRepository;
import in.clear.http.repository.SCAuditEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class SCAuditService {

    private final SCAuditEventRepository scAuditEventRepository;
    private final AuditEventTemplatesRepository auditEventTemplatesService;
    private final AuditEventTypeRepository auditEventTypeRepository;

    public static final EnumSet<DiffFlags> enumSet = EnumSet.of(DiffFlags.OMIT_VALUE_ON_REMOVE, DiffFlags.ADD_ORIGINAL_VALUE_ON_REPLACE);

    public AuditEventTemplates saveAuditEventTemplates(AuditEventTemplates auditEventTemplates) {
        if(!auditEventTypeRepository.existsById(auditEventTemplates.getEventTypeId())) {
            throw new RuntimeException("AuditEventType not found with id: " + auditEventTemplates.getEventTypeId());
        }

        var existingTemplate = auditEventTemplatesService.findByEventTypeId(auditEventTemplates.getEventTypeId());
        existingTemplate.ifPresent(eventTemplates -> auditEventTemplates.setId(eventTemplates.getId()));

        return auditEventTemplatesService.save(auditEventTemplates);
    }

    public AuditEventTemplates getAuditEventTemplatesById(Long id) {
        return auditEventTemplatesService.findById(id)
                .orElseThrow(() -> new RuntimeException("AuditEventTemplates not found with id: " + id));
    }

    public List<AuditEventTemplates> listAuditEventTemplates() {
        return auditEventTemplatesService.findAll();
    }

    public AuditEventType saveAuditEventType(AuditEventType auditEventType) {
       return auditEventTypeRepository.save(auditEventType);
    }

    public AuditEventType getAuditEventTypeById(Long id) {
        return auditEventTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AuditEventType not found with id: " + id));
    }

    public List<AuditEventType> listAuditEventType() {
        return auditEventTypeRepository.findAll();
    }

    public void logAuditEvent(SCAuditEventRequest scAuditEventRequest) {

        JsonNode diffs = JsonDiff.asJson(scAuditEventRequest.getPreviousData(),
                scAuditEventRequest.getCurrentData());
        addPreviousValues(scAuditEventRequest.getPreviousData(), diffs);

        scAuditEventRequest.setDiff(diffs);

        System.out.printf("Audit Log for Entity ID: %s%n", scAuditEventRequest.getEntityId());
        System.out.printf("Old State: %s%n", scAuditEventRequest.getPreviousData());
        System.out.printf("New State: %s%n", scAuditEventRequest.getCurrentData());
        System.out.printf("Diff: %s%n", diffs.toPrettyString());

        saveAuditEvent(scAuditEventRequest);
    }

    public SCAuditEvent saveAuditEvent(SCAuditEventRequest scAuditEventRequest) {

        SCAuditEvent scAuditEvent = SCAuditEvent.builder()
                .entityId(scAuditEventRequest.getEntityId())
                .eventTypeId(scAuditEventRequest.getEventTypeId())
                .previousData(scAuditEventRequest.getPreviousData())
                .currentData(scAuditEventRequest.getCurrentData())
                .diff(scAuditEventRequest.getDiff())
                .actor(scAuditEventRequest.getActor())
                .build();
        return saveAuditEvent(scAuditEvent);
    }

    public void addPreviousValues(JsonNode previousData, JsonNode diffs) {
        diffs.forEach(diff -> {
            String path = diff.get("path").asText();
            JsonNode previousValue = Objects.isNull(previousData) ? null : previousData.at(path);
            ((ObjectNode) diff).set("oldValue", previousValue);
            ((ObjectNode) diff).set("newValue", diff.get("value"));
        });

    }

    public SCAuditEvent saveAuditEvent(SCAuditEvent scAuditEvent) {
        return scAuditEventRepository.save(scAuditEvent);
    }

    public List<String> getTimelineView(String entityId) {
        List<SCAuditEvent> auditEvents = scAuditEventRepository.findByEntityId(entityId);
        List<String> timelineMessages = new ArrayList<>();

        auditEvents.forEach(auditEvent -> {
            Optional<AuditEventTemplates> auditEventTemplates = auditEventTemplatesService.findByEventTypeId(auditEvent.getEventTypeId());
            if (auditEventTemplates.isPresent()) {
                List<String> messages = getMessagesFromDiffs(auditEvent.getDiff(), auditEvent.getActor(), auditEventTemplates.get());
                timelineMessages.addAll(messages);
            }
        });
        return timelineMessages;
    }

    private List<String> getMessagesFromDiffs(JsonNode diffs, String actor, AuditEventTemplates auditEventTemplates) {
        List<String> messagesFromDiffs = new ArrayList<>();
        diffs.forEach(diff -> {
            String message = getTimelineMessageForField(auditEventTemplates, diff, actor);
            if (message != null) {
                messagesFromDiffs.add(message);
            }
        });
        return messagesFromDiffs;
    }

    private String getTimelineMessageForField(AuditEventTemplates auditEventTemplates, JsonNode diff, String actor) {

        String fieldPath = diff.get("path").asText();
        String oldValue = diff.get("oldValue").isValueNode() ? diff.get("oldValue").textValue() : diff.get("oldValue").toString();
        String newValue = diff.get("newValue").isValueNode() ? diff.get("newValue").textValue() : diff.get("newValue").toString();
        String operation = diff.get("op").asText();

        for (FieldTemplate fieldTemplate : auditEventTemplates.getFieldTemplates()) {
            Pattern fieldPathPattern = fieldTemplate.getPathPattern();
            if (fieldPathPattern.matcher(fieldPath).matches()) {
                FieldTemplate.MessageTemplate messageTemplate = fieldTemplate.getOps().get(operation);

                if (messageTemplate != null && messageTemplate.getMessageFormat() != null) {
                    String messageFormat = messageTemplate.getMessageFormat();
                    messageFormat = messageFormat
                            .replace("{actor}", Objects.toString(actor))
                            .replace("{oldValue}", Objects.toString(oldValue))
                            .replace("{newValue}", Objects.toString(newValue));

                    if (!CollectionUtils.isEmpty(messageTemplate.getArgs())) {
                        messageFormat = handleCustomArgs(messageFormat, diff, messageTemplate.getArgs());
                    }
                    return messageFormat;
                }
            }
        }
        return null;
    }

    private String handleCustomArgs(String messageFormat, JsonNode diff, Map<String, String> args) {
        for (Map.Entry<String, String> arg : args.entrySet()) {
            String argName = arg.getKey();
            String argValuePath = arg.getValue();
            String valueToReplace = null;

            JsonNode newValueNode = diff.get("newValue");
            JsonNode oldValueNode = diff.get("oldValue");

            if (argValuePath.startsWith("newValue:")) {
                String jsonPath = argValuePath.substring("newValue:".length());
                valueToReplace = newValueNode.at(jsonPath).textValue();
            } else if (argValuePath.startsWith("oldValue:")) {
                String jsonPath = argValuePath.substring("oldValue:".length());
                valueToReplace = oldValueNode.at(jsonPath).textValue();
            }

            if (valueToReplace != null) {
                messageFormat = messageFormat.replace("{" + argName + "}", valueToReplace);
            }

        }

        return messageFormat;
    }
}
