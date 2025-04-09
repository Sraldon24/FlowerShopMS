/* ===================================
   PurchaseOrderServiceImpl.java
   =================================== */
package com.champsoft.services.sales.BusinessLayer;

import com.champsoft.services.sales.Client.EmployeesServiceClient;
import com.champsoft.services.sales.Client.InventoryServiceClient;
import com.champsoft.services.sales.Client.SuppliersServiceClient;
import com.champsoft.services.sales.DataLayer.Identifiers.EmployeeIdentifier;
import com.champsoft.services.sales.DataLayer.Identifiers.FlowerIdentifier;
import com.champsoft.services.sales.DataLayer.Identifiers.InventoryIdentifier;
import com.champsoft.services.sales.DataLayer.Identifiers.SupplierIdentifier;
import com.champsoft.services.sales.DataLayer.Purchase.*;
import com.champsoft.services.sales.MapperLayer.PurchaseOrderRequestModelMapper;
import com.champsoft.services.sales.MapperLayer.PurchaseOrderResponseModelMapper;
import com.champsoft.services.sales.PresentationLayer.PurchaseRequestModel;
import com.champsoft.services.sales.PresentationLayer.PurchaseResponseModel;
import com.champsoft.services.sales.utils.Currency;
import com.champsoft.services.sales.utils.NotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderRequestModelMapper requestModelMapper;
    private final PurchaseOrderResponseModelMapper responseModelMapper;

    private final SuppliersServiceClient suppliersServiceClient;
    private final EmployeesServiceClient employeesServiceClient;
    private final InventoryServiceClient inventoryServiceClient;

    public PurchaseOrderServiceImpl(
            PurchaseOrderRepository purchaseOrderRepository,
            PurchaseOrderRequestModelMapper requestModelMapper,
            PurchaseOrderResponseModelMapper responseModelMapper,
            SuppliersServiceClient suppliersServiceClient,
            EmployeesServiceClient employeesServiceClient,
            InventoryServiceClient inventoryServiceClient
    ) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.requestModelMapper = requestModelMapper;
        this.responseModelMapper = responseModelMapper;
        this.suppliersServiceClient = suppliersServiceClient;
        this.employeesServiceClient = employeesServiceClient;
        this.inventoryServiceClient = inventoryServiceClient;
    }

    @Override
    public List<PurchaseResponseModel> getAllPurchaseOrders() {
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();
        return purchaseOrders.stream()
                .map(responseModelMapper::entityToResponseModel)
                .collect(Collectors.toList());
    }

    @Override
    public PurchaseResponseModel getPurchaseOrderById(String purchaseId) {
        PurchaseOrder purchaseOrder =
                purchaseOrderRepository.findPurchaseOrderByPurchaseOrderIdentifierPurchaseId(purchaseId);

        if (purchaseOrder == null) {
            throw new NotFoundException("Purchase Order with ID " + purchaseId + " not found.");
        }

        return responseModelMapper.entityToResponseModel(purchaseOrder);
    }

    @Override
    public PurchaseResponseModel addPurchaseOrder(PurchaseRequestModel requestModel) {
        var supplier = suppliersServiceClient.getSupplierBySupplierId(requestModel.getSupplierId());
        if (supplier == null) {
            throw new NotFoundException("Unknown supplier ID: " + requestModel.getSupplierId());
        }

        var employee = employeesServiceClient.getEmployeeByEmployeeId(requestModel.getEmployeeId());
        if (employee == null) {
            throw new NotFoundException("Unknown employee ID: " + requestModel.getEmployeeId());
        }

        var flower = inventoryServiceClient.getFlowerByFlowerId(
                requestModel.getInventoryId(),
                requestModel.getFlowerIdentificationNumber()
        );
        if (flower == null) {
            throw new NotFoundException(
                    "Flower " + requestModel.getFlowerIdentificationNumber() +
                            " is not found in inventory " + requestModel.getInventoryId()
            );
        }

        PurchaseOrder purchaseOrder = requestModelMapper.requestModelToEntity(requestModel);

        if (purchaseOrder.getPurchaseOrderIdentifier() == null) {
            purchaseOrder.setPurchaseOrderIdentifier(new PurchaseOrderIdentifier());
        }

        if (purchaseOrder.getPrice() == null) {
            purchaseOrder.setPrice(new Price(BigDecimal.ZERO, Currency.USD));
        }

        if (requestModel.getSalePurchaseStatus() == null) {
            purchaseOrder.setSalePurchaseStatus(PurchaseStatus.PENDING);
        } else {
            purchaseOrder.setSalePurchaseStatus(requestModel.getSalePurchaseStatus());
        }

        PurchaseOrder savedOrder = purchaseOrderRepository.save(purchaseOrder);
        return responseModelMapper.entityToResponseModel(savedOrder);
    }

    @Override
    public PurchaseResponseModel updatePurchaseOrder(String purchaseId, PurchaseRequestModel requestModel) {
        PurchaseOrder existingOrder = purchaseOrderRepository
                .findPurchaseOrderByPurchaseOrderIdentifierPurchaseId(purchaseId);

        if (existingOrder == null) {
            throw new NotFoundException("No purchase order found with ID " + purchaseId);
        }

        existingOrder.setSaleOfferDate(requestModel.getSaleOfferDate());
        existingOrder.setSalePurchaseStatus(requestModel.getSalePurchaseStatus());

        if (requestModel.getInventoryId() != null) {
            existingOrder.setInventoryIdentifier(new InventoryIdentifier(requestModel.getInventoryId()));
        }

        if (requestModel.getFlowerIdentificationNumber() != null) {
            existingOrder.setFlowerIdentifier(new FlowerIdentifier(requestModel.getFlowerIdentificationNumber()));
        }

        if (requestModel.getSupplierId() != null) {
            existingOrder.setSupplierIdentifier(new SupplierIdentifier(requestModel.getSupplierId()));
        }

        if (requestModel.getEmployeeId() != null) {
            existingOrder.setEmployeeIdentifier(new EmployeeIdentifier(requestModel.getEmployeeId())); // ✅ FIXED
        }

        if (requestModel.getSalePrice() != null
                && requestModel.getCurrency() != null) {
            existingOrder.setPrice(new Price(
                    BigDecimal.valueOf(requestModel.getSalePrice()),
                    Currency.valueOf(requestModel.getCurrency())
            ));
        }

        if (requestModel.getFinancingAgreementDetails() != null) {
            existingOrder.getFinancingAgreementDetails().setNumberOfMonthlyPayments(
                    requestModel.getFinancingAgreementDetails().getNumberOfMonthlyPayments()
            );
            existingOrder.getFinancingAgreementDetails().setMonthlyPaymentAmount(
                    requestModel.getFinancingAgreementDetails().getMonthlyPaymentAmount()
            );
            existingOrder.getFinancingAgreementDetails().setDownPaymentAmount(
                    requestModel.getFinancingAgreementDetails().getDownPaymentAmount()
            );
        }

        PurchaseOrder updatedOrder = purchaseOrderRepository.save(existingOrder);
        return responseModelMapper.entityToResponseModel(updatedOrder);
    }


    @Override
    public void deletePurchaseOrder(String purchaseId) {
        PurchaseOrder existingOrder =
                purchaseOrderRepository.findPurchaseOrderByPurchaseOrderIdentifierPurchaseId(purchaseId);

        if (existingOrder == null) {
            throw new NotFoundException("No purchase order found with ID " + purchaseId);
        }

        purchaseOrderRepository.delete(existingOrder);
    }
}
