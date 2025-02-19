package in.clear.http.mapper;

import in.clear.http.dto.VendorAuditDTO;
import in.clear.http.model.Vendor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface VendorMapper {
    VendorMapper INSTANCE = Mappers.getMapper(VendorMapper.class);

    VendorAuditDTO toVendorAuditDTO(Vendor vendor);
}
