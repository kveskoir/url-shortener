package com.kvesko.urlshortener.config

import com.kvesko.urlshortener.model.exceptions.ErrorMessageModel
import com.kvesko.urlshortener.model.exceptions.TooManyRequestsException
import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionControllerAdvice {

    @ExceptionHandler
    fun handleIllegalStateException(ex: TooManyRequestsException): ResponseEntity<ErrorMessageModel> {

        val errorMessage = ErrorMessageModel(
            HttpStatus.TOO_MANY_REQUESTS.value(),
            ex.message
        )
        return ResponseEntity(errorMessage, HttpStatus.TOO_MANY_REQUESTS)
    }

    @ExceptionHandler
    fun handleIllegalStateException(ex: ChangeSetPersister.NotFoundException): ResponseEntity<ErrorMessageModel> {

        val errorMessage = ErrorMessageModel(
            HttpStatus.NOT_FOUND.value(),
            ex.message
        )
        return ResponseEntity(errorMessage, HttpStatus.NOT_FOUND)
    }
}