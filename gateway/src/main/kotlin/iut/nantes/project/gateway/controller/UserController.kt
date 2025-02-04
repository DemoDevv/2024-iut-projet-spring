package iut.nantes.project.gateway.controller

import iut.nantes.project.gateway.controller.dto.UserDto
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val userDetailsManager: UserDetailsManager,
    private val passwordEncoder: PasswordEncoder
) {
    @PostMapping
    fun createUser(@RequestBody request: UserDto): String {
        if (userDetailsManager.userExists(request.login)) {
            throw IllegalArgumentException("User already exists")
        }

        val user = User.withUsername(request.login)
            .password(passwordEncoder.encode(request.password))
            .roles(if (request.isAdmin) "ADMIN" else "USER")
            .build()

        userDetailsManager.createUser(user)

        return "User created successfully"
    }
}