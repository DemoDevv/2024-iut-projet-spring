package iut.nantes.project.gateway.controller

import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.function.client.WebClient

@RestController
@RequestMapping("/api/v1/")
class ProxyController(private val webClientBuilder: WebClient.Builder) {
    private val productsServiceUrl = "http://localhost:8081/api/v1/products"
    private val storesServiceUrl = "http://localhost:8082/api/v1/stores"

    @GetMapping("/{service}/{**endpoint}")
    fun proxyRequest(
        @PathVariable service: String,
        @PathVariable endpoint: String,
        @RequestHeader headers: HttpHeaders,
        @RequestParam params: MultiValueMap<String, String>
    ): ResponseEntity<*> {
        val targetUrl = when (service) {
            "products" -> "$productsServiceUrl/$endpoint"
            "stores" -> "$storesServiceUrl/$endpoint"
            else -> throw IllegalArgumentException("Invalid service: $service")
        }

        // Ajouter le header X-User
        val modifiedHeaders = HttpHeaders()
        modifiedHeaders.putAll(headers)
        val user = headers.getFirst("Authorization")?.let { login(it) } ?: "Anonymous"
        modifiedHeaders["X-User"] = user

        // Utiliser WebClient pour rediriger la requête
        val webClient = webClientBuilder.build()
        return webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path(targetUrl)
                    .queryParams(params)
                    .build()
            }
            .headers { it.addAll(modifiedHeaders) }
            .retrieve()
            .toEntity(String::class.java)
            .block() ?: ResponseEntity.internalServerError().build<Any>()
    }

    private fun login(token: String): String {
        return "ADMIN/ADMIN"
    }
}