package iut.nantes.project.stores.controllers

import iut.nantes.project.stores.services.StoreService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/stores")
@Validated
class StoreController(private val storeService: StoreService) {
}