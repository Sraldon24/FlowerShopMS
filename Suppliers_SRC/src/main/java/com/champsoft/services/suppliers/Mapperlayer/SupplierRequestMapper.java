/* =============================
   SupplierRequestMapper.java
   ============================= */
package com.champsoft.services.suppliers.Mapperlayer;

import com.champsoft.services.suppliers.DataLayer.Supplier;
import com.champsoft.services.suppliers.PresentationLayer.SupplierRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SupplierRequestMapper {

    @Mapping(target = "supplierIdentifier", source = "supplierId")
    //@Mapping(target = "id", source = "supplierRequestModel.id")
   // @Mapping(target = "password", source = "supplierRequestModel.password1")

    @Mapping(target = "companyName", source = "companyName")
    @Mapping(target = "contactPerson", source = "contactPerson")
    @Mapping(target = "emailAddress", source = "emailAddress")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "address.streetAddress", source = "streetAddress")
    @Mapping(target = "address.province", source = "province")
    @Mapping(target = "address.postalCode", source = "postalCode")
    @Mapping(target = "address.city", source = "city")
    @Mapping(target = "phoneNumbers", source = "phoneNumbers")
    Supplier requestModelToEntity(SupplierRequestModel supplierRequestModel);

    List<Supplier> requestModelListToEntityList(List<SupplierRequestModel> supplierRequestModels);
}
