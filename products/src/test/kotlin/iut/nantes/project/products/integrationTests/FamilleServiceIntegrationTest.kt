package iut.nantes.project.products.integrationTests

import iut.nantes.project.products.controllers.dto.FamilleDto
import iut.nantes.project.products.exceptions.FamilleNameConflictException
import iut.nantes.project.products.exceptions.FamilleNotFoundException
import iut.nantes.project.products.exceptions.InvalidIdFormatException
import iut.nantes.project.products.services.FamilleService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.UUID.randomUUID
import kotlin.test.assertEquals

@SpringBootTest
class FamilleServiceIntegrationTest {

    @Autowired
    private lateinit var familleService: FamilleService

    @Test
    @Transactional
    fun `lorsqu'on ajoute une famille, elle doit être persistée et retrouvée`() {
        val uuid = randomUUID().toString()
        val famille = FamilleDto(
            uuid, "famille de test", "Ceci est une famille de test"
        )
        familleService.createFamille(famille)

        val retrievedFamille = familleService.getFamilleById(uuid)

        assertEquals("famille de test", retrievedFamille.name)
    }

    @Test
    fun `lorsqu'on ajout deux famille avec le même nom, on doit obtenir une erreur de conflit`() {
        val famille1 = FamilleDto(randomUUID().toString(), "Famille de test", "Ceci est une famille de test")
        val famille2 =
            FamilleDto(randomUUID().toString(), "Famille de test", "Ceci est une famille de test avec le même nom")

        familleService.createFamille(famille1)

        assertThrows<FamilleNameConflictException> {
            familleService.createFamille(famille2)
        }
    }

    @Test
    @Transactional
    fun `lorsqu'on recherche une famille existante avec le mauvais id, on doit obtenir une erreur not found`() {
        val goodId = randomUUID().toString()
        val badId = goodId + "false"
        val famille1 = FamilleDto(goodId, "famille de test", "Ceci est une famille de test")

        familleService.createFamille(famille1)

        assertThrows<FamilleNotFoundException> {
            familleService.getFamilleById(badId)
        }
    }

    @Test
    @Transactional
    fun `lorsqu'on recherche une famille existante avec id qui n'est pas un UUID, on doit obtenir une erreur invalid id format`() {
        val badId = "it's a very bad UUID"
        val famille1 = FamilleDto(randomUUID().toString(), "famille de test", "Ceci est une famille de test")

        familleService.createFamille(famille1)

        assertThrows<InvalidIdFormatException> {
            familleService.getFamilleById(badId)
        }
    }
}