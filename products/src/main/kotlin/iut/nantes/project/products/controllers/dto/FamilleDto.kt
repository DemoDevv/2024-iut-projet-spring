package iut.nantes.project.products.controllers.dto

import iut.nantes.project.products.controllers.entities.FamilleEntity
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class FamilleDto(
    @NotNull
    var id: String?,
    @field:Size(min = 3, max = 30, message = "The name must have between 3 and 30 caracters")
    val name: String,
    @field:Size(min = 5, max = 100, message = "The description must have between 5 and 100 caracters")
    val description: String
) {
    fun toEntity(): FamilleEntity {
        return FamilleEntity(this.id, this.name, this.description)
    }
}