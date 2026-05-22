package com.fintech.prototype.gateway.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class GatewayContractsTest {

    @Test
    void consultRequestCarriesClientCorrelationIdentifier() {
        ConsultRequestDTO request = new ConsultRequestDTO("abc-123", "0001", "12345-6");

        assertThat(request.identifier()).isEqualTo("abc-123");
        assertThat(request.agency()).isEqualTo("0001");
        assertThat(request.account()).isEqualTo("12345-6");
    }

    @Test
    void withdrawalRequestCarriesAmountAndPassword() {
        CashWithdrawalRequestDTO request = new CashWithdrawalRequestDTO(BigDecimal.TEN, "1234");

        assertThat(request.amount()).isEqualByComparingTo(BigDecimal.TEN);
        assertThat(request.password()).isEqualTo("1234");
    }
}
