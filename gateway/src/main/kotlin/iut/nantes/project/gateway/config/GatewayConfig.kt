package iut.nantes.project.gateway.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.provisioning.JdbcUserDetailsManager
import org.springframework.security.provisioning.UserDetailsManager
import javax.sql.DataSource


@Configuration
@EnableWebSecurity
class GatewayConfig(private val dataSource: DataSource) {

    @Bean
    @ConditionalOnProperty(name = ["gateway.security"], havingValue = "inmemory", matchIfMissing = false)
    fun inMemoryUserDetailService(): UserDetailsManager {
        return InMemoryUserDetailsManager(adminUser())
    }

    @Bean
    @ConditionalOnProperty(name = ["gateway.security"], havingValue = "database", matchIfMissing = true)
    fun jdbcUserDetailsService(): UserDetailsManager {
        return JdbcUserDetailsManager(dataSource).apply {
            if (!userExists("ADMIN")) {
                createUser(adminUser())
            }
        }
    }

    private fun adminUser() = User
        .withUsername("ADMIN")
        .password(passwordEncoder().encode("ADMIN"))
        .roles("ADMIN")
        .build()

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun filterChain(http:HttpSecurity): SecurityFilterChain{
        http {
            csrf { disable() }
            cors { disable() }
            authorizeHttpRequests {
                authorize(HttpMethod.POST,"/api/v1/user", permitAll)

                authorize(HttpMethod.POST,"/api/v1/stores/{storeId}/products/{productId}/add", authenticated)
                authorize(HttpMethod.POST,"/api/v1/stores/{storeId}/products/{productId}/remove", authenticated)
                authorize(HttpMethod.DELETE,"/api/v1/stores/{storeId}/products", authenticated)

                authorize(anyRequest, hasRole("ADMIN"))
            }
            httpBasic { }
            formLogin { }
        }
        return http.build()
    }
}