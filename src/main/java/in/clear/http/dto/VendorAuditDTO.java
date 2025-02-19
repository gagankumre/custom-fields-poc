package in.clear.http.dto;

import in.clear.http.model.VendorContact;
import in.clear.http.model.customField.VendorCustomFields;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorAuditDTO {
    private String erpId;
    private VendorCustomFields customField;
    private List<VendorContact> contacts;
}
