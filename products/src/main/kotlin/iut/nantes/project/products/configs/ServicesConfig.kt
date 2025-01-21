package iut.nantes.project.products.configs

import iut.nantes.project.products.repositories.FamilleRepository
import iut.nantes.project.products.services.FamilleService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ServicesConfig {
    @Bean
    fun familleService(familleRepository: FamilleRepository): FamilleService = FamilleService(familleRepository)
}