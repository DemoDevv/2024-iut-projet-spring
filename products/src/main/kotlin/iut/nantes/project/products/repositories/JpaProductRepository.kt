package iut.nantes.project.products.repositories

import iut.nantes.project.products.controllers.entities.ProductEntity
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.JpaRepository

@Profile("!dev")
interface JpaProductRepository : ProductRepository, JpaRepository<ProductEntity, String>