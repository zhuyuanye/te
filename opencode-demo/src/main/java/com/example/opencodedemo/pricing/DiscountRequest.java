package com.example.opencodedemo.pricing;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/** 折扣计算请求。 */
public record DiscountRequest(
        @NotNull(message = "原价不能为空")
        @DecimalMin(value = "0.00", message = "原价不能小于 0")
        BigDecimal originalPrice,

        @NotNull(message = "折扣百分比不能为空")
        @DecimalMin(value = "0.00", message = "折扣百分比不能小于 0")
        @DecimalMax(value = "100.00", message = "折扣百分比不能大于 100")
        BigDecimal discountPercent
) {
}
