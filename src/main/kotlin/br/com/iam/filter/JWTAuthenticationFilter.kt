package br.com.iam.filter

import br.com.iam.config.JWTConfig
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthenticationFilter(private val jwtConfig: JWTConfig): OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val header = request.getHeader("Authorization")
        val jwt = getJwt(header)

        if (jwtConfig.checkIfJwtIsValid(jwt)) {
            val authentication = jwtConfig.getAuthentication(jwt)
            SecurityContextHolder.getContext().authentication = authentication
        }

        filterChain.doFilter(request, response)
    }

    private fun getJwt(token: String?): String? {
        return token?.let {
                jwt -> jwt.startsWith("Bearer ")
            jwt.substring(7, jwt.length)
        }
    }
}
