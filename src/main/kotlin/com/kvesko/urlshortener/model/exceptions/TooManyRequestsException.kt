package com.kvesko.urlshortener.model.exceptions

class TooManyRequestsException(message: String) : RuntimeException(message) {
}