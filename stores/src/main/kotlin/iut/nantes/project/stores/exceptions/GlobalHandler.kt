package iut.nantes.project.stores.exceptions

import org.springframework.http.HttpStatus
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

    @ExceptionHandler(InvalidIdFormatException::class)
    fun handleInvalidIdFormatException(ex: InvalidIdFormatException): ResponseEntity<Unit> =
        ResponseEntity.status(
            HttpStatus.BAD_REQUEST
        ).build()

    @ExceptionHandler(ConflictException::class)
    fun handleConflictException(ex: ConflictException): ResponseEntity<Unit> =
        ResponseEntity.status(HttpStatus.CONFLICT).build()

    @ExceptionHandler(ContactNotFoundException::class)
    fun handleContactNotFoundException(ex: ContactNotFoundException): ResponseEntity<Unit> =
        ResponseEntity.status(HttpStatus.NOT_FOUND).build()

    @ExceptionHandler(StoreNotFoundException::class)
    fun handleStoreNotFoundException(ex: StoreNotFoundException): ResponseEntity<Unit> =
        ResponseEntity.status(HttpStatus.NOT_FOUND).build()
}