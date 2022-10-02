package br.com.iam.service

import br.com.iam.model.User
import br.com.iam.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository): UserDetailsService {
    fun getUserById(id: Long): User {
        return userRepository.getOne(id)
    }

    override fun loadUserByUsername(username: String?): UserDetails {
        val user = userRepository.findByEmail(username)?: throw RuntimeException()

        return UserDetailService(user)
    }
}
