package com.kvesko.urlshortener.service

import com.kvesko.urlshortener.dto.ShortenUrlRequest
import com.kvesko.urlshortener.model.ShortUrl
import java.util.UUID

interface IUrlShortenerService {
    fun shortenUrl(request: ShortenUrlRequest): ShortUrl

    fun getUrl(id: UUID): ShortUrl
}