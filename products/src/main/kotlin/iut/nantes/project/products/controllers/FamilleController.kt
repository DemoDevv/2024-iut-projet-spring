package iut.nantes.project.products.controllers

import iut.nantes.project.products.controllers.dto.FamilleDto
import iut.nantes.project.products.exceptions.*
import iut.nantes.project.products.services.FamilleService
import jakarta.validation.Valid
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RestController
@RequestMapping("/api/v1/families")
class FamilleController(private val familleService: FamilleService) {

    // POST: Create family
    @PostMapping
    fun createFamily(@RequestBody @Valid famille: FamilleDto): ResponseEntity<Any> {
        return try {
            val createdFamille = familleService.createFamille(famille)
            ResponseEntity.status(HttpStatus.CREATED).body(createdFamille)
        } catch (e: FamilleNameConflictException) {
            ResponseEntity.status(HttpStatus.CONFLICT).body(mapOf("error" to "Family name already exists"))
        }catch(e:DataIntegrityViolationException){
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("bad request: attributs are not well written")
        }
    }

    // GET: get all families
    @GetMapping
    fun getAllFamilies(): ResponseEntity<List<FamilleDto>> {
        val familles = familleService.getAllFamilles()
        return ResponseEntity.ok(familles)
    }

    // GET: get family by id
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

    // PUT: update family
    @PutMapping("/{id}")
    fun updateFamily(@PathVariable id: String, @RequestBody @Valid famille: FamilleDto): ResponseEntity<Any> {
        return try {
            val updatedFamille = familleService.updateFamille(id, famille)
            ResponseEntity.ok(updatedFamille)
        } catch (e: FamilleNameConflictException) {
            ResponseEntity.status(HttpStatus.CONFLICT).body(mapOf("error" to "Family name conflict"))
        } catch (e:FamilleNotFoundException){
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to "Family not found"))
        }catch (e:DataIntegrityViolationException){
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("please fill all attributes")
        }
    }

    // DELETE: delete family
    @DeleteMapping("/{id}")
    fun deleteFamille(@PathVariable id: String): ResponseEntity<Any> {
        return try {
            familleService.deleteFamille(id)
            ResponseEntity.status(HttpStatus.NO_CONTENT).body("family has been deleted successfully")
        } catch (e: FamilleNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to "Family not found"))
        } catch (e: FamilleHasLinkedProductsException) {
            ResponseEntity.status(HttpStatus.CONFLICT).body(mapOf("error" to "Family has linked products"))
        }
    }
}