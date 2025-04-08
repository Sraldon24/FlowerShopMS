/* =============================
   InventoryRequestMapper.java
   ============================= */
package com.champsoft.services.inventory.MapperLayer.Inventory;

import com.champsoft.services.inventory.DataLayer.Inventory.Inventory;
import com.champsoft.services.inventory.PresentationLayer.Inventory.InventoryRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InventoryRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "inventoryIdentifier", ignore = true)
    Inventory requestModelToEntity(InventoryRequestModel requestModel);
}
