package backend.etec.controller

import backend.etec.dto.ErrorResponse
import backend.etec.exceptions.PrecondicionInvalidaException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler{

    @ExceptionHandler(PrecondicionInvalidaException::class)
    fun manejarPrecondicionInvalida(ex: PrecondicionInvalidaException): ResponseEntity<ErrorResponse> {
        val cuerpoError = ErrorResponse(error = ex.message ?: "Parámetros inválidos")
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cuerpoError)
    }
}
