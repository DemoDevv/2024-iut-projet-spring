package iut.nantes.project.stores.services

import iut.nantes.project.stores.controllers.dto.ContactDto
import org.springframework.stereotype.Service

@Service
class ContactService {
    fun createContact(contact: ContactDto): ContactDto {

    }

    fun getAllContacts(city: String?): List<ContactDto> {

    }

    fun getContactById(id: Long): ContactDto {

    }

    fun updateContact(id: Long, contactUpdate: ContactDto): ContactDto {

    }

    fun deleteContact(id: Long) {

    }
}