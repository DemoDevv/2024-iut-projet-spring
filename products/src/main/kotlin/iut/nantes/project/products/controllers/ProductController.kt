package iut.nantes.project.products.controllers

import iut.nantes.project.products.controllers.dto.ProductDto
import iut.nantes.project.products.exceptions.FamilleNotFoundException
import iut.nantes.project.products.exceptions.InvalidIdFormatException
import iut.nantes.project.products.exceptions.ProductNotDeletableException
import iut.nantes.project.products.exceptions.ProductNotFoundException
import iut.nantes.project.products.services.ProductService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RestController
@RequestMapping("/api/v1/products")
class ProductController(private val productService: ProductService) {
    // POST: create product
    @PostMapping
    fun createProduct(@RequestBody @Valid product: ProductDto): ResponseEntity<Any> {
        return try {
            val createdProduct = productService.createProduct(product)
            ResponseEntity.status(HttpStatus.CREATED).body(createdProduct)
        } catch (e: FamilleNotFoundException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.message)
        }
    }

    // GET: get all products (with optionals filters)
    @GetMapping
    fun getProducts(
        @RequestParam(required = false) familyname: String?,
        @RequestParam(required = false) minprice: Double?,
        @RequestParam(required = false) maxprice: Double?
    ): ResponseEntity<Any> {
        if (minprice != null && minprice <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("the minprice must be >0")
        }
        if (minprice != null && maxprice != null && minprice >= maxprice) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("the minPrice can't be equal or higher than the maxPrice")
        }

        val products = productService.getProducts(familyname, minprice, maxprice)
        return ResponseEntity.ok(products)
    }

    // GET: get product by id
    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: String): ResponseEntity<Any> {
        return try {
            val product = productService.getProductById(id)
            ResponseEntity.ok(product)
        } catch (e: ProductNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
        } catch (e: InvalidIdFormatException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.message)
        }
    }

    // PUT: update product by id
    @PutMapping("/{id}")
    fun updateProduct(
        @PathVariable id: String,
        @RequestBody @Valid productUpdated: ProductDto
    ): ResponseEntity<Any> {
        return try {
            val updatedProduct = productService.updateProduct(id, productUpdated)
            ResponseEntity.ok(updatedProduct)
        } catch (e: FamilleNotFoundException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.message)
        }
    }

    // DELETE:  delete product
    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: String): ResponseEntity<Any> {
        return try {
            productService.deleteProductById(id)
            ResponseEntity.status(HttpStatus.NO_CONTENT).body("deleted succefully") // 204 No Content
        } catch (e: InvalidIdFormatException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.message)
        } catch (e: ProductNotDeletableException) {
            ResponseEntity.status(HttpStatus.CONFLICT).body(e.message)
        }
    }
}