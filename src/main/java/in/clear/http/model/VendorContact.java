package in.clear.http.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorContact {
    private String name;
    private String email;
    private String phoneNumber;
    private String designation;
    private String department;
}
