package com.kvesko.urlshortener.converter

import com.kvesko.urlshortener.dto.ShortenUrlResponse
import com.kvesko.urlshortener.model.ShortUrl
import org.springframework.stereotype.Component

@Component
class ShortUrlResponseConverter {
    fun convertToResponse(entity: ShortUrl): ShortenUrlResponse = ShortenUrlResponse(entity.id, entity.url)
}