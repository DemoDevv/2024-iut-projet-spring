package iut.nantes.project.products.services

import iut.nantes.project.products.controllers.dto.ProductDto
import iut.nantes.project.products.exceptions.FamilleNotFoundException
import iut.nantes.project.products.exceptions.InvalidIdFormatException
import iut.nantes.project.products.exceptions.ProductNotFoundException
import iut.nantes.project.products.repositories.FamilleRepository
import iut.nantes.project.products.repositories.ProductRepository
import org.springframework.core.env.Environment
import java.lang.IllegalArgumentException
import java.util.*

class ProductService(
    private val productRepository: ProductRepository,
    private val familleRepository: FamilleRepository,
    private val environment: Environment
) {
    fun createProduct(product: ProductDto): ProductDto {
        if (!environment.activeProfiles.contains("test")) product.id = UUID.randomUUID().toString()

        val isFamille =
            familleRepository.findById(product.family.id!!).isPresent // todo: pour enlever le possible nul peut être faire un dto différent de celui donnée dans la request

        if (!isFamille) throw FamilleNotFoundException(null) // todo: mettre le message de l'erreur

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

    fun deleteProductById(id: String) {
        // todo: je pense que je dois d'abord implémenter les stores
    }
}