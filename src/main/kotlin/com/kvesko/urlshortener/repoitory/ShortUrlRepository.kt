package com.kvesko.urlshortener.repoitory

import com.kvesko.urlshortener.model.ShortUrl
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ShortUrlRepository : CrudRepository<ShortUrl, UUID> {

}