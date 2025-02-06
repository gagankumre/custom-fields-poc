package in.clear.http.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class LineItem {
    private String buyerItemCode;
    private String supplierItemCode;
    private String description;
    private String amount;
}
