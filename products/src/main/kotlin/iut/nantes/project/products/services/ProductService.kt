package iut.nantes.project.products.services

import iut.nantes.project.products.configs.WebClientConfig
import iut.nantes.project.products.controllers.dto.ProductDto
import iut.nantes.project.products.exceptions.FamilleNotFoundException
import iut.nantes.project.products.exceptions.InvalidIdFormatException
import iut.nantes.project.products.exceptions.ProductNotDeletableException
import iut.nantes.project.products.exceptions.ProductNotFoundException
import iut.nantes.project.products.repositories.FamilleRepository
import iut.nantes.project.products.repositories.ProductRepository
import org.apache.catalina.Store
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties.Web
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

        val famille = familleRepository.findById(product.family.id!!).orElseThrow { FamilleNotFoundException("") }
        product.family = famille.toDto()

        return productRepository.save(product.toEntity()).toDto()
    }

    fun getProducts(familyName: String?, minPrice: Double?, maxPrice: Double?): List<ProductDto> {
        var products = productRepository.findAll()

        if (familyName != null) products = products.filter { it.family.name == familyName }

        if (minPrice != null) products = products.filter { it.price.amout >= minPrice }

        if (maxPrice != null) products = products.filter { it.price.amout <= maxPrice }

        return products.map { it.toDto() }
    }

    fun getProductById(id: String): ProductDto {
        try {
            UUID.fromString(id)
        } catch (e: IllegalArgumentException) {
            throw InvalidIdFormatException("Id is not in a UUID format")
        }

        return productRepository.findById(id).orElseThrow {
            ProductNotFoundException("Product with ID $id not found.")
        }.toDto()
    }

    fun updateProduct(id: String, productUpdate: ProductDto): ProductDto {
        val isFamille =
            familleRepository.findById(productUpdate.family.id!!).isPresent

        if (!isFamille) throw FamilleNotFoundException(null)

        val product = productRepository.findById(id).orElseThrow { ProductNotFoundException(null) }

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
        webClient.get().uri("/api/v1/stores/products/{productId}", productId).retrieve()
            .onStatus({ status -> status == HttpStatus.CONFLICT }) { _ ->
              throw ProductNotDeletableException("Le produit est encore présent dans des stores avec une q>0")
            }
        //Si le code est exécuté ici, c'est qu'il n'a pas de conflits, on peut donc supprimer le produit.
            productRepository.deleteById(productId)
    }
}