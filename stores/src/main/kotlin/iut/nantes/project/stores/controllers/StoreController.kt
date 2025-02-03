package iut.nantes.project.stores.controllers

import iut.nantes.project.stores.controllers.dto.Product
import iut.nantes.project.stores.controllers.dto.StoreDto
import iut.nantes.project.stores.exceptions.ConflictException
import iut.nantes.project.stores.exceptions.DuplicateElementsException
import iut.nantes.project.stores.exceptions.InvalidRequestParameters
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

    // POST /api/v1/stores/{storeId}/products/{productId}/add?quantity=2
    @PostMapping("/{storeId}/products/{productId}/add")
    fun addProductToStock(
        @PathVariable storeId: String,
        @PathVariable productId: String,
        @RequestParam(required = false, defaultValue = "1") quantity: Int
    ): Product {
        return storeService.addProductToStore(storeId, productId, quantity)
    }

    // POST /api/v1/stores/{storeId}/products/{productId}/remove?quantity=2
    @PostMapping("/{storeId}/products/{productId}/remove")
    fun removeProductFromStock(
        @PathVariable storeId: String,
        @PathVariable productId: String,
        @RequestParam(required = false, defaultValue = "1") quantity: Int
    ): Product {

        return storeService.removeProductFromStock(storeId, productId, quantity)
    }

    // DELETE /api/v1/stores/{storeId}/products
    @DeleteMapping("/{storeId}/products")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removeProductsFromStore(
        @PathVariable storeId: String,
        @RequestBody productsToRemove: List<String>
    ) {

        storeService.removeProductsFromStore(storeId, productsToRemove)
    }

    // DELETE /api/v1/stores/products/{productID}
    //Fonction pour pouvoir remove un produit du store, depuis le serveur qui gère les produits.
    @DeleteMapping("/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removeProduct(
        @PathVariable productId: String
    ): Boolean {
        val result = storeService.productExistInStore(productId)
        if (result) {
            throw ConflictException()
        } else {
            storeService.removeProductsFromStoreIfZeroQuantity(productId)
            return true
        }
    }
}