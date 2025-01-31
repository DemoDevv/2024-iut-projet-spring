package iut.nantes.project.gateway.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.provisioning.UserDetailsManager

@Configuration
@EnableWebSecurity
class GatewayConfig {
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun userDetailsService(): UserDetailsManager {
        // Créer un utilisateur ADMIN par défaut
        val adminUser = User.withUsername("ADMIN")
            .password(passwordEncoder().encode("ADMIN"))
            .roles("ADMIN")
            .build()
        return InMemoryUserDetailsManager(adminUser)
    }

    @Bean
    fun filterChain(http:HttpSecurity): SecurityFilterChain{
        http {
            csrf { disable() }
            cors { disable() }
            authorizeHttpRequests {
                authorize("/api/v1/user", permitAll)

                authorize("/api/v1/stores/{storeId}/products/{productId}/add", authenticated)
                authorize("/api/v1/stores/{storeId}/products/{productId}/remove", authenticated)
                authorize("/api/v1/stores/{storeId}/products", authenticated)

                authorize(anyRequest, hasRole("ADMIN"))
            }
            httpBasic { }
            formLogin { }
        }
        return http.build()
    }
}