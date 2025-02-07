package iut.nantes.project.gateway

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import javax.sql.DataSource

@TestConfiguration
class TestConfiguration {
    @Bean
    fun dataSource(): DataSource {
        val dataSource = org.springframework.jdbc.datasource.DriverManagerDataSource()
        dataSource.setDriverClassName("org.h2.Driver")
        dataSource.url = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1"
        dataSource.username = "sa"
        dataSource.password = "password"
        return dataSource
    }
}