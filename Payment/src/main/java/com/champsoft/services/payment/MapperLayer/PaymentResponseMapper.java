
package com.champsoft.services.payment.MapperLayer;

import com.champsoft.services.payment.DataLayer.Payment;
import com.champsoft.services.payment.PresentationLayer.PaymentResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentResponseMapper {

    @Mapping(target = "method", source = "method")
    @Mapping(target = "status", source = "status")
    PaymentResponseModel entityToResponseModel(Payment payment);

    List<PaymentResponseModel> entityListToResponseModelList(List<Payment> payments);
}
