package iut.nantes.project.stores.services

import iut.nantes.project.stores.controllers.dto.Product
import iut.nantes.project.stores.controllers.dto.StoreDto
import iut.nantes.project.stores.exceptions.*
import iut.nantes.project.stores.repositories.ContactRepository
import iut.nantes.project.stores.repositories.StoreRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class StoreService(
    private val storeRepository: StoreRepository,
    private val contactRepository: ContactRepository,
    private val webClient: WebClient
) {
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

    // il faut impérativement testé cette méthode car je ne suis pas du tout sûr de son fonctionnement
    fun addProductToStore(storeId: String, productId: String, quantity: Int): Product {
        val storeIdAslong = storeId.toLongOrNull() ?: throw InvalidIdFormatException()

        val store = storeRepository.findById(storeIdAslong).orElseThrow { StoreNotFoundException() }

        val productInStore = store.products.find { it.id == productId } ?: run {
            // Demander au service products si le produit existe et l'ajouter
            val product = webClient.get().uri("/api/v1/products/{id}", productId).retrieve()
                .onStatus({ status -> status != HttpStatus.OK }) { _ ->
                    // Gérer les statuts autres que 200
                    Mono.error(InvalidRequestParameters())
                }.bodyToMono(Product::class.java).block()

            store.products.add(product!!)

            return product
        }

        productInStore.quantity += quantity

        storeRepository.save(store)

        return productInStore
    }

    fun removeProductFromStore(storeId: String, productId: String, quantity: Int): Product {
        val storeIdAslong = storeId.toLongOrNull() ?: throw InvalidIdFormatException()

        val store = storeRepository.findById(storeIdAslong).orElseThrow { StoreNotFoundException() }

        val productInStore = store.products.find { it.id == productId } ?: throw ProductNotPresentInStoreException()

        productInStore.quantity -= quantity

        if (productInStore.quantity < 0) throw ExcessiveProductRemovalException()

        storeRepository.save(store)

        return productInStore
    }

    fun removeProductsFromStore(storeId: String, productsToRemove: List<String>) {
        val storeIdAslong = storeId.toLongOrNull() ?: throw InvalidIdFormatException()

        val store = storeRepository.findById(storeIdAslong).orElseThrow { StoreNotFoundException() }

        store.products.removeAll { productsToRemove.contains(it.id) }
    }
}