package br.com.iam.model

class AuthenticationObject {
    var accessToken: String? = ""
    val tokenType: String = "Bearer"
    var expiresIn: Long = 0
    var scope: List<String>? = ArrayList()
}
