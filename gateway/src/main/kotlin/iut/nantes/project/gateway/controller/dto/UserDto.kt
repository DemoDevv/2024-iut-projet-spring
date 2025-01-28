package iut.nantes.project.gateway.controller.dto

import iut.nantes.project.gateway.controller.entity.UserEntity
import jakarta.persistence.*

data class UserDto(
    val login: String,
    val password: String,
    val isAdmin: Boolean
) {
    fun toEntity(): UserEntity {
        return UserEntity(this.login, this.password, this.isAdmin)
    }
}