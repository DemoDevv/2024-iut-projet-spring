package iut.nantes.project.stores.controllers

import iut.nantes.project.stores.controllers.dto.ContactDto
import iut.nantes.project.stores.services.ContactService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/contacts")
@Validated
class ContactController(private val contactService: ContactService) {
    // POST /api/v1/contacts : Création d'un contact
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createContact(@Valid @RequestBody contactDto: ContactDto): ContactDto {
        return contactService.createContact(contactDto)
    }

    // GET /api/v1/contacts : Récupérer tous les contacts (optionnellement filtrés par ville)
    @GetMapping
    fun getAllContacts(@RequestParam(value = "city", required = false) city: String?): List<ContactDto> {
        return contactService.getAllContacts(city)
    }

    // GET /api/v1/contacts/{id} : Récupérer un contact par son ID
    @GetMapping("/{id}")
    fun getContactById(@PathVariable id: String): ContactDto {
        return contactService.getContactById(id)
    }

    // PUT /api/v1/contacts/{id} : Mise à jour d'un contact
    @PutMapping("/{id}")
    fun updateContact(@PathVariable id: String, @Valid @RequestBody contactDto: ContactDto): ContactDto {
        return contactService.updateContact(id, contactDto)
    }

    // DELETE /api/v1/contacts/{id} : Suppression d'un contact
    @DeleteMapping("/{id}")
    fun deleteContact(@PathVariable id: String) {
        return contactService.deleteContact(id)
    }
}