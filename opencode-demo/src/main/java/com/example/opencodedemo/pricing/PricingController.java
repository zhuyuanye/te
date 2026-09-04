package com.example.opencodedemo.pricing;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** 提供价格计算接口。 */
@RestController
@RequestMapping("/api/prices")
public class PricingController {

    private final PricingService pricingService;

    public PricingController(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    /** 计算百分比折扣后的最终价格。 */
    @PostMapping("/discount")
    @ResponseStatus(HttpStatus.OK)
    public DiscountResponse applyDiscount(@Valid @RequestBody DiscountRequest request) {
        return new DiscountResponse(
                request.originalPrice(),
                request.discountPercent(),
                pricingService.applyPercentageDiscount(
                        request.originalPrice(),
                        request.discountPercent()
                )
        );
    }
}
