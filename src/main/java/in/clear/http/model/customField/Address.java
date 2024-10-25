package in.clear.http.model.customField;

import lombok.Data;

@Data
public class Address {
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String zipCode;
    private String stateName;
    private String countryName;
    private String countryCode;
    private String phoneNumber1;
    private String phoneNumber2;
    private String mobileNumber;
    private String email;
}
