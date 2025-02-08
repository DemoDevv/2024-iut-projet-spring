package iut.nantes.project.products.services

import iut.nantes.project.products.controllers.dto.FamilleDto
import iut.nantes.project.products.exceptions.FamilleHasLinkedProductsException
import iut.nantes.project.products.exceptions.FamilleNameConflictException
import iut.nantes.project.products.exceptions.FamilleNotFoundException
import iut.nantes.project.products.exceptions.InvalidIdFormatException
import iut.nantes.project.products.repositories.FamilleRepository
import iut.nantes.project.products.repositories.ProductRepository
import org.springframework.core.env.Environment
import org.springframework.dao.DataIntegrityViolationException
import java.lang.IllegalArgumentException
import java.util.UUID

class FamilleService(
    private val familleRepository: FamilleRepository,
    private val productRepository: ProductRepository,
    private val environment: Environment,

    ) {
    fun createFamille(famille: FamilleDto): FamilleDto {
        if (!environment.activeProfiles.contains("test")) famille.id = UUID.randomUUID().toString()
        if(famille.name==null||famille.description==null){
            throw DataIntegrityViolationException("Problems with attributes")
        }
        try {

            val newFamille = familleRepository.save(famille.toEntity())
            return newFamille.toDto()
        } catch (e: DataIntegrityViolationException) {
            throw FamilleNameConflictException("Famille with name ${famille.name} already exist.")
        }
    }

    fun getAllFamilles(): List<FamilleDto> {
        return familleRepository.findAll().map { it.toDto() }
    }

    fun getFamilleById(id: String): FamilleDto {
        try {
            UUID.fromString(id)
        } catch (e: IllegalArgumentException) {
            throw InvalidIdFormatException("Id is not in a UUID format")
        }

        return familleRepository.findById(id).orElseThrow {
            FamilleNotFoundException("Famille with ID $id not found.")
        }.toDto()
    }

    fun updateFamille(id: String, familleUpdate: FamilleDto): FamilleDto {
        val famille = familleRepository.findById(id).orElseThrow { FamilleNotFoundException("Family not found.") }


        if (familleUpdate.name==null||familleUpdate.description==null){
            throw DataIntegrityViolationException("send all the familly body")
        }


        famille.name = familleUpdate.name
        famille.description = familleUpdate.description

        try {
            familleRepository.save(famille)
        } catch (e: DataIntegrityViolationException) {
            throw FamilleNameConflictException("Famille with name ${famille.name} already exist.")
        }

        return famille.toDto()
    }

    fun deleteFamille(id: String) {

         familleRepository.findById(id)
            .orElseThrow { FamilleNotFoundException("Famille with ID $id not found.") }

        // Vérification s'il y a des produits liés à cette famille
        if (productRepository.findAll().any { it.family.id == id }) {
            throw FamilleHasLinkedProductsException("Cannot delete a family with linked products.")
        } else {
            familleRepository.deleteById(id)
        }

    }
}