package org.example.Payment.PresentationLayer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentHateoasWrapper {

    @JsonProperty("_embedded")
    private Embedded embedded;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Embedded {
        @JsonProperty("paymentResponseModelList")
        private List<PaymentResponseModel> paymentList;
    }
}
