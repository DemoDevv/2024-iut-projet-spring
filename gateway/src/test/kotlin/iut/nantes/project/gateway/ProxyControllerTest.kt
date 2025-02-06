package iut.nantes.project.gateway

import com.fasterxml.jackson.databind.ObjectMapper
import com.jayway.jsonpath.JsonPath
import iut.nantes.project.gateway.controller.dto.UserDto
import org.apache.tomcat.util.codec.binary.Base64
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.*

@WebMvcTest
@Import(TestConfiguration::class)
class ProxyControllerTest {

//OBLIGATOIRE: LA BASE DE DONNEE DES AUTRES MICRO-SERVICES DOIVENT ETRE EN MEMOIRE POUR TESTER !!!

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper


    //donne un droit admin pour permettre un peuplement d'un serveur en mémoire depuis la gateway, quand on teste pour un anonymous user.
    private fun adminAutorisationForAnonymousTreatement(): String {

        val credentials = "ADMIN:ADMIN"
        val encoded = Base64.encodeBase64String(credentials.toByteArray())
        return "Basic $encoded"
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
                status { HttpStatus.UNAUTHORIZED }
            }
    }

    @WithAnonymousUser
    @Test
    fun `basic anonymous POST request`() {

        val requestBody = """{"name":"Food","description":"All foods"}"""

        mockMvc.post("/api/v1/families") {
            contentType = MediaType.APPLICATION_JSON
            content = requestBody
        }
            .andExpect {
                status { HttpStatus.UNAUTHORIZED }
            }
    }

    @WithMockUser(roles = ["ADMIN"])
    @Test
    fun `basic Admin POST request`() {

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
    fun `basic Admin PUT request`() {

        val requestBody = """{"name":"Food","description":"All foods"}"""

        val request = mockMvc.post("/api/v1/families") {
            contentType = MediaType.APPLICATION_JSON
            content = requestBody
        }.andReturn()

        val response = request.response.contentAsString
        val id: String = JsonPath.read(response, "$.id")
        val newRequestBody = """{"name":"Candies","description":"All types of candies"}"""


        mockMvc.put("/api/v1/families/{id}", id) {
            contentType = MediaType.APPLICATION_JSON
            content = newRequestBody
        }.andExpect {
            status { HttpStatus.OK }
        }
    }


    @WithAnonymousUser
    @Test
    fun `basic Anonymous PUT request`() {

        val adminAutorisation = adminAutorisationForAnonymousTreatement()

        val requestBody = """{"name":"Drink","description":"All Drink"}"""

        val request = mockMvc.post("/api/v1/families") {
            contentType = MediaType.APPLICATION_JSON
            content = requestBody
            header("Authorization", adminAutorisation)

        }.andReturn()

        val response = request.response.contentAsString
        println(response)
        val id = JsonPath.read<String>(response, "$.id")
        val newRequestBody = """{"name":"Candies","description":"All types of candies"}"""


        mockMvc.put("/api/v1/families/{id}", id) {
            contentType = MediaType.APPLICATION_JSON
            content = newRequestBody
        }.andExpect {
            status { HttpStatus.UNAUTHORIZED }
        }
    }


    @WithMockUser(roles = ["ADMIN"])
    @Test
    fun `basic Admin DELETE request`() {


        val requestBody = """{"name":"Drink","description":"All Drink"}"""

        val request = mockMvc.post("/api/v1/families") {
            contentType = MediaType.APPLICATION_JSON
            content = requestBody
        }.andReturn()

        val response = request.response.contentAsString
        println(response)
        val id = JsonPath.read<String>(response, "$.id")


        mockMvc.delete("/api/v1/families/{id}", id)
            .andExpect {
                status { HttpStatus.NO_CONTENT }
            }
    }


    @WithAnonymousUser
    @Test
    fun `basic Anonymous DELETE request`() {

        val adminAutorisation = adminAutorisationForAnonymousTreatement()

        val requestBody = """{"name":"Drink","description":"All Drink"}"""

        val request = mockMvc.post("/api/v1/families") {
            contentType = MediaType.APPLICATION_JSON
            content = requestBody
            header("Authorization", adminAutorisation)
        }.andReturn()

        val response = request.response.contentAsString
        println(response)
        val id = JsonPath.read<String>(response, "$.id")


        mockMvc.delete("/api/v1/families/{id}", id)
            .andExpect {
                status { HttpStatus.UNAUTHORIZED }
            }
    }

    //Tests des stocks

    @WithAnonymousUser
    @Test
    fun `all stock Routes is authorized for anonymous user`() {

        val adminCredentials = adminAutorisationForAnonymousTreatement()

        val storeId=createCompletStoreFromScratch()
        val products=createSomeProducts()



        mockMvc.post("/api/v1/stores/{storeId}/products/{productID}/add?quantity=4",storeId,
            products["products"]?.get(1) ?: 0
        ) {

            header("Authorization", adminCredentials)
        }.andExpect {
            status { HttpStatus.CREATED }
        }
    }






    //Créer un magasin en base et retourne son id.
    private fun createCompletStoreFromScratch(): Int {

        val adminCredentials=adminAutorisationForAnonymousTreatement()

        val contact="""{"email": "contact1@email.com","phone": "0123456789","address": {"street": "15 Rue des Lilas","city": "Paris","postalCode": "75000"}}"""
        val requestcontact=mockMvc.post("/api/v1/contacts") {
            contentType = MediaType.APPLICATION_JSON
            content = contact
            header("Authorization", adminCredentials)
        }.andReturn()

        val responsecontact = requestcontact.response.contentAsString
        val contactid: Int = JsonPath.read(responsecontact, "$.id")


        //create store
        val store= """{"name": "Atlantis","contact": {"id": $contactid,"email": "contact1@email.com","phone": "0123456789","address": {"street": "15 Rue des Lilas","city": "Paris","postalCode": "75000"}},"products": []}"""
        val requeststore=mockMvc.post("/api/v1/stores") {
            contentType = MediaType.APPLICATION_JSON
            content = store
            header("Authorization", adminCredentials)
        }.andReturn()
        val responsestore = requeststore.response.contentAsString

        return JsonPath.read(responsestore, "$.id")
    }

    private fun createSomeProducts():MutableMap<String,List<String>>{

        val adminCredentials = adminAutorisationForAnonymousTreatement()
        val hashmap= mutableMapOf<String,List<String>>()

        //create Families
        val family1RequestBody =
            """{"name": "Smartphones","description": "All kinds of mobile phones, from basic to advanced models with various features."}"""
        val family2RequestBody =
            """{"name": "Kitchen Appliances","description": "Devices used for food preparation, cooking, and storage in the kitchen."}"""


        //première famille
        val requestFamily1=mockMvc.post("/api/v1/families") {
            contentType = MediaType.APPLICATION_JSON
            content = family1RequestBody
            header("Authorization", adminCredentials)
        }.andReturn()

        val responseFamily1 = requestFamily1.response.contentAsString
        val familyid1: String = JsonPath.read(responseFamily1, "$.id")


        //seconde famille
        val requestFamily2=mockMvc.post("/api/v1/families") {
            contentType = MediaType.APPLICATION_JSON
            content = family2RequestBody
            header("Authorization", adminCredentials)
        }.andReturn()

        val responseFamily2 = requestFamily2.response.contentAsString
        val familyid2: String = JsonPath.read(responseFamily2, "$.id")

        //create products
        val product1Requestbody =
            """{"name": "iPhone 14 Pro","description": "Latest Apple smartphone with advanced camera and display features.","price": {"amount": 999,"currency": "USD"},"family": {"id":"$familyid1","name": "Smartphones","description": "All kinds of mobile phones, from basic to advanced models with various features."}}"""
        val product2Requestbody =
            """{"name": "Samsung Galaxy S23","description": "Flagship smartphone with powerful performance and high-resolution screen.","price": {"amount": 849,"currency": "USD"},"family": {"id":"$familyid1","name": "Smartphones","description": "All kinds of mobile phones, from basic to advanced models with various features."}}
            """
        val product3Requestbody =
            """{"name": "Instant Pot Duo","description": "7-in-1 electric pressure cooker with multiple cooking functions.","price": {"amount": 89,"currency": "USD"},"family": {"id":"$familyid2","name": "Kitchen Appliances","description": "Devices used for food preparation, cooking, and storage in the kitchen."}}
            """

        //premier produit
        val requestproduct1=mockMvc.post("/api/v1/products") {
            contentType = MediaType.APPLICATION_JSON
            content = product1Requestbody
            header("Authorization", adminCredentials)
        }.andReturn()

        val responseProduct1 = requestproduct1.response.contentAsString
        val product1id: String = JsonPath.read(responseProduct1, "$.id")

        //second produit
        val requestproduct2=mockMvc.post("/api/v1/products") {
            contentType = MediaType.APPLICATION_JSON
            content = product2Requestbody
            header("Authorization", adminCredentials)
        }.andReturn()

        val responseProduct2 = requestproduct2.response.contentAsString
        val product2id: String = JsonPath.read(responseProduct2, "$.id")


        //troisième produit
        val requestproduct3=mockMvc.post("/api/v1/products") {
            contentType = MediaType.APPLICATION_JSON
            content = product3Requestbody
            header("Authorization", adminCredentials)
        }.andReturn()

        val responseProduct3 = requestproduct3.response.contentAsString
        val product3id: String = JsonPath.read(responseProduct3, "$.id")


        hashmap["products"] = listOf(product1id,product2id,product3id)

        return hashmap
    }



}