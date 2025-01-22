package iut.nantes.project.products.configs

import iut.nantes.project.products.repositories.FamilleRepository
import iut.nantes.project.products.repositories.HashMapFamilleRepository
import iut.nantes.project.products.repositories.HashMapProductRepository
import iut.nantes.project.products.repositories.ProductRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("dev")
class DevConfig {
    @Bean
    fun familleRepository(): FamilleRepository = HashMapFamilleRepository()

    @Bean
    fun productRepository(): ProductRepository = HashMapProductRepository()
}