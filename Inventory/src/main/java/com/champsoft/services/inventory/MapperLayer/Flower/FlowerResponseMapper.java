/* ============================
   FlowerResponseMapper.java
   ============================ */
package com.champsoft.services.inventory.MapperLayer.Flower;

import com.champsoft.services.inventory.DataLayer.Flowers.Flower;
import com.champsoft.services.inventory.MapperLayer.OptionMapper;
import com.champsoft.services.inventory.MapperLayer.PriceMapper;
import com.champsoft.services.inventory.PresentationLayer.Flower.FlowerResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OptionMapper.class, PriceMapper.class})
public interface FlowerResponseMapper {

    @Mapping(source = "flowersIdentifier.flowerNumber", target = "flowerId")
    @Mapping(source = "inventoryIdentifier.inventoryId", target = "inventoryId")
    @Mapping(source = "flowerName", target = "flowerName")
    @Mapping(source = "flowerColor", target = "flowerColor")
    @Mapping(source = "flowerCategory", target = "flowerCategory")
    @Mapping(source = "status", target = "flowerStatus")
    @Mapping(source = "stockQuantity", target = "stockQuantity")
    @Mapping(source = "supplierIdentifier", target = "supplierIdentifier")
    @Mapping(source = "price.amount", target = "price")
    @Mapping(source = "price.currency", target = "currency")
    @Mapping(source = "options", target = "options", qualifiedByName = "entityListToDtoList")
    @Mapping(target = "supplier", ignore = true)
        // <--- add this line
    FlowerResponseModel entityToResponseModel(Flower flower);

    List<FlowerResponseModel> entityListToResponseModelList(List<Flower> flowers);
}

