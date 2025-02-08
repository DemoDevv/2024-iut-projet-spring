package iut.nantes.project.products.controllerTests

import iut.nantes.project.products.controllers.FamilleController
import iut.nantes.project.products.controllers.ProductController
import iut.nantes.project.products.controllers.dto.FamilleDto
import iut.nantes.project.products.controllers.dto.Price
import iut.nantes.project.products.controllers.dto.ProductDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import java.util.*
import kotlin.test.assertEquals

@SpringBootTest
@AutoConfigureMockMvc
class ProductsControllerTest {


    @Autowired
    private lateinit var famillycontroller: FamilleController

    @Autowired
    private lateinit var productcontroller: ProductController


    @Test
    fun createProductBasicCase() {

        val famille = FamilleDto(id = null, name = "a name", description = "Valid description")
        val body = famillycontroller.createFamily(famille).body as FamilleDto
        val id = body.id
        val product=ProductDto(null,"un produit","une description de produit",Price(12,"EUR"),FamilleDto(id,null,null))
        assertEquals(HttpStatus.CREATED,productcontroller.createProduct(product).statusCode)

    }

    @Test
    fun createProductFamillyNotFound() {

        val famille = FamilleDto(id = null, name = "a name", description = "Valid description")
        famillycontroller.createFamily(famille)

        val product=ProductDto(null,"un produit","une description de produit",Price(12,"EUR"),FamilleDto(UUID.randomUUID().toString(),null,null))
        assertEquals(HttpStatus.BAD_REQUEST,productcontroller.createProduct(product).statusCode)

    }

    @Test
    fun getAllNoParam(){

        val famille = FamilleDto(id = null, name = "a name", description = "Valid description")

        val body = famillycontroller.createFamily(famille).body as FamilleDto
        val id = body.id


        val product=ProductDto(null,"un produit","une description de produit",Price(12,"EUR"),FamilleDto(id,null,null))
        val product2=ProductDto(null,"un autre produit","une description d'un autre produit",Price(15,"EUR"),FamilleDto(id,null,null))

        productcontroller.createProduct(product)
        productcontroller.createProduct(product2)
        val res=productcontroller.getProducts(null,null,null).body as List<ProductDto>
        println(res)
        assertEquals(2,res.size)

    }

    @Test
    fun getId(){

        val famille = FamilleDto(id = null, name = "a name", description = "Valid description")

        val body = famillycontroller.createFamily(famille).body as FamilleDto
        val id = body.id


        val product=ProductDto(null,"un produit","une description de produit",Price(12,"EUR"),FamilleDto(id,null,null))

        val bodyProduct=productcontroller.createProduct(product).body as ProductDto
        val idproduct = bodyProduct.id

        assertEquals(HttpStatus.OK,productcontroller.getProductById(idproduct ?: "").statusCode)

    }

    @Test
    fun getIdNotfound(){

        val famille = FamilleDto(id = null, name = "a name", description = "Valid description")
        val body = famillycontroller.createFamily(famille).body as FamilleDto
        val id = body.id

        val product=ProductDto(null,"un produit","une description de produit",Price(12,"EUR"),FamilleDto(id,null,null))

        productcontroller.createProduct(product).body as ProductDto

        assertEquals(HttpStatus.NOT_FOUND,productcontroller.getProductById(UUID.randomUUID().toString()).statusCode)

    }


    //You need to start the store server in memory
    @Test
    fun deleteID(){

        val famille = FamilleDto(id = null, name = "a name", description = "Valid description")
        val body = famillycontroller.createFamily(famille).body as FamilleDto
        val id = body.id

        val product=ProductDto(null,"un produit","une description de produit",Price(12,"EUR"),FamilleDto(id,null,null))

        val bodyproduct =productcontroller.createProduct(product).body as ProductDto

        assertEquals(HttpStatus.NO_CONTENT,productcontroller.deleteProduct(bodyproduct.id?:"").statusCode)

    }



}