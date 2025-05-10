package com.champsoft.services.sales.MapperLayer;

import com.champsoft.services.sales.DataLayer.Purchase.PurchaseOrder;
import com.champsoft.services.sales.PresentationLayer.PurchaseRequestModel;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface PurchaseOrderRequestModelMapper {

    @Mapping(target = "id", ignore = true)

    // ─── Embedded identifiers ──────────────────────────────────────────
    @Mapping(target = "flowerIdentifier.flowerNumber",   source = "flowerIdentificationNumber")
    @Mapping(target = "inventoryIdentifier.inventoryId", source = "inventoryId")
    @Mapping(target = "supplierIdentifier.supplierId",   source = "supplierId")
    @Mapping(target = "employeeIdentifier.employeeId",   source = "employeeId")
    @Mapping(target = "paymentIdentifier.paymentId",     source = "paymentId")   // ✅ NEW

    // ─── Price ─────────────────────────────────────────────────────────
    @Mapping(target = "price.amount",   source = "salePrice")
    @Mapping(target = "price.currency", source = "currency")

    // ─── Dates / status ────────────────────────────────────────────────
    @Mapping(target = "saleOfferDate",  source = "saleOfferDate")
    @Mapping(source = "salePurchaseStatus", target = "salePurchaseStatus")
    PurchaseOrder requestModelToEntity(PurchaseRequestModel dto);
}
