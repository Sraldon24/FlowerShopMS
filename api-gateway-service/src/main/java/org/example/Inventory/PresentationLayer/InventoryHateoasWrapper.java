package org.example.Inventory.PresentationLayer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryHateoasWrapper {
    @JsonProperty("_embedded")
    private Embedded embedded;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Embedded {
        @JsonProperty("inventoryResponseModelList") // must match backend
        private List<InventoryResponseModel> inventoryList;
    }
}