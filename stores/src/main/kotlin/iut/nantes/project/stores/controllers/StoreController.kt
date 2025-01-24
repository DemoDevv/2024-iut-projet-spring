package iut.nantes.project.stores.controllers

import iut.nantes.project.stores.controllers.dto.StoreDto
import iut.nantes.project.stores.services.StoreService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/stores")
@Validated
class StoreController(private val storeService: StoreService) {
    // POST /api/v1/stores : Création d'un store
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createStore(@Valid @RequestBody storeDto: StoreDto): StoreDto {
        return storeService.createStore(storeDto)
    }

    // GET /api/v1/stores :  Récupérer tous les magasins triés par nom (a→z)
    @GetMapping
    fun getAllStores(): List<StoreDto> {
        return storeService.getAllStores()
    }

    // GET /api/v1/stores/{id} : Récupérer un magasin par son ID
    @GetMapping("/{id}")
    fun getStoreById(@PathVariable id: String): StoreDto {
        return storeService.getStoreById(id)
    }

    // PUT /api/v1/stores/{id} : Mettre à jour un magasin
    @PutMapping("/{id}")
    fun updateStore(
        @PathVariable id: String,
        @Valid @RequestBody storeUpdate: StoreDto
    ): StoreDto {
        return storeService.updateStore(id, storeUpdate)
    }

    // DELETE /api/v1/stores/{id} : Supprimer un magasin
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteStore(@PathVariable id: String) {
        storeService.deleteStore(id)
    }
}