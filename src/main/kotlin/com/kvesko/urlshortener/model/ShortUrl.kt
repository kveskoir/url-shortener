package com.kvesko.urlshortener.model

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "short_url")
class ShortUrl (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID,
    val url: String
) {
    constructor() : this(UUID.randomUUID(), "")
    constructor(url: String) : this(UUID.randomUUID(), url)
}