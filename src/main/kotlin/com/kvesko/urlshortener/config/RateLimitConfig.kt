package com.kvesko.urlshortener.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "rate-limit")
data class RateLimitConfig(
    var capacity: Long = 1,
    var tokens: Long = 20,
    var periodInMinutes: Long = 1
)