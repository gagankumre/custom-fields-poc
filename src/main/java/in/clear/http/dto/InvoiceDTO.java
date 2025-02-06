package in.clear.http.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;;
import java.util.List;

@Data
@SuperBuilder
public class InvoiceDTO {

    private Long id;

    private String workspaceId;

    private String amount;

    private String taxAmount;

    private String documentNumber;

    private List<LineItem> lineItems;
}

