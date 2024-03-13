package com.kvesko.urlshortener.dto

import java.util.UUID

data class ShortenUrlResponse (
    val id: UUID,
    val url: String
)