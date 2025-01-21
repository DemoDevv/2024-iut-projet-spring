package iut.nantes.project.products.controllers.entities

import iut.nantes.project.products.controllers.dto.FamilleDto
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import jakarta.validation.constraints.Size

@Entity
@Table(name = "famille_table", uniqueConstraints = [UniqueConstraint(columnNames = ["name"])])
class FamilleEntity(
    @Id
    @Column(name = "famille_id")
    val id: String?,
    //@field:Size(min = 3, max = 30, message = "Le nom d'utilisateur doit contenir entre 3 et 30 caractères")
    val name: String,
    //@field:Size(min = 5, max = 100, message = "La description doit contenir entre 5 et 100 caractères")
    val description: String
) {
    constructor() : this(null, "", "")

    fun toDto(): FamilleDto {
        return FamilleDto(this.id!!, this.name, this.description) // FIXME: j'ai mis un !! sur l'id
    }
}