package iut.nantes.project.products.repositoriesTests

import iut.nantes.project.products.controllers.entities.FamilleEntity
import iut.nantes.project.products.repositories.FamilleRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.util.UUID.randomUUID

@DataJpaTest
class FamilleRepositoryTest {
    @Autowired
    private lateinit var jpaRepository: FamilleRepository

    @Test
    fun `find one existing`() {
        jpaRepository.save(FamilleEntity(randomUUID().toString(), "name", "description"))

        val result = jpaRepository.findAll()

        assertThat(result).hasSize(1)
    }
}