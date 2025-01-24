package iut.nantes.project.stores.controllers.dto

import iut.nantes.project.stores.controllers.entities.ContactEntity
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class Address(
    @field:Size(min = 5, max = 50)
    val street: String,
    @field:Size(min = 1, max = 30)
    val city: String,
    @field:Size(min = 5, max = 5)
    val postalCode: String
) {
    constructor(): this("", "", "")
}

data class ContactDto(
    val id: Long,
    @NotBlank
    @Email
    val email: String,
    @field:Size(min = 10, max = 10)
    val phone: String,
    val address: Address
) {
    fun toEntity(): ContactEntity {
        return ContactEntity(this.id, this.email, this.phone, this.address)
    }
}