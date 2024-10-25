package in.clear.http.model.customField;

import lombok.Data;

@Data
public class VendorCustomFields {

    private String name;
    private String vendorCode;
    private String paymentTerms;
    private String reconAccount;
    private Address address;
    private BankDetail bankDetail;

}
