package in.clear.http.service;

import in.clear.http.model.Vendor;
import in.clear.http.model.customField.VendorCustomFields;
import in.clear.http.repository.VendorRepository;
import in.clear.http.repository.specification.VendorSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javers.core.Javers;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VendorService {

    private final Javers javers;

    private final VendorRepository vendorRepository;

    public Vendor createVendor(String workspaceId, String erpId, VendorCustomFields customField) {
        Vendor vendor = new Vendor();
        vendor.setWorkspaceId(workspaceId);
        vendor.setErpId(erpId);
        vendor.setCustomField(customField);
        return vendorRepository.save(vendor);
    }

    public Vendor getVendor(Long id) {
        return vendorRepository.findById(id).orElseThrow();
    }

    public Vendor updateVendor(Long id, Vendor newVendor) {
        Vendor vendor = vendorRepository.findById(id).orElseThrow();
        javers.commit("fetchVendor", vendor);
        vendor.setCustomField(newVendor.getCustomField());
        vendor.setErpId(newVendor.getErpId());
        vendor.setContacts(newVendor.getContacts());
        return vendorRepository.save(newVendor);
    }

    public void saveVendors(List<Vendor> vendors) {
        int count = 0;
        for (Vendor vendor : vendors) {
            try {
                count++;
                vendorRepository.save(vendor);
            } catch (Exception e) {
                log.debug("Error saving vendor: {}", vendor);
                log.info(" current count: {}", count);
            }
        }
    }

    public List<Vendor> listVendors(String workspaceId, String erpId, String name, String email, String accountNumber, String city) {
        Specification<Vendor> specification = Specification
                .where(VendorSpecification.hasWorkspaceId(workspaceId))
                .and(VendorSpecification.hasErpId(erpId))
                .and(VendorSpecification.hasCustomFieldName(name))
                .and(VendorSpecification.hasCustomFieldEmail(email))
                .and(VendorSpecification.hasCustomFieldAccountNumber(accountNumber))
                .and(VendorSpecification.hasCustomFieldCity(city));

        var vendors = vendorRepository.findAll(specification);
        log.info("found vendors: {}", vendors.size());
        return vendors;
    }
}
