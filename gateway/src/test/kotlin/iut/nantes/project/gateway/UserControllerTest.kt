package iut.nantes.project.gateway

import com.fasterxml.jackson.databind.ObjectMapper
import iut.nantes.project.gateway.controller.dto.UserDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@WebMvcTest
@Import(TestConfiguration::class)
class UserControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @WithAnonymousUser
    @Test
    fun `login route with anonymous user`() {
        val userDto = UserDto("TEST", "TEST", false)
        val jsonContent = objectMapper.writeValueAsString(userDto)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent)
        )
            .andExpect(status().isOk)
            .andExpect(content().string("User created successfully"))
    }
}
