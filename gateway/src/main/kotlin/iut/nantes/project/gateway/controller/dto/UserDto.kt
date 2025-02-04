package iut.nantes.project.gateway.controller.dto

data class UserDto(
    val login: String,
    val password: String,
    val isAdmin: Boolean
)