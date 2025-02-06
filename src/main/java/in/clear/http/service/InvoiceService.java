package in.clear.http.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.clear.http.dto.InvoiceDTO;
import in.clear.http.dto.LineItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.springframework.stereotype.Service;

import javax.json.Json;
import javax.json.JsonMergePatch;
import javax.json.JsonObject;
import javax.json.JsonPatch;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import java.util.List;

import static org.javers.common.string.ToStringBuilder.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceService {

    public static void main(String[] args) {
        Javers javers = JaversBuilder.javers().build();

//        SCAuditEvent<String> auditEvent = SCAuditEvent.<String>builder()
//                .identifier("1")
//                .currentData("objv1")
//                .previousData(null)
//                .build();
//
//        SCAuditEvent<String> auditEvent2 = SCAuditEvent.<String>builder()
//                .identifier("2")
//                .currentData("objv2")
//                .previousData("objv1")
//                .build();

        LineItem lineItem = LineItem.builder()
                .buyerItemCode("code1")
                .supplierItemCode("code2")
                .amount("100")
                .description("description")
                .build();



        InvoiceDTO invoiceDTO = InvoiceDTO.builder()
                .id(1L)
                .workspaceId("workspaceId")
                .amount("100")
                .documentNumber("documentNumber")
                .lineItems(List.of(lineItem))
                .build();

        LineItem lineItem2 = LineItem.builder()
                .buyerItemCode("code1")
                .supplierItemCode("code2")
                .amount("200")
                .description("description")
                .build();

        InvoiceDTO invoiceDTOUpdated = InvoiceDTO.builder()
                .id(1L)
                .workspaceId("workspaceId")
                .amount("200")
                .documentNumber("documentNumber")
                .lineItems(List.of(lineItem2))
                .build();


        Diff diff = javers.compare(invoiceDTO, invoiceDTOUpdated);

        diff.getChanges().forEach(change -> {
            System.out.println(change.getClass().getSimpleName() + ": " + change);
        });

//        ObjectMapper mapper = new ObjectMapper();
//        JsonStructure source = mapper.convertValue(invoiceDTO, JsonStructure.class);
//        JsonStructure target = mapper.convertValue(invoiceDTOUpdated, JsonStructure.class);
//
//        JsonPatch diff = Json.createDiff(source, target);
//
//        JsonMergePatch mergeDiff = Json.createMergeDiff(source, target);
//
//        JsonValue patched = mergeDiff.apply(source);
//
//        System.out.println(format(diff.toJsonArray()));
//        System.out.println(format(mergeDiff.toJsonValue()));
    }
}
