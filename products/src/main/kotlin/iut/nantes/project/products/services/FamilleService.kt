package iut.nantes.project.products.services

import iut.nantes.project.products.controllers.dto.FamilleDto
import iut.nantes.project.products.repositories.FamilleRepository

class FamilleService(private val familleRepository: FamilleRepository) {
    fun getFamilleData(): List<FamilleDto> {
        return familleRepository.findAll().map { it.toDto() }
    }
}