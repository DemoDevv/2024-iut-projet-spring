package iut.nantes.project.products.controllers.dto

import iut.nantes.project.products.controllers.entities.ProductEntity
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class Price(
    @field:Positive
    val amount: Int,
    @field:Size(max = 3)
    val currency: String,
) {
    constructor(): this(0, "")
}

data class ProductDto(
    var id: String?,
    @field:Size(min = 2, max = 20, message = "the username must have between 2 and 20 caracters")
    val name: String,
    @field:Size(min = 5, max = 100, message = "the description must have between 5 and 100 caracters")
    val description: String?,
    val price: Price,
    var family: FamilleDto
) {
    fun toEntity(): ProductEntity {
        return ProductEntity(this.id, this.name, this.description, this.price, this.family.toEntity())
    }
}