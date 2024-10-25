package in.clear.http.model;

import in.clear.http.model.customField.VendorCustomFields;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "vendor",
        indexes = {
                @Index(name = "IDX_workspace", columnList = "workspace_id"),
        }
)
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "workspace_id", nullable = false)
    private String workspaceId;

    @Column(name = "erp_id", nullable = false, unique = true)
    private String erpId;

    @Type(type = "io.hypersistence.utils.hibernate.type.json.JsonType")
    @Column(columnDefinition = "jsonb", name = "custom_field")
    private VendorCustomFields customField;
}

