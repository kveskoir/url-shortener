package com.kvesko.urlshortener.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "short_url")
class ShortUrl(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID,
    val url: String
) {
    constructor() : this(UUID.randomUUID(), "")
    constructor(url: String) : this(UUID.randomUUID(), url)
}