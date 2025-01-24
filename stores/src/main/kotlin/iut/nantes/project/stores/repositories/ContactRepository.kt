package iut.nantes.project.stores.repositories

import iut.nantes.project.stores.controllers.entities.ContactEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ContactRepository: JpaRepository<ContactEntity, Long> {
}