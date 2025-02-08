package iut.nantes.project.stores.controllers.dto

import iut.nantes.project.stores.controllers.entities.ContactEntity
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class Address(
    @field:Size(min = 5, max = 50, message = "the size of the street must be between 5 and 50 caracters")
    val street: String,
    @field:Size(min = 1, max = 30, message = "the size of the city must be between 1 and 30 caracters")
    val city: String,
    @field:Size(min = 5, max = 5, message = "postalCode must be a french postal code (for example:49100)")
    val postalCode: String
) {
    constructor() : this("", "", "")
}

data class ContactDto(
    val id: Long,
    @NotBlank
    @Email
    val email: String,
    @field:Size(min = 10, max = 10, message = "The phone number is a french number. (example: 012345678)")
    val phone: String,
    @field:Valid
    val address: Address
) {
    fun toEntity(): ContactEntity {
        return ContactEntity(this.id, this.email, this.phone, this.address)
    }
}