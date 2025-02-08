package iut.nantes.project.products.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<Any> {
        val errors = mutableMapOf<String,String?>()
        ex.bindingResult.fieldErrors.forEach { error ->
            errors[error.field]=(error.defaultMessage ?: "Somes arguments are invalids.")
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors)
    }
}