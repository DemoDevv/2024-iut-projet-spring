package iut.nantes.project.stores.exceptions

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalHandler {
    @ExceptionHandler(Exception::class)
    fun handleGeneralException(ex: Exception): ResponseEntity<Map<String, String>> {
        val body = mapOf("error" to "An unexpected error occurred", "details" to ex.message!!)
        return ResponseEntity.status(500).body(body)
    }

}