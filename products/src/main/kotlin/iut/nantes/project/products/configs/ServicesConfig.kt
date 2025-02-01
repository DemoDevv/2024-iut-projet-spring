package iut.nantes.project.products.configs

import iut.nantes.project.products.repositories.FamilleRepository
import iut.nantes.project.products.repositories.ProductRepository
import iut.nantes.project.products.services.FamilleService
import iut.nantes.project.products.services.ProductService
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties.Web
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class ServicesConfig {
    @Bean
    fun familleService(familleRepository: FamilleRepository,productRepository: ProductRepository, environment: Environment): FamilleService =
        FamilleService(familleRepository,productRepository, environment)

    @Bean
    fun productService(
        productRepository: ProductRepository,
        familleRepository: FamilleRepository,
        environment: Environment,
        webClient: WebClient
    ): ProductService =
        ProductService(productRepository, familleRepository, environment,webClient)
}