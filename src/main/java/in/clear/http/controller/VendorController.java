package in.clear.http.controller;

import in.clear.http.model.Vendor;
import in.clear.http.model.customField.VendorCustomFields;
import in.clear.http.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vendors")
@RequiredArgsConstructor
public class VendorController {

    private final VendorService vendorService;

    @PostMapping
    public ResponseEntity<Vendor> createVendor(@RequestParam String workspaceId,
                                               @RequestParam String erpId,
                                               @RequestBody VendorCustomFields customField) {
        Vendor vendor = vendorService.createVendor(workspaceId, erpId, customField);
        return ResponseEntity.ok(vendor);
    }

    @GetMapping("{id}")
    public ResponseEntity<Vendor> getVendor(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(vendorService.getVendor(id));
    }

    @PutMapping("{id}/update")
    public ResponseEntity<Vendor> createVendor(
            @PathVariable(name = "id") Long id,
            @RequestBody Vendor vendor) {
        return ResponseEntity.ok(vendorService.updateVendor(id, vendor));
    }

    @GetMapping("/list")
    public List<Vendor> listVendors(
            @RequestParam(required = false) String workspaceId,
            @RequestParam(required = false) String erpId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String accountNumber,
            @RequestParam(required = false) String city) {
        return vendorService.listVendors(workspaceId, erpId, name, email, accountNumber, city);
    }
}
