package iut.nantes.project.products.repositories

import iut.nantes.project.products.controllers.entities.FamilleEntity
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.JpaRepository

@Profile("!dev")
interface JpaFamilleRepository: FamilleRepository, JpaRepository<FamilleEntity, String>