package org.example.Sales.DataLayer;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancingAgreementDetails {
    private Integer numberOfMonthlyPayments;
    private Double monthlyPaymentAmount;
    private Double downPaymentAmount;
}
