package com.champsoft.services.sales.MapperLayer;

import com.champsoft.services.sales.DataLayer.Purchase.PurchaseOrder;
import com.champsoft.services.sales.PresentationLayer.PurchaseRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PurchaseOrderRequestModelMapper {

    @Mapping(target = "id", ignore = true) // ID is auto-generated

    // Flower references
    @Mapping(target = "flowerIdentifier.flowerNumber", source = "flowerIdentificationNumber")
    @Mapping(target = "inventoryIdentifier.inventoryId", source = "inventoryId")

    // Supplier references
    @Mapping(target = "supplierIdentifier.supplierId", source = "supplierId")

    // employee references
    @Mapping(target = "employeeIdentifier.employeeId", source = "employeeId")

    // Pricing
    @Mapping(target = "price.amount", source = "salePrice")
    @Mapping(target = "price.currency", source = "currency")

    // Dates & Status
    @Mapping(target = "saleOfferDate", source = "saleOfferDate")
    @Mapping(source = "salePurchaseStatus", target = "salePurchaseStatus")
    PurchaseOrder requestModelToEntity(PurchaseRequestModel purchaseRequestModel);
}
