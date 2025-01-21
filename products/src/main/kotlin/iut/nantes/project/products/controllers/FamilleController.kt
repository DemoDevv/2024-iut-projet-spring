package iut.nantes.project.products.controllers

import iut.nantes.project.products.services.FamilleService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class FamilleController(private val familleService: FamilleService) {

    @GetMapping("/famille")
    fun getFamilleInfo(): String {
        return familleService.getFamilleData().toString()
    }
}