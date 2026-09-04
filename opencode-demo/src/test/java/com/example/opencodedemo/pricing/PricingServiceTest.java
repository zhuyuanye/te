package com.example.opencodedemo.pricing;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PricingServiceTest {

    private final PricingService pricingService = new PricingService();

    @Test
    void 应按百分比计算折后价() {
        BigDecimal result = pricingService.applyPercentageDiscount(
                new BigDecimal("100.00"),
                new BigDecimal("20")
        );

        assertThat(result).isEqualByComparingTo("80.00");
    }

    @Test
    void 应将金额四舍五入到两位小数() {
        BigDecimal result = pricingService.applyPercentageDiscount(
                new BigDecimal("19.99"),
                new BigDecimal("10")
        );

        assertThat(result).isEqualByComparingTo("17.99");
    }

    @Test
    void 应支持折扣边界值() {
        assertThat(pricingService.applyPercentageDiscount(
                new BigDecimal("50"),
                BigDecimal.ZERO
        )).isEqualByComparingTo("50.00");

        assertThat(pricingService.applyPercentageDiscount(
                new BigDecimal("50"),
                new BigDecimal("100")
        )).isEqualByComparingTo("0.00");
    }

    @Test
    void 原价为负数时应拒绝计算() {
        assertThatThrownBy(() -> pricingService.applyPercentageDiscount(
                new BigDecimal("-1"),
                new BigDecimal("10")
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("原价必须是大于或等于 0 的数字");
    }

    @Test
    void 折扣百分比超出范围时应拒绝计算() {
        assertThatThrownBy(() -> pricingService.applyPercentageDiscount(
                new BigDecimal("10"),
                new BigDecimal("101")
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("折扣百分比必须在 0 到 100 之间");
    }
}
