package com.example.opencodedemo.pricing;

import java.math.BigDecimal;

/** 折扣计算结果。 */
public record DiscountResponse(
        BigDecimal originalPrice,
        BigDecimal discountPercent,
        BigDecimal finalPrice
) {
}
