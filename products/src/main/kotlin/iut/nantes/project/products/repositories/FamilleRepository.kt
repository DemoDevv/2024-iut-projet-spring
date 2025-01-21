package iut.nantes.project.products.repositories

import iut.nantes.project.products.controllers.entities.FamilleEntity
import java.util.*

interface FamilleRepository {
    fun save(entity: FamilleEntity): FamilleEntity
    fun findById(id: String): Optional<FamilleEntity>
    fun findAll(): List<FamilleEntity>
    fun deleteById(id: String)
}