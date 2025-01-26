package iut.nantes.project.products.configs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {
    @Bean
    fun webClient(): WebClient {
        return WebClient.builder()
            .baseUrl("http://localhost:8082") // utilisé pour les appels vers d'autres api donc changer par l'url de l'api
            .build()
    }
}