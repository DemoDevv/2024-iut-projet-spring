package iut.nantes.project.gateway.controller

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.toEntity

@RestController
@RequestMapping("/api/v1/")
class ProxyController(private val webClientBuilder: WebClient.Builder) {
    private val productsServiceUrl = "http://localhost:8081/api/v1"
    private val storesServiceUrl = "http://localhost:8082/api/v1"

    @RequestMapping(
        "/{service}/**",
        method = [RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE]
    )
    fun proxyRequest(
        @PathVariable service: String,
        @RequestHeader headers: HttpHeaders,
        @RequestParam params: MultiValueMap<String, String>,
        request: HttpServletRequest,

        //pour les requetes POST et PUT
        @RequestBody(required = false) body: String?
    ): ResponseEntity<*> {
        // Extraire le login de l'utilisateur authentifié
        val authentication = SecurityContextHolder.getContext().authentication
        val username = authentication.name

        val endpoint = request.requestURI.substringAfter("/$service", missingDelimiterValue = "")

        // Construire l'URL cible en fonction du service
        var targetUrl = when (service) {
            "families" -> "$productsServiceUrl/$service$endpoint"
            "products" -> "$productsServiceUrl/$service$endpoint"
            "contacts" -> "$storesServiceUrl/$service$endpoint"
            "stores" -> "$storesServiceUrl/$service$endpoint"
            else -> throw IllegalArgumentException("Invalid service: $service")
        }

        if (params.isNotEmpty()) targetUrl =
            targetUrl.plus("?${params.map { "${it.key}=${it.value[0]}" }.joinToString("&")}")

        // Ajouter le header X-User avec le login de l'utilisateur
        val modifiedHeaders = HttpHeaders()
        modifiedHeaders.putAll(headers)
        modifiedHeaders["X-User"] = username

        // Utiliser WebClient pour rediriger la requête
        try {
            return when (request.method) {

                HttpMethod.GET.name() -> webClientBuilder
                    .build()
                    .get()
                    .uri(targetUrl)
                    .headers { it.addAll(modifiedHeaders) }
                    .retrieve()
                    .toEntity(String::class.java)
                    .block() ?: ResponseEntity.internalServerError().build<Any>()

                HttpMethod.POST.name() -> webClientBuilder.defaultHeader("Content-Type", "application/json")
                    .build()
                    .post()
                    .uri(targetUrl)
                    .headers { it.addAll(modifiedHeaders) }
                    .bodyValue(body!!)
                    .retrieve()
                    .toEntity(String::class.java)
                    .block() ?: ResponseEntity.internalServerError().build<Any>()

                HttpMethod.PUT.name() -> webClientBuilder.defaultHeader("Content-Type", "application/json")
                    .build()
                    .put()
                    .uri(targetUrl)
                    .headers { it.addAll(modifiedHeaders) }
                    .bodyValue(body!!)
                    .retrieve()
                    .toEntity(String::class.java)
                    .block() ?: ResponseEntity.internalServerError().build<Any>()

                HttpMethod.DELETE.name() -> webClientBuilder
                    .build()
                    .delete()
                    .uri(targetUrl)
                    .headers { it.addAll(modifiedHeaders) }
                    .retrieve()
                    .toEntity(String::class.java)
                    .block() ?: ResponseEntity.internalServerError().build<Any>()

                else -> throw UnsupportedOperationException("HTTP not supported: ${request.method}")
            }

        } catch (e: Exception) {
            println(e.message)
        }
        return ResponseEntity.badRequest().body("An error has occured. You can't read theses lines if you run this server without modifications.")
    }
}