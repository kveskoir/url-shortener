package com.kvesko.urlshortener.web

import com.kvesko.urlshortener.config.RateLimitConfig
import com.kvesko.urlshortener.model.ShortUrl
import com.kvesko.urlshortener.model.exceptions.TooManyRequestsException
import com.kvesko.urlshortener.repoitory.ShortUrlRepository
import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import io.github.bucket4j.Refill
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpClientErrorException.TooManyRequests
import java.time.Duration
import java.util.*

@RestController
@RequestMapping("shortcut")
class UrlShortenerController (
    private val repository: ShortUrlRepository,
    rateLimitConfig: RateLimitConfig
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
        .build();

    @GetMapping("/test/{UUID}")
    fun getFullUrl(@PathVariable("UUID") id: UUID): String = executeWithRateLimit{repository.findById(id)}.get().url

    @PostMapping("/test")
    fun shortenUrl(@RequestParam("url") url: String): ResponseEntity<ShortUrl> {
        val shortUrl = repository.save(ShortUrl(url))
        return ResponseEntity(shortUrl, HttpStatus.CREATED)
    }

    private fun <R> executeWithRateLimit(supplier: () -> R): R {
        if (bucket.tryConsume(1)) {
            return supplier.invoke()
        }
        throw TooManyRequestsException("Some message")
    }
}