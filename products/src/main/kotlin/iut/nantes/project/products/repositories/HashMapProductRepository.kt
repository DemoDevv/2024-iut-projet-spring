package iut.nantes.project.products.repositories

import iut.nantes.project.products.controllers.entities.ProductEntity
import java.util.*

class HashMapProductRepository : ProductRepository {
    private val data = mutableMapOf<String, ProductEntity>()

    override fun save(entity: ProductEntity): ProductEntity {
        data[entity.id!!] = entity
        return entity
    }

    override fun findById(id: String): Optional<ProductEntity> {
        return Optional.ofNullable(data[id])
    }

    override fun findAll(): List<ProductEntity> = data.values.toList()

    override fun deleteById(id: String) {
        data.remove(id)
    }
}