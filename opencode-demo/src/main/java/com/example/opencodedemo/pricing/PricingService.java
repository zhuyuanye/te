package com.example.opencodedemo.pricing;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** 价格计算领域服务。 */
@Service
public class PricingService {

    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    /**
     * 按百分比计算折后价。
     *
     * @param originalPrice 原价，必须大于或等于 0
     * @param discountPercent 折扣百分比，取值范围为 0 到 100
     * @return 四舍五入并保留两位小数的折后价
     */
    public BigDecimal applyPercentageDiscount(
            BigDecimal originalPrice,
            BigDecimal discountPercent
    ) {
        validate(originalPrice, discountPercent);

        BigDecimal discountRate = discountPercent.divide(ONE_HUNDRED);
        return originalPrice
                .multiply(BigDecimal.ONE.subtract(discountRate))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private void validate(BigDecimal originalPrice, BigDecimal discountPercent) {
        if (originalPrice == null || originalPrice.signum() < 0) {
            throw new IllegalArgumentException("原价必须是大于或等于 0 的数字");
        }
        if (discountPercent == null
                || discountPercent.signum() < 0
                || discountPercent.compareTo(ONE_HUNDRED) > 0) {
            throw new IllegalArgumentException("折扣百分比必须在 0 到 100 之间");
        }
    }
}
