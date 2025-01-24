package iut.nantes.project.stores.services

import iut.nantes.project.stores.repositories.StoreRepository
import org.springframework.stereotype.Service

@Service
class StoreService(private val storeRepository: StoreRepository) {
}