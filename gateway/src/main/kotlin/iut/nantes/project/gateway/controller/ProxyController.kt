package iut.nantes.project.gateway.controller

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
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

        //for POST / PUT request
        @RequestBody(required = false) body: String?
    ): ResponseEntity<*> {
        // extract login from authentified user
        val authentication = SecurityContextHolder.getContext().authentication
        val username = authentication.name

        val endpoint = request.requestURI.substringAfter("/$service", missingDelimiterValue = "")

        // build the target URL depending on service;
        var targetUrl = when (service) {
            "families" -> "$productsServiceUrl/$service$endpoint"
            "products" -> "$productsServiceUrl/$service$endpoint"
            "contacts" -> "$storesServiceUrl/$service$endpoint"
            "stores" -> "$storesServiceUrl/$service$endpoint"
            else -> throw IllegalArgumentException("Invalid service: $service")
        }

        if (params.isNotEmpty()) targetUrl =
            targetUrl.plus("?${params.map { "${it.key}=${it.value[0]}" }.joinToString("&")}")

        // add "X-User" header with the user login.
        val modifiedHeaders = HttpHeaders()
        modifiedHeaders.putAll(headers)
        modifiedHeaders["X-User"] = username

        // Use WebClient to redirect the request
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

                HttpMethod.POST.name() -> webClientBuilder.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build()
                    .post()
                    .uri(targetUrl)
                    .headers { it.addAll(modifiedHeaders) }
                    .bodyValue(body?: "")
                    .retrieve()
                    .toEntity(String::class.java)
                    .block() ?: ResponseEntity.internalServerError().build<Any>()

                HttpMethod.PUT.name() -> webClientBuilder.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build()
                    .put()
                    .uri(targetUrl)
                    .headers { it.addAll(modifiedHeaders) }
                    .bodyValue(body ?: "")
                    .retrieve()
                    .toEntity(String::class.java)
                    .block() ?: ResponseEntity.internalServerError().build<Any>()

                HttpMethod.DELETE.name() -> webClientBuilder
                    .build()

                    //we used ".method" instead of delete() because this one don't have bodyValue() method. We have some Route with a body when it's a DELETE request.
                    .method(HttpMethod.DELETE)
                    .uri(targetUrl)
                    .bodyValue(body?: "")
                    .headers { it.addAll(modifiedHeaders) }

                    .retrieve()
                    .toEntity(String::class.java)
                    .block() ?: ResponseEntity.internalServerError().build<Any>()

                else -> throw UnsupportedOperationException("HTTP not supported: ${request.method}")
            }

        } catch (e: Exception) {
           return  ResponseEntity.badRequest().body("An error has occured, error message: ${e.message}")
        }
    }
}