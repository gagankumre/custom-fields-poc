package in.clear.http.model.customField;

import lombok.Data;

@Data
public class BankDetail {
    private String accountType;
    private String accountNumber;
    private String bankName;
    private String holderName;
}
