package in.clear.http.model.audit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.regex.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldTemplate {
    private Pattern pathPattern;
    private Map<String, MessageTemplate> ops; // key: op, value: MessageTemplate

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangeType {
        private MessageTemplate replace;
        private MessageTemplate add;
        private MessageTemplate remove;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageTemplate {
        private String messageFormat;
        private Map<String, String> args; // key: argName, value: argValuePath

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageArgs {
        private String argName;
        private String argValuePath;
    }
}
