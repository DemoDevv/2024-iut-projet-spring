package iut.nantes.project.products.configs

import iut.nantes.project.products.filters.UserHeaderFilter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterConfig {
    @Bean
    fun userHeaderFilterRegistration(): FilterRegistrationBean<UserHeaderFilter> {
        val registrationBean = FilterRegistrationBean(UserHeaderFilter())
        registrationBean.addUrlPatterns("/api/*")
        registrationBean.order = 1
        return registrationBean
    }
}