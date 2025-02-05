package iut.nantes.project.gateway

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest
@Import(TestConfiguration::class)
class ProxyControllerTest {

//OBLIGATOIRE: LA BASE DE DONNEE DES AUTRES MICRO-SERVICES DOIVENT ETRE EN MEMOIRE POUR TESTER !!!

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

    @WithMockUser(roles = ["ADMIN"])
    @Test
    fun `basic Admin GET request`() {
        mockMvc.get("/api/v1/products")
            .andExpect {
                status { isOk() }
            }
    }
    @WithAnonymousUser
    @Test
    fun `basic anonymous GET request`() {
        mockMvc.get("/api/v1/products")
            .andExpect {
                status { isUnauthorized() }
            }
    }

    @WithAnonymousUser
    @Test
    fun `basic anonymous POST request`(){

        mockMvc.post("/api/v1/families", "{\"name\":\"Food\",\"description\":\"All foods\"}")
            .andExpect {
                status { isUnauthorized() }
            }
    }

    @WithMockUser(roles = ["ADMIN"])
    @Test
    fun `basic Admin POST request`(){

        val requestBody = """{"name":"Foodeeee","description":"All foods"}"""// Corps de la requête JSON

        mockMvc.post("/api/v1/families") {
            contentType = MediaType.APPLICATION_JSON // Indique que c'est du JSON
            content = requestBody // Passe le JSON dans le body
        }
            .andExpect {
                status { isCreated() }
            }



    }

    @WithMockUser(roles = ["ADMIN"])
    @Test
    fun `route without admin role`() {
        mockMvc.get("/api/v1/stores/1/products/1/add?quantity=2")
            .andExpect {
                status { isOk() }
            }
    }

}