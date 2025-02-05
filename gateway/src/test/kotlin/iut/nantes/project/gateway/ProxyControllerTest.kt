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

        val requestBody = """{"name":"Food","description":"All foods"}"""

        mockMvc.post("/api/v1/families"){
            contentType = MediaType.APPLICATION_JSON
            content = requestBody
        }
            .andExpect {
                status { HttpStatus.UNAUTHORIZED }
            }
    }

    @WithMockUser(roles = ["ADMIN"])
    @Test
    fun `basic Admin POST request`(){

        val requestBody = """{"name":"Food","description":"All foods"}"""

        mockMvc.post("/api/v1/families") {
            contentType = MediaType.APPLICATION_JSON
            content = requestBody
        }
            .andExpect {
                status { HttpStatus.CREATED }
            }
    }


    @WithMockUser(roles = ["ADMIN"])
    @Test
    fun `basic Admin PUT request`(){

        val requestBody = """{"name":"Food","description":"All foods"}"""

        val request=mockMvc.post("/api/v1/families") {
            contentType = MediaType.APPLICATION_JSON
            content = requestBody
        }.andReturn()

        val response=request.response.contentAsString
        val id : String = JsonPath.read(response,"$.id")
        val newRequestBody = """{"name":"Candies","description":"All types of candies"}"""


        mockMvc.put("/api/v1/families/{id}",id) {
            contentType = MediaType.APPLICATION_JSON
            content = newRequestBody
        } .andExpect {
            status { HttpStatus.OK }
        }
    }


    @WithAnonymousUser
    @Test
    fun `basic Anonymous PUT request`(){

        val adminAutorisation=adminAutorisationForAnonymousTreatement()

        val requestBody = """{"name":"Food","description":"All foods"}"""

        val request=mockMvc.post("/api/v1/families") {
            contentType = MediaType.APPLICATION_JSON
            content = requestBody
            header("Authorization",adminAutorisation)

        }.andReturn()

        val response=request.response.contentAsString
        val id : String = JsonPath.read(response,"$.id")
        val newRequestBody = """{"name":"Candies","description":"All types of candies"}"""


        mockMvc.put("/api/v1/families/{id}",id) {
            contentType = MediaType.APPLICATION_JSON
            content = newRequestBody
        } .andExpect {
            status { HttpStatus.UNAUTHORIZED }
        }
    }



}