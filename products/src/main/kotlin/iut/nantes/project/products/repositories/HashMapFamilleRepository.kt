package iut.nantes.project.products.repositories

import iut.nantes.project.products.controllers.entities.FamilleEntity
import java.util.*

class HashMapFamilleRepository: FamilleRepository {
    private val data = mutableMapOf<String, FamilleEntity>()

    override fun save(entity: FamilleEntity): FamilleEntity {
        data[entity.id!!] = entity
        return entity
    }

    override fun findById(id: String): Optional<FamilleEntity> {
        return Optional.ofNullable(data[id])
    }

    override fun findAll(): List<FamilleEntity> = data.values.toList()

    override fun deleteById(id: String) {
        data.remove(id)
    }
}