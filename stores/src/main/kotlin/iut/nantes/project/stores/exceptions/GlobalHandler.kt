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
    fun handleInvalidIdFormatException(ex: InvalidIdFormatException): ResponseEntity<Unit> =
        ResponseEntity.status(
            HttpStatus.BAD_REQUEST
        ).build()

    @ExceptionHandler(ConflictException::class)
    fun handleConflictException(ex: ConflictException): ResponseEntity<Unit> =
        ResponseEntity.status(HttpStatus.CONFLICT).build()

    @ExceptionHandler(InvalidRequestParameters::class)
    fun handleInvalidRequestParameters(ex: InvalidRequestParameters): ResponseEntity<Unit> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).build()


    //Exception contacts
    @ExceptionHandler(ContactNotFoundException::class)
    fun handleContactNotFoundException(ex: ContactNotFoundException): ResponseEntity<Unit> =
        ResponseEntity.status(HttpStatus.NOT_FOUND).build()

    @ExceptionHandler(EmailNotValidExeception::class)
    fun handleEmailNotValidException(ex:EmailNotValidExeception): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le format de l'email n'est pas bon")

    @ExceptionHandler(PhoneNumberException::class)
    fun handlePhoneNumberNotValidException(ex:PhoneNumberException): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le numéro de téléphone doit contenir 10 chiffres")

    @ExceptionHandler(StreetNotValidException::class)
    fun handleStreetNotValidException(ex:StreetNotValidException): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La rue doit contenir entre 5 et 50 caractères")

    @ExceptionHandler(CityNotValidException::class)
    fun handleCityNotValidException(ex:CityNotValidException): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La ville doit contenir entre 1 et 30 caractères")

    @ExceptionHandler(PostalCodeNotValidException::class)
    fun handlePostalCodeNotValidException(ex:PostalCodeNotValidException): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body("un code postal contient 5 chiffres.")





    //Exception Stores
    @ExceptionHandler(StoreNotFoundException::class)
    fun handleStoreNotFoundException(ex: StoreNotFoundException): ResponseEntity<Unit> =
        ResponseEntity.status(HttpStatus.NOT_FOUND).build()


    //Exception produits appellés.
    @ExceptionHandler(ProductNotPresentInStoreException::class)
    fun handleProductNotPresentInStoreException(ex: ProductNotPresentInStoreException): ResponseEntity<Unit> =
        ResponseEntity.status(HttpStatus.NOT_FOUND).build()

    @ExceptionHandler(ExcessiveProductRemovalException::class)
    fun handleExcessiveProductRemovalException(ex: ExcessiveProductRemovalException): ResponseEntity<Unit> =
        ResponseEntity.status(HttpStatus.CONFLICT).build()

    @ExceptionHandler(DuplicateElementsException::class)
    fun handleDuplicateElementsException(ex: DuplicateElementsException): ResponseEntity<Unit> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
}