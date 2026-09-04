package com.example.opencodedemo.common;

import java.time.Instant;

/** API 错误响应。 */
public record ApiError(
        String code,
        String message,
        Instant timestamp
) {
}
