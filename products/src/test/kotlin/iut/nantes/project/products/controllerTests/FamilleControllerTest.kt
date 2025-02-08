package iut.nantes.project.products.controllerTests

import iut.nantes.project.products.controllers.FamilleController
import iut.nantes.project.products.controllers.ProductController
import iut.nantes.project.products.controllers.dto.FamilleDto
import iut.nantes.project.products.controllers.dto.Price
import iut.nantes.project.products.controllers.dto.ProductDto
import jakarta.validation.ConstraintViolationException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import java.util.*
import kotlin.test.assertEquals

@SpringBootTest
@AutoConfigureMockMvc
class FamilleControllerTest {


    @Autowired
    private lateinit var famillycontroller: FamilleController

    @Autowired
    private lateinit var productcontroller: ProductController

    @Test
    fun `basic case`() {

        val invalidFamille = FamilleDto(id = null, name = "a name", description = "Valid description")

        assertEquals(HttpStatus.CREATED,famillycontroller.createFamily(invalidFamille).statusCode)

    }

    @Test
    fun `should fail when name is too short`() {

        val invalidFamille = FamilleDto(id = null, name = "AB", description = "Valid description")

        assertThrows<ConstraintViolationException> { famillycontroller.createFamily(invalidFamille) }

    }

    @Test
    fun `should fail when name is too long`() {
        val invalidFamille = FamilleDto(
            id = null,
            name = "ttttttttttttttttttttttttttttttttttttttttttttt",
            description = "Valid description"
        )

        assertThrows<ConstraintViolationException> { famillycontroller.createFamily(invalidFamille) }

    }

    @Test
    fun `should fail when description is too short`() {
        val invalidFamille = FamilleDto(id = null, name = "okay name", description = "no")

        assertThrows<ConstraintViolationException> { famillycontroller.createFamily(invalidFamille) }

    }

    @Test
    fun `should fail when description is too long`() {
        val invalidFamille = FamilleDto(
            id = null,
            name = "okay name",
            description = "this description is toooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo big"
        )

        assertThrows<ConstraintViolationException> { famillycontroller.createFamily(invalidFamille) }

    }

    @Test
    fun `should work when both descriptions are same`() {
        val famille = FamilleDto(id = null, name = "okay name", description = "this description")
        val famille2 = FamilleDto(id = null, name = "very ok name", description = "this description")


        famillycontroller.createFamily(famille)
        assertEquals(HttpStatus.CREATED,famillycontroller.createFamily(famille2).statusCode)

    }

    @Test
    fun `should have conflict when both names are same`() {
        val famille = FamilleDto(id = null, name = "okay name", description = "this description")
        val famille2 = FamilleDto(id = null, name = "okay name", description = "a description")


        famillycontroller.createFamily(famille)
        assertEquals(HttpStatus.CONFLICT,famillycontroller.createFamily(famille2).statusCode)

    }

    @Test
    fun getAll() {
        val famille = FamilleDto(id = null, name = "okay name", description = "this description")
        val famille2 = FamilleDto(id = null, name = "very ok name", description = "a description")
        val famille3 = FamilleDto(id = null, name = "very very ok name", description = "a description")

        famillycontroller.createFamily(famille)
        famillycontroller.createFamily(famille2)
        famillycontroller.createFamily(famille3)
        assertEquals(3,famillycontroller.getAllFamilies().body?.size ?: 0)

    }

    @Test
    fun getbyId() {
        val famille = FamilleDto(id = null, name = "okay name", description = "this description")

        val body = famillycontroller.createFamily(famille).body as FamilleDto
        val id = body.id

        assertEquals(famillycontroller.getFamilyById(id ?: "").statusCode, HttpStatus.OK)

    }

    @Test
    fun failGetbyId() {

        val famille = FamilleDto(id = null, name = "okay name", description = "this description")
        famillycontroller.createFamily(famille).body as FamilleDto
        assertEquals(HttpStatus.NOT_FOUND, famillycontroller.getFamilyById(UUID.randomUUID().toString()).statusCode)

    }

    @Test
    fun updateFamily() {
        val famille = FamilleDto(id = null, name = "okay name", description = "this description")

        val body = famillycontroller.createFamily(famille).body as FamilleDto
        val id = body.id

        val newFamille = FamilleDto("flk", "New name", "new description")

        val result = famillycontroller.updateFamily(id ?: "", newFamille)
        val body2 = result.body as FamilleDto
        val sameId = body2.id
        val name = body2.name
        val description = body2.description

        assertEquals(listOf(name, description, sameId), listOf(newFamille.name, newFamille.description, id))

    }

    @Test
    fun `Can't update family, not found`() {
        val famille = FamilleDto(id = null, name = "okay name", description = "this description")

        val body = famillycontroller.createFamily(famille).body as FamilleDto
        val id = body.id
        val newFamille = FamilleDto(null, "New name", "new description")

        val result = famillycontroller.updateFamily(UUID.randomUUID().toString(), newFamille)
        val body2 = result.body as FamilleDto
        val sameId = body2.id
        val name = body2.name
        val description = body2.description

        assertEquals(listOf(name, description, sameId), listOf(newFamille.name, newFamille.description, id))
    }

    @Test
    fun `Can't update family, conflit`() {

        val famille = FamilleDto(id = null, name = "okay name", description = "this description")
        famillycontroller.createFamily(famille)

        val familletoUpdate = FamilleDto(id = null, name = "AnotherName", description = "this description")
        val body = famillycontroller.createFamily(familletoUpdate).body as FamilleDto
        val id = body.id
        val newFamille = FamilleDto(null, "okay name", "new description")


        assertEquals(HttpStatus.CONFLICT,famillycontroller.updateFamily(id ?: "", newFamille).statusCode)

    }


    @Test
    fun deleteFamilly() {
        val famille = FamilleDto(id = null, name = "okay name", description = "this description")

        val body = famillycontroller.createFamily(famille).body as FamilleDto
        val id = body.id

        assertEquals(HttpStatus.NO_CONTENT,famillycontroller.deleteFamille(id ?: "").statusCode)

    }

    @Test
    fun `Can't delete family, not found`() {
        val famille = FamilleDto(id = null, name = "okay name", description = "this description")

        famillycontroller.createFamily(famille).body as FamilleDto

        assertEquals(HttpStatus.NOT_FOUND,famillycontroller.deleteFamille(UUID.randomUUID().toString()).statusCode)

    }

    @Test
    fun `Can't delete family, conflict`() {

        val famille = FamilleDto(id = null, name = "okay name", description = "this description")
        val body = famillycontroller.createFamily(famille).body as FamilleDto
        val id = body.id
        famille.id = id

        val produit = ProductDto(null, "a product", "this is a product", Price(20, "EUR"), famille)
        productcontroller.createProduct(produit)


        assertEquals(HttpStatus.CONFLICT,famillycontroller.deleteFamille(id ?: "").statusCode)

    }

}