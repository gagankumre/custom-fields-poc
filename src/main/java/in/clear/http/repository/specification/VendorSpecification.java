package in.clear.http.repository.specification;

import in.clear.http.model.Vendor;
import org.springframework.data.jpa.domain.Specification;

public class VendorSpecification {

    public static Specification<Vendor> hasWorkspaceId(String workspaceId) {
        return (root, query, criteriaBuilder) ->
                workspaceId == null ? null : criteriaBuilder.equal(root.get("workspaceId"), workspaceId);
    }

    public static Specification<Vendor> hasErpId(String erpId) {
        return (root, query, criteriaBuilder) ->
                erpId == null ? null : criteriaBuilder.equal(root.get("erpId"), erpId);
    }

    public static Specification<Vendor> hasCustomFieldName(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? null : criteriaBuilder.equal(
                        criteriaBuilder.function("jsonb_extract_path_text", String.class, root.get("customField"), criteriaBuilder.literal("name")),
                        name
                );
    }

    public static Specification<Vendor> hasCustomFieldEmail(String email) {
        return (root, query, criteriaBuilder) ->
                email == null ? null : criteriaBuilder.equal(
                        criteriaBuilder.function("jsonb_extract_path_text", String.class, root.get("customField"), criteriaBuilder.literal("address"), criteriaBuilder.literal("email")),
                        email
                );
    }

    public static Specification<Vendor> hasCustomFieldAccountNumber(String accountNumber) {
        return (root, query, criteriaBuilder) ->
                accountNumber == null ? null : criteriaBuilder.equal(
                        criteriaBuilder.function("jsonb_extract_path_text", String.class, root.get("customField"), criteriaBuilder.literal("bankDetail"), criteriaBuilder.literal("accountNumber")),
                        accountNumber
                );
    }

    public static Specification<Vendor> hasCustomFieldCity(String city) {
        return (root, query, criteriaBuilder) ->
                city == null ? null : criteriaBuilder.equal(
                        criteriaBuilder.function("jsonb_extract_path_text", String.class, root.get("customField"), criteriaBuilder.literal("address"), criteriaBuilder.literal("city")),
                        city
                );
    }
}
