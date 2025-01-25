package iut.nantes.project.stores.services

import iut.nantes.project.stores.controllers.dto.Product
import iut.nantes.project.stores.controllers.dto.StoreDto
import iut.nantes.project.stores.exceptions.ContactNotFoundException
import iut.nantes.project.stores.exceptions.InvalidIdFormatException
import iut.nantes.project.stores.exceptions.StoreNotFoundException
import iut.nantes.project.stores.repositories.ContactRepository
import iut.nantes.project.stores.repositories.StoreRepository
import org.springframework.stereotype.Service

@Service
class StoreService(private val storeRepository: StoreRepository, private val contactRepository: ContactRepository) {
    fun createStore(store: StoreDto): StoreDto {
        if (storeRepository.findById(store.id).isPresent) return store

        val contactExist = contactRepository.findById(store.contact.id).isPresent

        val newContact = if (!contactExist) {
            val contact = store.contact.toEntity()
            // reset id for get a generated one
            contact.id = null
            contactRepository.save(contact).toDto()
        } else {
            store.contact
        }

        val storeEntity = store.toEntity()

        // we don't care about products here, so i reset that
        storeEntity.products = mutableListOf()

        storeEntity.contact = newContact.toEntity()

        return storeRepository.save(storeEntity).toDto()
    }

    fun getAllStores(): List<StoreDto> {
        return storeRepository.findAll().sortedBy { it.name }.map { it.toDto() }
    }

    fun getStoreById(id: String): StoreDto {
        val idAslong = id.toLongOrNull() ?: throw InvalidIdFormatException()

        return storeRepository.findById(idAslong).orElseThrow { StoreNotFoundException() }.toDto()
    }

    fun updateStore(id: String, storeUpdate: StoreDto): StoreDto {
        val idAslong = id.toLongOrNull() ?: throw InvalidIdFormatException()

        val store = storeRepository.findById(idAslong).orElseThrow { StoreNotFoundException() }

        store.name = storeUpdate.name

        // check if contact exist before
        val contact = contactRepository.findById(storeUpdate.contact.id).orElseThrow { ContactNotFoundException() }
        store.contact = contact

        return storeRepository.save(store).toDto()
    }

    fun deleteStore(id: String) {
        val idAslong = id.toLongOrNull() ?: throw InvalidIdFormatException()

        val store = storeRepository.findById(idAslong).orElseThrow { StoreNotFoundException() }

        storeRepository.delete(store)
    }

    fun addProductToStore(storeId: String, productId: String, quantity: Int): Product {
        val storeIdAslong = storeId.toLongOrNull() ?: throw InvalidIdFormatException()

        // TODO: implémenter la fonction
    }

    fun removeProductFromStore(storeId: String, productId: String, quantity: Int): Product {
        val storeIdAslong = storeId.toLongOrNull() ?: throw InvalidIdFormatException()

        // TODO: implémenter la fonction
    }

    fun removeProductsFromStore(storeId: String, productsToRemove: List<String>) {
        val storeIdAslong = storeId.toLongOrNull() ?: throw InvalidIdFormatException()

        // TODO: implémenter la fonction
    }
}