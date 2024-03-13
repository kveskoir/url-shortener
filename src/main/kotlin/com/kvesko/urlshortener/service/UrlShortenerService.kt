package com.kvesko.urlshortener.service

import com.kvesko.urlshortener.dto.ShortenUrlRequest
import com.kvesko.urlshortener.model.ShortUrl
import com.kvesko.urlshortener.repoitory.ShortUrlRepository
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UrlShortenerService(private val repository: ShortUrlRepository) : IUrlShortenerService {
    @CachePut(value = ["url"], key = "#result.id")
    override fun shortenUrl(request: ShortenUrlRequest): ShortUrl {
        return repository.save(ShortUrl(request.url))
    }

    @Cacheable(value = ["url"], key = "#id")
    override fun getUrl(id: UUID): ShortUrl {
        return repository.findById(id).orElseThrow { NotFoundException() }
    }
}