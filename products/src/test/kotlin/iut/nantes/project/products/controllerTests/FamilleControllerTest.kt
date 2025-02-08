package iut.nantes.project.products.controllerTests

import iut.nantes.project.products.controllers.FamilleController
import iut.nantes.project.products.controllers.dto.FamilleDto
import jakarta.validation.ConstraintViolationException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import kotlin.test.assertEquals

@SpringBootTest
@AutoConfigureMockMvc
class FamilleControllerTest {



    @Autowired
    private lateinit var famillycontroller:FamilleController






    @Test
    fun `basic case`() {

        val invalidFamille = FamilleDto(id = null, name = "a name", description = "Valid description")

        assertEquals(famillycontroller.createFamily(invalidFamille).statusCode,HttpStatus.CREATED)

    }

    @Test
    fun `should fail when name is too short`() {

        val invalidFamille = FamilleDto(id = null, name = "AB", description = "Valid description")

        assertThrows<ConstraintViolationException>{famillycontroller.createFamily(invalidFamille)}

    }

    @Test
    fun `should fail when name is too long`() {
        val invalidFamille = FamilleDto(id = null, name = "ttttttttttttttttttttttttttttttttttttttttttttt", description = "Valid description")

        assertThrows<ConstraintViolationException>{famillycontroller.createFamily(invalidFamille)}

    }

    @Test
    fun `should fail when description is too short`() {
        val invalidFamille = FamilleDto(id = null, name = "okay name", description = "no")

        assertThrows<ConstraintViolationException>{famillycontroller.createFamily(invalidFamille)}

    }
    @Test
    fun `should fail when description is too long`() {
        val invalidFamille = FamilleDto(id = null, name = "okay name", description = "this description is toooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo big")

        assertThrows<ConstraintViolationException>{famillycontroller.createFamily(invalidFamille)}

    }

    @Test
    fun `should work when both descriptions are same`() {
        val famille = FamilleDto(id = null, name = "okay name", description = "this description")
        val famille2 = FamilleDto(id = null, name = "very ok name", description = "this description")


       famillycontroller.createFamily(famille)
        assertEquals(famillycontroller.createFamily(famille2).statusCode,HttpStatus.CREATED)

    }

    @Test
    fun `should have conflict when both names are same`() {
        val famille = FamilleDto(id = null, name = "okay name", description = "this description")
        val famille2 = FamilleDto(id = null, name = "okay name", description = "a description")


        famillycontroller.createFamily(famille)
        assertEquals(famillycontroller.createFamily(famille2).statusCode,HttpStatus.CONFLICT)

    }

}