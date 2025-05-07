package org.example.Inventory.PresentationLayer.Flower;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlowerHateoasWrapper {

    @JsonProperty("_embedded")
    private Embedded embedded;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Embedded {
        @JsonProperty("flowerResponseModelList") // MUST match microservice response
        private List<FlowerResponseModel> flowerResponseModelList;
    }
}
