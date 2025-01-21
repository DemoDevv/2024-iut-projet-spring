package iut.nantes.project.products.controllers

import iut.nantes.project.products.controllers.dto.FamilleDto
import iut.nantes.project.products.exceptions.*
import iut.nantes.project.products.services.FamilleService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RestController
@RequestMapping("/api/v1/families")
class FamilleController(private val familleService: FamilleService) {

    // POST: Créer une famille
    @PostMapping
    fun createFamily(@RequestBody @Valid famille: FamilleDto): ResponseEntity<Any> {
        return try {
            val createdFamille = familleService.createFamille(famille)
            ResponseEntity.status(HttpStatus.CREATED).body(createdFamille)
        } catch (e: FamilleNameConflictException) {
            ResponseEntity.status(HttpStatus.CONFLICT).body(mapOf("error" to "Family name already exists"))
        }
    }

    // GET: Récupérer toutes les familles
    @GetMapping
    fun getAllFamilies(): ResponseEntity<List<FamilleDto>> {
        val familles = familleService.getAllFamilles()
        return ResponseEntity.ok(familles)
    }

    // GET: Récupérer une famille par ID
    @GetMapping("/{id}")
    fun getFamilyById(@PathVariable id: String): ResponseEntity<Any> {
        return try {
            val famille = familleService.getFamilleById(id)
            ResponseEntity.ok(famille)
        } catch (e: FamilleNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to "Family not found"))
        } catch (e: InvalidIdFormatException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to "Invalid ID format"))
        }
    }

    // PUT: Mettre à jour une famille
    @PutMapping("/{id}")
    fun updateFamily(@PathVariable id: String, @RequestBody @Valid famille: FamilleDto): ResponseEntity<Any> {
        return try {
            val updatedFamille = familleService.updateFamille(id, famille)
            ResponseEntity.ok(updatedFamille)
        } catch (e: FamilleNameConflictException) {
            ResponseEntity.status(HttpStatus.CONFLICT).body(mapOf("error" to "Family name conflict"))
        }
    }

    // DELETE: Supprimer une famille
    @DeleteMapping("/{id}")
    fun deleteFamille(@PathVariable id: String): ResponseEntity<Any> {
        return try {
            familleService.deleteFamille(id)
            ResponseEntity.noContent().build()
        } catch (e: FamilleNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to "Family not found"))
        } catch (e: FamilleHasLinkedProductsException) {
            ResponseEntity.status(HttpStatus.CONFLICT).body(mapOf("error" to "Family has linked products"))
        }
    }
}