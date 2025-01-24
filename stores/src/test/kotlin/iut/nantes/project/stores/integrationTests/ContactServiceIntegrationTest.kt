package iut.nantes.project.stores.integrationTests

import iut.nantes.project.stores.controllers.dto.Address
import iut.nantes.project.stores.controllers.dto.ContactDto
import iut.nantes.project.stores.exceptions.ContactNotFoundException
import iut.nantes.project.stores.exceptions.InvalidIdFormatException
import iut.nantes.project.stores.repositories.ContactRepository
import iut.nantes.project.stores.services.ContactService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
class ContactServiceIntegrationTest {

    @Autowired
    private lateinit var contactService: ContactService

    @Autowired
    private lateinit var contactRepository: ContactRepository

    private fun seedContact(): ContactDto {
        val address = Address("10 rue de la paix", "Paris", "75000")
        val contact = ContactDto(
            id = 0L,
            email = "test@email.com",
            phone = "0123456789",
            address = address
        )
        return contactService.createContact(contact)
    }

    @Test
    @Transactional
    fun `lorsqu'on crée un nouveau contact, il doit être persisté et retrouvé`() {
        val contact = seedContact()

        // Retrouver le contact depuis le ContactService
        val retrievedContact = contactService.getContactById(contact.id.toString())

        // Vérifier les champs
        assertEquals(contact.email, retrievedContact.email)
        assertEquals(contact.phone, retrievedContact.phone)
        assertNotNull(retrievedContact.address)
        assertEquals(contact.address.street, retrievedContact.address.street)
        assertEquals(contact.address.city, retrievedContact.address.city)
        assertEquals(contact.address.postalCode, retrievedContact.address.postalCode)
    }

    @Test
    @Transactional
    fun `lorsqu'on recherche un contact avec un ID invalide, on doit obtenir une erreur`() {
        // Test avec un ID au format non valide
        assertThrows<InvalidIdFormatException> {
            contactService.getContactById("invalid-id")
        }

        // Test avec un ID inexistant
        assertThrows<ContactNotFoundException> {
            contactService.getContactById("123")
        }
    }

    @Test
    @Transactional
    fun `lorsqu'on met à jour un contact existant, les données doivent être modifiées`() {
        val contact = seedContact()

        val updatedContact = ContactDto(
            id = contact.id,
            email = "updated@email.com",
            phone = "9876543210",
            address = Address("20 avenue des Champs", "Lyon", "69000")
        )

        val contactAfterUpdate = contactService.updateContact(contact.id.toString(), updatedContact)

        // Vérifiez que les données sont mises à jour
        assertEquals("updated@email.com", contactAfterUpdate.email)
        assertEquals("9876543210", contactAfterUpdate.phone)
        assertNotNull(contactAfterUpdate.address)
        assertEquals("20 avenue des Champs", contactAfterUpdate.address.street)
        assertEquals("Lyon", contactAfterUpdate.address.city)
        assertEquals("69000", contactAfterUpdate.address.postalCode)
    }

    @Test
    @Transactional
    fun `lorsqu'on met à jour un contact avec un ID invalide, on doit obtenir une erreur`() {
        val contact = seedContact()

        val updatedContact = ContactDto(
            id = contact.id,
            email = "invalid@email.com",
            phone = "0000000000",
            address = Address("10 rue de l'erreur", "Erreur-ville", "12345")
        )

        // Test avec un ID non numérique :
        assertThrows<InvalidIdFormatException> {
            contactService.updateContact("invalid-ID", updatedContact)
        }

        // Test avec un ID inexistant :
        assertThrows<ContactNotFoundException> {
            contactService.updateContact("9999", updatedContact)
        }
    }

    @Test
    @Transactional
    fun `lorsqu'on supprime un contact, il ne doit plus exister dans la base`() {
        val contact = seedContact()

        // Supprimez le contact
        contactService.deleteContact(contact.id.toString())

        // Vérifiez qu'il n'existe plus
        assertThrows<ContactNotFoundException> {
            contactService.getContactById(contact.id.toString())
        }
        val contactExists = contactRepository.findById(contact.id).isPresent
        assertEquals(false, contactExists)
    }

    @Test
    @Transactional
    fun `lorsqu'on supprime un contact avec un ID invalide, on doit obtenir une erreur`() {
        // Test avec un ID non numérique :
        assertThrows<InvalidIdFormatException> {
            contactService.deleteContact("invalid-ID")
        }
    }

    @Test
    @Transactional
    fun `lorsqu'on récupère tous les contacts, ils doivent être filtrés correctement`() {
        seedContact()
        seedContact()

        // Récupérer tous les contacts
        val allContacts = contactService.getAllContacts(null)
        assertEquals(2, allContacts.size)

        // Filtrage par ville
        val filteredContacts = contactService.getAllContacts("Paris")
        assertEquals(2, filteredContacts.size)

        // Filtrage par ville inexistante
        val noContacts = contactService.getAllContacts("NonExistingCity")
        assertEquals(0, noContacts.size)
    }
}
