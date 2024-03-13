package com.kvesko.urlshortener.web

import com.kvesko.urlshortener.config.RateLimitConfig
import com.kvesko.urlshortener.converter.ShortUrlResponseConverter
import com.kvesko.urlshortener.dto.ShortenUrlRequest
import com.kvesko.urlshortener.dto.ShortenUrlResponse
import com.kvesko.urlshortener.model.exceptions.TooManyRequestsException
import com.kvesko.urlshortener.service.IUrlShortenerService
import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import io.github.bucket4j.Refill
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Duration
import java.util.UUID

@RestController
@RequestMapping("shortcut")
class UrlShortenerController(
    private val service: IUrlShortenerService,
    private val shortUrlResponseConverter: ShortUrlResponseConverter,
    rateLimitConfig: RateLimitConfig,
) {
    private final val limit: Bandwidth = Bandwidth
        .classic(
            rateLimitConfig.capacity,
            Refill.greedy(
                rateLimitConfig.tokens,
                Duration.ofMinutes(rateLimitConfig.periodInMinutes)
            )
        )

    private var bucket: Bucket = Bucket.builder()
        .addLimit(limit)
        .build()

    @GetMapping("/url/{UUID}")
    fun getFullUrl(@PathVariable("UUID") id: UUID): ResponseEntity<ShortenUrlResponse> {
        val url = executeWithRateLimit { service.getUrl(id) }
        val response = shortUrlResponseConverter.convertToResponse(url)
        return ResponseEntity(response, HttpStatus.OK)
    }

    @PostMapping("/url")
    fun shortenUrl(@RequestBody request: ShortenUrlRequest): ResponseEntity<ShortenUrlResponse> {
        val shortUrl = executeWithRateLimit { service.shortenUrl(request) }
        val response = shortUrlResponseConverter.convertToResponse(shortUrl)
        return ResponseEntity(response, HttpStatus.CREATED)
    }

    private fun <R> executeWithRateLimit(supplier: () -> R): R {
        if (bucket.tryConsume(1)) {
            return supplier.invoke()
        }
        throw TooManyRequestsException("Some message that should be localized in bundles...")
    }
}