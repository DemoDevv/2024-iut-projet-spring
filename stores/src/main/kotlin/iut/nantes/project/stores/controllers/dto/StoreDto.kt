package iut.nantes.project.stores.controllers.dto

import iut.nantes.project.stores.controllers.entities.StoreEntity
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.validation.constraints.Size

@Entity
data class Product(
    @Id
    val id: String,
    val name: String,
    val quantity: Int
) {
    constructor(): this("", "", 0)
}

// todo: je comprends pas la phrase "Le nom du produit doit être cohérent avec le contenu du service product." du sujet
data class StoreDto(
    val id: Long,
    @field:Size(min = 3, max = 30)
    val name: String,
    val contact: ContactDto,
    val products: MutableList<Product>
) {
    fun toEntity(): StoreEntity {
        return StoreEntity(this.id, this.name, this.contact.toEntity(), this.products)
    }
}