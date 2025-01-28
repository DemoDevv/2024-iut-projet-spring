package iut.nantes.project.products.integrationTests

import iut.nantes.project.products.controllers.dto.FamilleDto
import iut.nantes.project.products.controllers.dto.Price
import iut.nantes.project.products.controllers.dto.ProductDto
import iut.nantes.project.products.exceptions.FamilleNotFoundException
import iut.nantes.project.products.exceptions.InvalidIdFormatException
import iut.nantes.project.products.exceptions.ProductNotFoundException
import iut.nantes.project.products.services.FamilleService
import iut.nantes.project.products.services.ProductService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.util.UUID.randomUUID
import kotlin.test.assertEquals

@SpringBootTest
class ProductServiceIntegrationTest {

    @Autowired
    private lateinit var productService: ProductService
    @Autowired
    private lateinit var familleService: FamilleService

    private fun seed(uuid: String) {
        val famille = FamilleDto(
            randomUUID().toString(), "famille de test", "Ceci est une famille de test"
        )
        val product = ProductDto(
            uuid, "produit de test", "Ceci est un produit de test", Price(35, "EUR"), famille
        )

        familleService.createFamille(famille)
        productService.createProduct(product)
    }

    @Test
    @Transactional
    fun `lorsqu'on ajoute un produit, il doit être persisté et retrouvé`() {
        val uuid = randomUUID().toString()
        seed(uuid)

        val retrievedProduct = productService.getProductById(uuid)

        assertEquals("famille de test", retrievedProduct.family.name)
        assertEquals(35, retrievedProduct.price.amount)
        assertEquals("produit de test", retrievedProduct.name)
    }

    @Test
    @Transactional
    fun `lorsqu'on ajoute un produit avec une famille inexistante il doit y avoir une erreur`() {
        val uuid = randomUUID().toString()
        val famille = FamilleDto(
            randomUUID().toString(), "famille de test", "Ceci est une famille de test"
        )
        val product = ProductDto(
            uuid, "produit de test", "Ceci est un produit de test", Price(35, "EUR"), famille
        )

        assertThrows<FamilleNotFoundException> {
            productService.createProduct(product)
        }
    }

    @Test
    @Transactional
    fun `lorsqu'on recherche un produit avec un id invalide, il doit y avoir une erreur`() {
        assertThrows<InvalidIdFormatException> {
            productService.getProductById("uh422u-zhf3fh-3947ezhf2izeh22-fiezh4-fi8z")
        }

        assertThrows<ProductNotFoundException> {
            val uuid = randomUUID().toString()
            productService.getProductById(uuid)
        }
    }

    @Test
    @Transactional
    fun `lorsqu'on recherche un produit avec un son id, on doit obtenir le produit correspondant`() {
        val uuid = randomUUID().toString()
        seed(uuid)

        val retrievedProduct = productService.getProductById(uuid)

        assertEquals("famille de test", retrievedProduct.family.name)
        assertEquals(35, retrievedProduct.price.amount)
        assertEquals("produit de test", retrievedProduct.name)
    }

    @Test
    @Transactional
    fun `lorsqu'on met à jour un produit avec une famille inexistante, on doit obtenir une erreur`() {
        val uuid = randomUUID().toString()
        seed(uuid)

        val fakeFamille = FamilleDto(randomUUID().toString(), "Famille de test", "famille qui n'existe pas")
        val productUpdate = ProductDto(
            uuid, "produit de test", "Ceci est un produit de test", Price(35, "EUR"), fakeFamille
        )

        assertThrows<FamilleNotFoundException> { productService.updateProduct(uuid, productUpdate) }
    }

    @Test
    @Transactional
    fun `lorsqu'on met à jour un produit correctement, on doit obtenir un produit modifié`() {
        val uuid = randomUUID().toString()
        seed(uuid)

        val newFamille = FamilleDto(
            randomUUID().toString(), "famille de test 2", "Ceci est une famille de test"
        )
        val productUpdate = ProductDto(
            uuid, "produit de test", "Ceci est un produit de test modifié", Price(40, "EUR"), newFamille
        )

        familleService.createFamille(newFamille)

        val retrievedProduct = productService.updateProduct(uuid, productUpdate)

        assertEquals("famille de test 2", retrievedProduct.family.name)
        assertEquals(40, retrievedProduct.price.amount)
        assertEquals("produit de test", retrievedProduct.name)
    }

}