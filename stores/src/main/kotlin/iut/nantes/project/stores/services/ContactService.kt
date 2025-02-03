package iut.nantes.project.stores.services

import iut.nantes.project.stores.controllers.dto.ContactDto
import iut.nantes.project.stores.exceptions.*
import iut.nantes.project.stores.repositories.ContactRepository
import iut.nantes.project.stores.repositories.StoreRepository
import org.springframework.stereotype.Service

@Service
class ContactService(private val contactRepository: ContactRepository, private val storeRepository: StoreRepository) {
    fun createContact(contact: ContactDto): ContactDto {

        verifyContact(contact)
        return contactRepository.save(contact.toEntity()).toDto()

    }

    fun getAllContacts(city: String?): List<ContactDto> {
        return contactRepository.findAll().filter { city == null || it.address.city == city }.map { it.toDto() }
    }

    fun getContactById(id: String): ContactDto {
        val idAslong = id.toLongOrNull() ?: throw InvalidIdFormatException()

        return contactRepository.findById(idAslong).orElseThrow {
            ContactNotFoundException()
        }.toDto()
    }

    fun updateContact(id: String, contactUpdate: ContactDto): ContactDto {
        val idAslong = id.toLongOrNull() ?: throw InvalidIdFormatException()

        verifyContact(contactUpdate)
        val contact = contactRepository.findById(idAslong).orElseThrow { ContactNotFoundException() }

        contact.email = contactUpdate.email
        contact.phone = contactUpdate.phone
        contact.address = contactUpdate.address

        return contactRepository.save(contact).toDto()
    }

    fun deleteContact(id: String) {
        val idAslong = id.toLongOrNull() ?: throw InvalidIdFormatException()
        if (storeRepository.existsByContactId(idAslong)) throw ConflictException()

        contactRepository.deleteById(idAslong)
    }


    private fun verifyContact(contact: ContactDto) {
        //validité du mail
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        if (!contact.email.matches(emailRegex)) {
            throw EmailNotValidExeception()


            //TODO(Les erreurs renseignées dans l'entity adresses ne sont pas levées ("sous entité" donc pas prise en compte ? car l'erreur de phone fonctionne et est dans l'entity principale).si elles fonctionnents cette fonction + ces exceptions créées ne servent à rien.)
            //validité du phone number
        } else if (contact.phone.length != 10) {
            throw PhoneNumberException()
            //validité de la street
        } else if (contact.address.street.length < 5 || contact.address.street.length > 50) {
            throw StreetNotValidException()
            //validité de la city
        } else if (contact.address.city.isEmpty() || contact.address.city.length > 30) {
            throw CityNotValidException()

            //Et validité du code postal
        } else if (contact.address.postalCode.length != 5) {
            throw PostalCodeNotValidException()

        } else {
            return
        }
    }
}