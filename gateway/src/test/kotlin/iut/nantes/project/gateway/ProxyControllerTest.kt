package iut.nantes.project.gateway

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest
@Import(TestConfiguration::class)
class ProxyControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @WithAnonymousUser
    @Test
    fun `admin route without admin role`() {
        mockMvc.get("/api/v1/families")
            .andExpect {
                status { isUnauthorized() }
            }
    }

    @WithMockUser(roles = ["ADMIN"])
    @Test
    fun `admin route with admin role`() {
        mockMvc.get("/api/v1/families")
            .andExpect {
                status { isOk() }
            }
    }

    @WithMockUser
    @Test
    fun `route without admin role`() {
        mockMvc.get("/api/v1/stores/1/products/1/add?quantity=2")
            .andExpect {
                status { isOk() }
            }
    }

}