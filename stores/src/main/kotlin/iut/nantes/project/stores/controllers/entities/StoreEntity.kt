package iut.nantes.project.stores.controllers.entities

import iut.nantes.project.stores.controllers.dto.Product
import iut.nantes.project.stores.controllers.dto.StoreDto
import jakarta.persistence.*

@Entity
@Table(name = "store_table")
class StoreEntity(
    @Id @GeneratedValue @Column(name = "store_id")
    val id: Long?,
    var name: String,
    @OneToOne
    @JoinColumn(name = "contact_id", nullable = false)
    var contact: ContactEntity,
    val products: MutableList<Product> = mutableListOf()
) {
    constructor(): this(0L, "", ContactEntity())

    fun toDto(): StoreDto {
        return StoreDto(this.id!!, this.name, this.contact.toDto(), this.products)
    }
}