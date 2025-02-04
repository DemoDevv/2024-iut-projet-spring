package iut.nantes.project.gateway.controller.entity

import iut.nantes.project.gateway.controller.dto.UserDto
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties.Simple
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "user_table")
class UserEntity(
    @Id
    @Column(nullable = false)
    val login: String,
    @Column(nullable = false)
    private val password: String,
    @Column(nullable = false)
    var isAdmin: Boolean
) : UserDetails {
    constructor() : this("", "", false)

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val authorities = mutableListOf<GrantedAuthority>()

        authorities.add(SimpleGrantedAuthority("USER")) // Tous les utilisateurs ont le rôle USER

        if (isAdmin) {
            authorities.add(SimpleGrantedAuthority("ADMIN"))
        }

        return authorities
    }

    override fun getPassword(): String {
        return this.password
    }

    override fun getUsername(): String {
        return this.login
    }
}
