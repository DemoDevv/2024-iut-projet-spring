package iut.nantes.project.stores.controllers

import iut.nantes.project.stores.controllers.dto.Product
import iut.nantes.project.stores.controllers.dto.StoreDto
import iut.nantes.project.stores.exceptions.ConflictException
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

    // GET /api/v1/stores :  get all stores sorted by (a→z)
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllStores(): List<StoreDto> {
        return storeService.getAllStores()
    }

    // GET /api/v1/stores/{id} : get store by id
    @GetMapping("/{id}")
    fun getStoreById(@PathVariable id: String): StoreDto {
        return storeService.getStoreById(id)
    }

    // PUT /api/v1/stores/{id} : update store
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateStore(
        @PathVariable id: String,
        @Valid @RequestBody storeUpdate: StoreDto
    ): StoreDto {
        return storeService.updateStore(id, storeUpdate)
    }

    // DELETE /api/v1/stores/{id} : delete store
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
    //this function is usefully in order to remove a product of a store from the product server
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