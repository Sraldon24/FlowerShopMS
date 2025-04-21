package com.champsoft.services.payment.MapperLayer;

import com.champsoft.services.payment.DataLayer.Payment;
import com.champsoft.services.payment.PresentationLayer.PaymentRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentRequestMapper {

    @Mapping(target = "paymentIdentifier", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "method", expression = "java(com.champsoft.services.payment.DataLayer.PaymentMethod.valueOf(request.getMethod()))")
    @Mapping(target = "status", expression = "java(com.champsoft.services.payment.DataLayer.PaymentStatus.valueOf(request.getStatus()))")
    Payment requestModelToEntity(PaymentRequestModel request);
}
