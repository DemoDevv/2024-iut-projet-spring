package iut.nantes.project.stores.repositories

import iut.nantes.project.stores.controllers.entities.StoreEntity
import org.springframework.data.jpa.repository.JpaRepository

interface StoreRepository : JpaRepository<StoreEntity, Long> {
    fun existsByContactId(contactId: Long): Boolean
}