package org.example.Suppliers.PresentationLayer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierHateoasWrapper {

    @JsonProperty("_embedded")
    private Embedded embedded;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Embedded {
        @JsonProperty("supplierResponseModelList")
        private List<SupplierResponseModel> supplierList;
    }
}
