package iut.nantes.project.stores.configs

import iut.nantes.project.stores.filters.UserHeaderFilter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterConfig {
    @Bean
    fun userHeaderFilterRegistration(filter: UserHeaderFilter): FilterRegistrationBean<UserHeaderFilter> {
        val registrationBean = FilterRegistrationBean(filter)
        registrationBean.addUrlPatterns("/api/*")
        registrationBean.order = 1
        return registrationBean
    }
}