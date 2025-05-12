/* ===================================
   PurchaseOrderServiceImpl.java
   =================================== */
package com.champsoft.services.sales.BusinessLayer;

import com.champsoft.services.sales.Client.EmployeesServiceClient;
import com.champsoft.services.sales.Client.InventoryServiceClient;
import com.champsoft.services.sales.Client.PaymentsServiceClient;
import com.champsoft.services.sales.Client.SuppliersServiceClient;
import com.champsoft.services.sales.DataLayer.Identifiers.*;
import com.champsoft.services.sales.DataLayer.Purchase.*;
import com.champsoft.services.sales.MapperLayer.PurchaseOrderRequestModelMapper;
import com.champsoft.services.sales.MapperLayer.PurchaseOrderResponseModelMapper;
import com.champsoft.services.sales.PresentationLayer.PurchaseRequestModel;
import com.champsoft.services.sales.PresentationLayer.PurchaseResponseModel;
import com.champsoft.services.sales.utils.Currency;
import com.champsoft.services.sales.utils.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderRequestModelMapper requestModelMapper;
    private final PurchaseOrderResponseModelMapper responseModelMapper;

    private final SuppliersServiceClient suppliersServiceClient;
    private final EmployeesServiceClient employeesServiceClient;
    private final InventoryServiceClient inventoryServiceClient;
    private final PaymentsServiceClient paymentsServiceClient;

    public PurchaseOrderServiceImpl(
            PurchaseOrderRepository purchaseOrderRepository,
            PurchaseOrderRequestModelMapper requestModelMapper,
            PurchaseOrderResponseModelMapper responseModelMapper,
            SuppliersServiceClient suppliersServiceClient,
            EmployeesServiceClient employeesServiceClient,
            InventoryServiceClient inventoryServiceClient,
            PaymentsServiceClient paymentsServiceClient // ✅ ADD THIS
    ) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.requestModelMapper = requestModelMapper;
        this.responseModelMapper = responseModelMapper;
        this.suppliersServiceClient = suppliersServiceClient;
        this.employeesServiceClient = employeesServiceClient;
        this.inventoryServiceClient = inventoryServiceClient;
        this.paymentsServiceClient = paymentsServiceClient; // ✅ ADD THIS
    }

    @Override
    public List<PurchaseResponseModel> getAllPurchaseOrders() {
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAll();
        log.info("Retrieved {} purchase orders from the database.", purchaseOrders.size());

        return purchaseOrders.stream()
                .map(order -> {
                    var response = responseModelMapper.entityToResponseModel(order);

                    if (response.getPaymentId() != null) {
                        try {
                            var payment = paymentsServiceClient.getPaymentById(response.getPaymentId());
                            response.setPaymentDetails(payment);
                        } catch (Exception ex) {
                            log.warn("Failed to fetch payment for {}: {}", response.getPaymentId(), ex.getMessage());
                            response.setPaymentDetails(null);
                        }
                    }

                    if (response.getInventoryId() != null) {
                        try {
                            var inventory = inventoryServiceClient.getInventoryById(response.getInventoryId());
                            response.setInventoryDetails(inventory);
                        } catch (Exception ex) {
                            log.warn("Failed to fetch inventory for {}: {}", response.getInventoryId(), ex.getMessage());
                            response.setInventoryDetails(null);
                        }
                    }

                    if (response.getInventoryId() != null && response.getFlowerIdentificationNumber() != null) {
                        try {
                            var flower = inventoryServiceClient.getFlowerByFlowerId(
                                    response.getInventoryId(),
                                    response.getFlowerIdentificationNumber());
                            response.setFlowerDetails(flower);
                        } catch (Exception ex) {
                            log.warn("Failed to fetch flower {} for inventory {}: {}", response.getFlowerIdentificationNumber(), response.getInventoryId(), ex.getMessage());
                            response.setFlowerDetails(null);
                        }
                    }

                    if (response.getSupplierId() != null) {
                        try {
                            var supplier = suppliersServiceClient.getSupplierBySupplierId(response.getSupplierId());
                            response.setSupplierDetails(supplier);
                        } catch (Exception ex) {
                            log.warn("Failed to fetch supplier {}: {}", response.getSupplierId(), ex.getMessage());
                            response.setSupplierDetails(null);
                        }
                    }

                    if (response.getEmployeeId() != null) {
                        try {
                            var employee = employeesServiceClient.getEmployeeByEmployeeId(response.getEmployeeId());
                            response.setEmployeeDetails(employee);
                        } catch (Exception ex) {
                            log.warn("Failed to fetch employee {}: {}", response.getEmployeeId(), ex.getMessage());
                            response.setEmployeeDetails(null);
                        }
                    }

                    return response;
                })
                .collect(Collectors.toList());
    }


    @Override
    public PurchaseResponseModel getPurchaseOrderById(String purchaseId) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository
                .findPurchaseOrderByPurchaseOrderIdentifierPurchaseId(purchaseId);

        if (purchaseOrder == null) {
            throw new NotFoundException("Purchase Order with ID " + purchaseId + " not found.");
        }

        var response = responseModelMapper.entityToResponseModel(purchaseOrder);

        if (response.getPaymentId() != null) {
            try {
                var payment = paymentsServiceClient.getPaymentById(response.getPaymentId());
                response.setPaymentDetails(payment);
            } catch (Exception ex) {
                log.warn("Failed to fetch payment for {}: {}", response.getPaymentId(), ex.getMessage());
                response.setPaymentDetails(null);
            }
        }

        if (response.getInventoryId() != null) {
            try {
                var inventory = inventoryServiceClient.getInventoryById(response.getInventoryId());
                response.setInventoryDetails(inventory);
            } catch (Exception ex) {
                log.warn("Failed to fetch inventory for {}: {}", response.getInventoryId(), ex.getMessage());
                response.setInventoryDetails(null);
            }
        }

        if (response.getInventoryId() != null && response.getFlowerIdentificationNumber() != null) {
            try {
                var flower = inventoryServiceClient.getFlowerByFlowerId(
                        response.getInventoryId(),
                        response.getFlowerIdentificationNumber());
                response.setFlowerDetails(flower);
            } catch (Exception ex) {
                log.warn("Failed to fetch flower {} for inventory {}: {}", response.getFlowerIdentificationNumber(), response.getInventoryId(), ex.getMessage());
                response.setFlowerDetails(null);
            }
        }

        if (response.getSupplierId() != null) {
            try {
                var supplier = suppliersServiceClient.getSupplierBySupplierId(response.getSupplierId());
                response.setSupplierDetails(supplier);
            } catch (Exception ex) {
                log.warn("Failed to fetch supplier {}: {}", response.getSupplierId(), ex.getMessage());
                response.setSupplierDetails(null);
            }
        }

        if (response.getEmployeeId() != null) {
            try {
                var employee = employeesServiceClient.getEmployeeByEmployeeId(response.getEmployeeId());
                response.setEmployeeDetails(employee);
            } catch (Exception ex) {
                log.warn("Failed to fetch employee {}: {}", response.getEmployeeId(), ex.getMessage());
                response.setEmployeeDetails(null);
            }
        }

        return response;
    }





    @Override
    public PurchaseResponseModel addPurchaseOrder(PurchaseRequestModel requestModel) {

        // ✅ Validate supplier
        var supplier = suppliersServiceClient.getSupplierBySupplierId(requestModel.getSupplierId());
        if (supplier == null) {
            throw new NotFoundException("Unknown supplier ID: " + requestModel.getSupplierId());
        }

        // ✅ Validate employee
        var employee = employeesServiceClient.getEmployeeByEmployeeId(requestModel.getEmployeeId());
        if (employee == null) {
            throw new NotFoundException("Unknown employee ID: " + requestModel.getEmployeeId());
        }

        // ✅ Validate inventory
        var inventory = inventoryServiceClient.getInventoryById(requestModel.getInventoryId());
        if (inventory == null) {
            throw new NotFoundException("Unknown inventory ID: " + requestModel.getInventoryId());
        }

        // ✅ Validate flower (requires inventoryId + flowerId)
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

        // ✅ Validate payment (optional)
        if (requestModel.getPaymentId() != null) {
            var payment = paymentsServiceClient.getPaymentById(requestModel.getPaymentId());
            if (payment == null) {
                throw new NotFoundException("Unknown payment ID: " + requestModel.getPaymentId());
            }
        }

        // ✅ Map request model to entity
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
        if (requestModel.getPaymentId() != null) {
            existingOrder.setPaymentIdentifier(new PaymentIdentifier(requestModel.getPaymentId()));
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
