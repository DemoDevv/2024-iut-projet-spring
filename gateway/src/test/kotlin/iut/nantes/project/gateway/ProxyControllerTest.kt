package iut.nantes.project.gateway

import com.fasterxml.jackson.databind.ObjectMapper
import com.jayway.jsonpath.JsonPath
import org.apache.tomcat.util.codec.binary.Base64
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.reactive.server.JsonPathAssertions
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import kotlin.math.ceil

@WebMvcTest
@Import(TestConfiguration::class)
class ProxyControllerTest {

//OBLIGATOIRE: LA BASE DE DONNEE DES AUTRES MICRO-SERVICES DOIVENT ETRE EN MEMOIRE POUR TESTER !!!

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper




    private fun adminAutorisationForAnonymousTreatement():String{
        val credentials = "ADMIN:ADMIN"
        val encoded = Base64.encodeBase64String(credentials.toByteArray())
        return "Basic $credentials"
    }


    @WithMockUser(roles = ["ADMIN"])
    @Test
    fun `basic Admin GET request`() {
        mockMvc.get("/api/v1/products")
            .andExpect {
                status { HttpStatus.OK }
            }
    }
    @WithAnonymousUser
    @Test
    fun `basic anonymous GET request`() {
        mockMvc.get("/api/v1/products")
            .andExpect {
                status { HttpStatus.UNAUTHORIZED}
            }
    }

    @WithAnonymousUser
    @Test
    fun `basic anonymous POST request`(){

        mockMvc.post("/api/v1/families", "{\"name\":\"Food\",\"description\":\"All foods\"}")
            .andExpect {
                status { HttpStatus.UNAUTHORIZED }
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
                status { HttpStatus.CREATED }
            }
    }


    @WithMockUser(roles = ["ADMIN"])
    @Test
    fun `basic Admin PUT request`(){

        val requestBody = """{"name":"Foodeeee","description":"All foods"}"""// Corps de la requête JSON

        val request=mockMvc.post("/api/v1/families") {
            contentType = MediaType.APPLICATION_JSON // Indique que c'est du JSON
            content = requestBody // Passe le JSON dans le body
        }.andReturn()

        val response=request.response.contentAsString
        val id : String = JsonPath.read(response,"$.id")

        mockMvc.put("/api/v1/families/{id}",id) {
            contentType = MediaType.APPLICATION_JSON // Indique que c'est du JSON
            content = requestBody // Passe le JSON dans le body
        } .andExpect {
            status { HttpStatus.OK }
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