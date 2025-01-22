package iut.nantes.project.products.controllers.dto

import iut.nantes.project.products.controllers.entities.ProductEntity
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class Price(
    @field:Positive
    val amout: Int,
    @field:Size(max = 3)
    val currency: String,
) {
    constructor(): this(0, "")
}

data class ProductDto(
    var id: String?,
    @field:Size(min = 2, max = 20, message = "Le nom d'utilisateur doit contenir entre 2 et 20 caractères")
    val name: String,
    @field:Size(min = 5, max = 100, message = "La description doit contenir entre 5 et 100 caractères")
    val description: String?,
    val price: Price,
    val family: FamilleDto
) {
    fun toEntity(): ProductEntity {
        return ProductEntity(this.id, this.name, this.description, this.price, this.family.toEntity())
    }
}