package iut.nantes.project.stores.exceptions

import jakarta.validation.constraints.Email
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
    fun handleInvalidIdFormatException(ex: InvalidIdFormatException): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID must contain only digits.")

    @ExceptionHandler(ConflictException::class)
    fun handleConflictException(ex: ConflictException): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.CONFLICT).body("Warning : you tried to delete a element who's present in another structure.\n Please verify the content of these structures.")

    @ExceptionHandler(InvalidRequestParameters::class)
    fun handleInvalidRequestParameters(ex: InvalidRequestParameters): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body("the parameter(s) filled in the URL doesn't match with the expected format. (example: quantity is negative.)")


    //Exception contacts
    @ExceptionHandler(ContactNotFoundException::class)
    fun handleContactNotFoundException(ex: ContactNotFoundException): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body("Contact not found.")

    @ExceptionHandler(EmailNotValidExeception::class)
    fun handleEmailNotValidException(ex:EmailNotValidExeception): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email format doesn't match.")

    @ExceptionHandler(PhoneNumberException::class)
    fun handlePhoneNumberNotValidException(ex:PhoneNumberException): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The phone number must be a french number (example: 012345678)")

    @ExceptionHandler(StreetNotValidException::class)
    fun handleStreetNotValidException(ex:StreetNotValidException): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The street must be between  5 and 50 caracters")

    @ExceptionHandler(CityNotValidException::class)
    fun handleCityNotValidException(ex:CityNotValidException): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The city must be between  1 and 30 caracters")

    @ExceptionHandler(PostalCodeNotValidException::class)
    fun handlePostalCodeNotValidException(ex:PostalCodeNotValidException): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A postal Code must be a french code (example:49140).")





    //Exception Stores
    @ExceptionHandler(StoreNotFoundException::class)
    fun handleStoreNotFoundException(ex: StoreNotFoundException): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body("Store not found.")


    //Exception produits appellés.
    @ExceptionHandler(ProductNotPresentInStoreException::class)
    fun handleProductNotPresentInStoreException(ex: ProductNotPresentInStoreException): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body("This product hasn't been found in this store")

    @ExceptionHandler(ExcessiveProductRemovalException::class)
    fun handleExcessiveProductRemovalException(ex: ExcessiveProductRemovalException): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.CONFLICT).body("You tried to delete too much quantity from a product (example: tried to delete 3 quantity on a product who have 1 quantity in this store).")

    @ExceptionHandler(DuplicateElementsException::class)
    fun handleDuplicateElementsException(ex: DuplicateElementsException): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Warning: There are duplicates elements in this list.")
}