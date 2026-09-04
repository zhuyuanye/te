package com.example.opencodedemo.pricing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PricingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 应返回折扣计算结果() throws Exception {
        mockMvc.perform(post("/api/prices/discount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "originalPrice": 100.00,
                                  "discountPercent": 20
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.originalPrice").value(100.00))
                .andExpect(jsonPath("$.discountPercent").value(20))
                .andExpect(jsonPath("$.finalPrice").value(80.00));
    }

    @Test
    void 参数不合法时应返回四百错误() throws Exception {
        mockMvc.perform(post("/api/prices/discount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "originalPrice": -1,
                                  "discountPercent": 20
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"))
                .andExpect(jsonPath("$.message").value("原价不能小于 0"));
    }
}
