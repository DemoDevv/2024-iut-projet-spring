package iut.nantes.project.stores.services

import iut.nantes.project.stores.controllers.dto.StoreDto
import iut.nantes.project.stores.repositories.StoreRepository
import org.springframework.stereotype.Service

@Service
class StoreService(private val storeRepository: StoreRepository) {
    fun createStore(store: StoreDto): StoreDto {

    }

    fun getAllStores(): List<StoreDto> {

    }

    fun getStoreById(id: String): StoreDto {

    }

    fun updateStore(id: String, storeUpdate: StoreDto): StoreDto {

    }

    fun deleteStore(id: String) {

    }
}