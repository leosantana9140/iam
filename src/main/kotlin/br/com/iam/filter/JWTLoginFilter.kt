package br.com.iam.filter

import br.com.iam.config.JWTConfig
import br.com.iam.model.AuthenticationObject
import br.com.iam.model.Credential
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTLoginFilter(private val authManager: AuthenticationManager?, private val jwtConfig: JWTConfig): UsernamePasswordAuthenticationFilter() {
    private val expirationTime: Long = 300000

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication? {
        val(username, password) = ObjectMapper().readValue(request?.inputStream, Credential::class.java)
        val token = UsernamePasswordAuthenticationToken(username, password)

        return authManager?.authenticate(token)
    }

    override fun successfulAuthentication(request: HttpServletRequest?, response: HttpServletResponse?, chain: FilterChain?, authResult: Authentication?) {
        val user = (authResult?.principal as UserDetails)
        val token = jwtConfig.generateToken(user.username, user.authorities)

        val authObject = AuthenticationObject()
        authObject.accessToken = token
        authObject.expiresIn = expirationTime
        authObject.scope = user.authorities.map { grantedAuthority -> grantedAuthority.authority }

        val json = Gson().toJson(authObject)

        response?.contentType = "application/json"
        response?.characterEncoding = "UTF-8"
        response?.writer?.write(json)
        response?.writer?.flush()
    }
}
