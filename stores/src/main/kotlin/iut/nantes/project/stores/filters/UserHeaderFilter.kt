package iut.nantes.project.stores.filters

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component

@Component
class UserHeaderFilter : Filter {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val req = request as HttpServletRequest
        val res = response as HttpServletResponse

        val user = req.getHeader("X-User")
        if (user.isNullOrBlank()) {
            res.status = HttpServletResponse.SC_FORBIDDEN
            res.writer.write("Forbidden: Missing X-User header")
            return
        }

        chain.doFilter(request, response)
    }
}