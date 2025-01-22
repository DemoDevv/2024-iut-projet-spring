package iut.nantes.project.products.repositories

import iut.nantes.project.products.controllers.entities.ProductEntity
import java.util.*

interface ProductRepository {
    fun save(entity: ProductEntity): ProductEntity
    fun findById(id: String): Optional<ProductEntity>
    fun findAll(): List<ProductEntity>
    fun deleteById(id: String)
}