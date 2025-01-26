package iut.nantes.project.stores.integrationTests

import iut.nantes.project.stores.services.StoreService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class StoreServiceIntegrationTest {

    @Autowired
    private lateinit var storeService: StoreService
}
