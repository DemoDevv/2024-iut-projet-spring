package iut.nantes.project.gateway.config

import com.sun.net.httpserver.HttpsExchange
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.config.annotation.web.invoke

@Configuration
@EnableWebSecurity
class GatewayConfig {
    @Bean
    fun filterChain(http:HttpSecurity): SecurityFilterChain{
        http {
            csrf { disable() }
            authorizeHttpRequests {
                authorize("/api/v1/test", permitAll)
                authorize(anyRequest, authenticated)
            }
            httpBasic { }
            formLogin { }
        }
        return http.build()
    }
}