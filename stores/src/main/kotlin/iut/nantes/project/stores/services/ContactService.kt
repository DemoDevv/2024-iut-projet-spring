package iut.nantes.project.stores.services

import iut.nantes.project.stores.controllers.dto.ContactDto
import iut.nantes.project.stores.exceptions.ConflictException
import iut.nantes.project.stores.exceptions.ContactNotFoundException
import iut.nantes.project.stores.exceptions.InvalidIdFormatException
import iut.nantes.project.stores.repositories.ContactRepository
import iut.nantes.project.stores.repositories.StoreRepository
import org.springframework.stereotype.Service

@Service
class ContactService(private val contactRepository: ContactRepository, private val storeRepository: StoreRepository) {
    fun createContact(contact: ContactDto): ContactDto {
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

        val contact = contactRepository.findById(idAslong).orElseThrow { ContactNotFoundException() }

        contact.email = contactUpdate.email
        contact.phone = contactUpdate.phone
        contact.address = contactUpdate.address

        return contactRepository.save(contact).toDto()
    }

    fun deleteContact(id: String) {
        val idAslong = id.toLongOrNull() ?: throw InvalidIdFormatException()

        // todo: je sais pas si cela fonctionne il faut faire des tests
        if (storeRepository.existsByContactId(idAslong)) throw ConflictException()

        contactRepository.deleteById(idAslong)
    }
}