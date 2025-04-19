package com.champsoft.services.sales.Mapper;

import com.champsoft.services.sales.DataLayer.Identifiers.*;
import com.champsoft.services.sales.DataLayer.Purchase.*;
import com.champsoft.services.sales.MapperLayer.PurchaseOrderRequestModelMapper;
import com.champsoft.services.sales.MapperLayer.PurchaseOrderResponseModelMapper;
import com.champsoft.services.sales.PresentationLayer.PurchaseRequestModel;
import com.champsoft.services.sales.PresentationLayer.PurchaseResponseModel;
import com.champsoft.services.sales.utils.Currency;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class PurchaseMapperTest {

    private final PurchaseOrderRequestModelMapper requestMapper =
            Mappers.getMapper(PurchaseOrderRequestModelMapper.class);

    private final PurchaseOrderResponseModelMapper responseMapper =
            new PurchaseOrderResponseModelMapper() {
                @Override
                public List<PurchaseResponseModel> entityToResponseModelList(List<PurchaseOrder> purchaseOrders) {
                    return purchaseOrders.stream()
                            .map(this::entityToResponseModel)
                            .collect(Collectors.toList());
                }
            };

    @Test
    void testRequestModelToEntityMapping() {
        PurchaseRequestModel request = PurchaseRequestModel.builder()
                .inventoryId("inv-1001")
                .flowerIdentificationNumber("fl-2001")
                .supplierId("sup-3001")
                .employeeId("emp-4001")
                .salePrice(120.50)
                .currency("CAD")
                .saleOfferDate(LocalDate.of(2025, 4, 1))
                .salePurchaseStatus(PurchaseStatus.COMPLETED)
                .build();

        PurchaseOrder entity = requestMapper.requestModelToEntity(request);

        assertEquals("inv-1001", entity.getInventoryIdentifier().getInventoryId());
        assertEquals("fl-2001", entity.getFlowerIdentifier().getFlowerNumber());
        assertEquals("sup-3001", entity.getSupplierIdentifier().getSupplierId());
        assertEquals("emp-4001", entity.getEmployeeIdentifier().getEmployeeId());
        assertEquals(new BigDecimal("120.5"), entity.getPrice().getAmount());
        assertEquals(Currency.CAD, entity.getPrice().getCurrency());
        assertEquals(LocalDate.of(2025, 4, 1), entity.getSaleOfferDate());
        assertEquals(PurchaseStatus.COMPLETED, entity.getSalePurchaseStatus());
    }

    @Test
    void testEntityToResponseModelMapping() {
        PurchaseOrder order = new PurchaseOrder();
        order.setPurchaseOrderIdentifier(new PurchaseOrderIdentifier("po-001"));
        order.setInventoryIdentifier(new InventoryIdentifier("inv-1001"));
        order.setFlowerIdentifier(new FlowerIdentifier("fl-2001"));
        order.setSupplierIdentifier(new SupplierIdentifier("sup-3001"));
        order.setEmployeeIdentifier(new EmployeeIdentifier("emp-4001"));
        order.setSaleOfferDate(LocalDate.of(2025, 4, 1));
        order.setSalePurchaseStatus(PurchaseStatus.PENDING);
        order.setPrice(new Price(new BigDecimal("89.99"), Currency.USD));

        PurchaseResponseModel response = responseMapper.entityToResponseModel(order);

        assertEquals("po-001", response.getPurchaseOrderId());
        assertEquals("inv-1001", response.getInventoryId());
        assertEquals("fl-2001", response.getFlowerIdentificationNumber());
        assertEquals("sup-3001", response.getSupplierId());
        assertEquals("emp-4001", response.getEmployeeId());
        assertEquals(new BigDecimal("89.99"), response.getSalePrice());
        assertEquals("USD", response.getCurrency());
        assertEquals(PurchaseStatus.PENDING, response.getSalePurchaseStatus());
        assertEquals(LocalDate.of(2025, 4, 1), response.getSaleOfferDate());
    }

    @Test
    void testEntityToResponseModelMapping_NullSafe() {
        assertNull(responseMapper.entityToResponseModel(null));
    }

    @Test
    void testEntityToResponseModelList() {
        PurchaseOrder order1 = new PurchaseOrder();
        order1.setPurchaseOrderIdentifier(new PurchaseOrderIdentifier("po-001"));

        PurchaseOrder order2 = new PurchaseOrder();
        order2.setPurchaseOrderIdentifier(new PurchaseOrderIdentifier("po-002"));

        List<PurchaseResponseModel> result = responseMapper.entityToResponseModelList(List.of(order1, order2));

        assertEquals(2, result.size());
        assertEquals("po-001", result.get(0).getPurchaseOrderId());
        assertEquals("po-002", result.get(1).getPurchaseOrderId());
    }
}
