package iut.nantes.project.products.controllers.entities

import iut.nantes.project.products.controllers.dto.Price
import iut.nantes.project.products.controllers.dto.ProductDto
import jakarta.persistence.*

@Entity
@Table(name = "product_table")
class ProductEntity(
    @Id
    @Column(name = "product_id")
    var id: String?,
    @Column(nullable = false)
    var name: String,
    @Column(nullable = true)
    var description: String?,
    @Embedded
    var price: Price,

    @ManyToOne
    @JoinColumn(name = "famille_id", nullable = false)
    var family: FamilleEntity
) {
    constructor() : this(null, "", "", Price(), FamilleEntity())

    fun toDto(): ProductDto {
        return ProductDto(this.id, this.name, this.description, this.price, this.family.toDto())
    }
}