package iut.nantes.project.gateway

import com.fasterxml.jackson.databind.ObjectMapper
import iut.nantes.project.gateway.controller.dto.UserDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest
class UserControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @WithAnonymousUser
    @Test
    fun `login route with anonyme user`() {
        val userDto = UserDto("TEST", "TEST", false)
        val jsonContent = objectMapper.writeValueAsString(userDto)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON) // Spécifie que la requête est en JSON
                .content(jsonContent) // Envoie le JSON dans le corps de la requête
        )
            .andExpect(status().isOk)
            .andExpect(content().string("User created successfully"))
    }
}