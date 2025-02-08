package iut.nantes.project.products.services

import iut.nantes.project.products.controllers.dto.ProductDto
import iut.nantes.project.products.exceptions.FamilleNotFoundException
import iut.nantes.project.products.exceptions.InvalidIdFormatException
import iut.nantes.project.products.exceptions.ProductNotDeletableException
import iut.nantes.project.products.exceptions.ProductNotFoundException
import iut.nantes.project.products.repositories.FamilleRepository
import iut.nantes.project.products.repositories.ProductRepository
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.lang.IllegalArgumentException
import java.util.*

class ProductService(
    private val productRepository: ProductRepository,
    private val familleRepository: FamilleRepository,
    private val environment: Environment,
    private val webClient: WebClient
) {
    fun createProduct(product: ProductDto): ProductDto {
        if (!environment.activeProfiles.contains("test")) product.id = UUID.randomUUID().toString()

        if (product.family.id == null) {
            throw FamilleNotFoundException("The id hasn't been filled ")
        } else {
            val famille = familleRepository.findById(product.family.id!!)
                .orElseThrow { FamilleNotFoundException("This id doesn't exist in Families's database") }
            product.family = famille.toDto()

        }

        return productRepository.save(product.toEntity()).toDto()
    }

    fun getProducts(familyName: String?, minPrice: Double?, maxPrice: Double?): List<ProductDto> {
        var products = productRepository.findAll()

        if (familyName != null) products = products.filter { it.family.name == familyName }

        if (minPrice != null) products = products.filter { it.price.amount >= minPrice }

        if (maxPrice != null) products = products.filter { it.price.amount <= maxPrice }

        return products.map { it.toDto() }
    }

    fun getProductById(id: String): ProductDto {
        try {
            UUID.fromString(id)
        } catch (e: IllegalArgumentException) {
            throw InvalidIdFormatException("This ID is not in a UUID format")
        }

        return productRepository.findById(id).orElseThrow {
            ProductNotFoundException("Product with ID $id not found.")
        }.toDto()
    }

    fun updateProduct(id: String, productUpdate: ProductDto): ProductDto {
        val isFamille =
            familleRepository.findById(productUpdate.family.id!!).isPresent

        if (!isFamille) throw FamilleNotFoundException("This family doesn't exist")

        val product =
            productRepository.findById(id).orElseThrow { ProductNotFoundException("This product doesn't exist") }

        product.name = productUpdate.name
        product.description = productUpdate.description
        product.price = productUpdate.price
        product.family = productUpdate.family.toEntity()

        productRepository.save(product)

        return product.toDto()
    }

    fun deleteProductById(productId: String) {
        try {
            UUID.fromString(productId)
        } catch (e: IllegalArgumentException) {
            throw InvalidIdFormatException("Id is not in a UUID format")
        }

        webClient.delete().uri("/api/v1/stores/products/{productId}", productId)
            .header("X-User", "RandomUser")
            .retrieve()
            .onStatus({ status -> status == HttpStatus.CONFLICT }) { _ ->
                Mono.error<Throwable?>(ProductNotDeletableException("This product still present in some stores with a quantity greater than 0."))


            }.bodyToMono(Boolean::class.java).block()

        //if the code is executed here, so there are no conflicts,we can delete the product.
        try {
            productRepository.deleteById(productId)

        } catch (e: Exception) {
            throw e
        }
    }
}