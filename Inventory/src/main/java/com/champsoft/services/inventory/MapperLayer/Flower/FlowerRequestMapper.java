/* ===========================
   FlowerRequestMapper.java
   =========================== */
package com.champsoft.services.inventory.MapperLayer.Flower;

import com.champsoft.services.inventory.DataLayer.Flowers.Flower;
import com.champsoft.services.inventory.MapperLayer.OptionMapper;
import com.champsoft.services.inventory.MapperLayer.PriceMapper;
import com.champsoft.services.inventory.PresentationLayer.Flower.FlowerRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OptionMapper.class, PriceMapper.class})
public interface FlowerRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flowersIdentifier.flowerNumber", source = "flowerId")
    @Mapping(target = "inventoryIdentifier.inventoryId", source = "inventoryId")

    @Mapping(target = "flowerName", source = "flowerName")
    @Mapping(target = "flowerColor", source = "flowerColor")
    @Mapping(target = "flowerCategory", source = "flowerCategory")

    @Mapping(target = "status", source = "flowerStatus")
    @Mapping(target = "stockQuantity", source = "stockQuantity")

    @Mapping(target = "supplierIdentifier", source = "supplierIdentifier") // ✅ Corrected from supplierId

    @Mapping(source = "options", target = "options", qualifiedByName = "dtoListToEntityList")

    // ✅ Instead of separate mapping, correctly map price and currency inside PriceMapper
    @Mapping(target = "price", expression = "java(new com.champsoft.services.inventory.DataLayer.Flowers.Price(flowerRequestModel.getPrice(), com.champsoft.services.utils.Currency.valueOf(flowerRequestModel.getCurrency())))")
    Flower requestModelToEntity(FlowerRequestModel flowerRequestModel);
}

