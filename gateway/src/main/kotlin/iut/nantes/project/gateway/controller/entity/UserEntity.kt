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
@Table(name="user_table")
class UserEntity (
    @Id
    @Column(nullable = false)
    var login: String,
    @Column(nullable = false)
    var password: String,
    @Column(nullable = false)
    var isAdmin: Boolean
): UserDetails{



    constructor(): this("","",false)

    fun toDto(): UserDto{
        return UserDto(this.login,this.password,this.isAdmin)
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        var mlist= mutableListOf<GrantedAuthority>(SimpleGrantedAuthority("ROLE_USER"))

        if (isAdmin) {
            mlist.add(SimpleGrantedAuthority("ROLE_ADMIN"))
        }
        return mlist
    }

    override fun getPassword(): String {
        return this.password
    }

    override fun getUsername(): String {
        return this.login
    }

    override fun isEnabled(): Boolean {
        return super.isEnabled()
    }

    override fun isAccountNonExpired(): Boolean {
        return super.isAccountNonExpired()
    }

    override fun isAccountNonLocked(): Boolean {
        return super.isAccountNonLocked()
    }

    override fun isCredentialsNonExpired(): Boolean {
        return super.isCredentialsNonExpired()
    }
}
