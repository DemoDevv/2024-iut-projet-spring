package iut.nantes.project.products.controllers.dto

import iut.nantes.project.products.controllers.entities.FamilleEntity
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class FamilleDto(
    @NotNull
    var id: String?,
    @field:Size(min = 3, max = 30, message = "Le nom doit contenir entre 3 et 30 caractères")
    val name: String,
    @field:Size(min = 5, max = 100, message = "La description doit contenir entre 5 et 100 caractères")
    val description: String
) {
    fun toEntity(): FamilleEntity {
        return FamilleEntity(this.id, this.name, this.description)
    }
}