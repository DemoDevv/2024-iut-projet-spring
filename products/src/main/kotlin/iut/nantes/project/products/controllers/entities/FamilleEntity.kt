package iut.nantes.project.products.controllers.entities

import iut.nantes.project.products.controllers.dto.FamilleDto
import jakarta.persistence.*

@Entity
@Table(name = "famille_table", uniqueConstraints = [UniqueConstraint(columnNames = ["name"])])
class FamilleEntity(
    @Id
    @Column(name = "famille_id")
    var id: String?,
    @Column(nullable = false)
    var name: String,
    @Column(nullable = false)
    var description: String,

    @OneToMany(mappedBy = "family", cascade = [CascadeType.ALL], orphanRemoval = true)
    val produits: List<ProductEntity> = emptyList()
) {
    constructor() : this(null, "", "")

    fun toDto(): FamilleDto {
        return FamilleDto(this.id, this.name, this.description)
    }
}