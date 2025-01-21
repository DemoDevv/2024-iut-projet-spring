package iut.nantes.project.products.controllers.entities

import iut.nantes.project.products.controllers.dto.FamilleDto
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(name = "famille_table", uniqueConstraints = [UniqueConstraint(columnNames = ["name"])])
class FamilleEntity(
    @Id
    @Column(name = "famille_id")
    val id: String?,
    @Column(nullable = false)
    val name: String,
    @Column(nullable = false)
    val description: String
) {
    constructor() : this(null, "", "")

    fun toDto(): FamilleDto {
        return FamilleDto(this.id!!, this.name, this.description) // FIXME: j'ai mis un !! sur l'id
    }
}