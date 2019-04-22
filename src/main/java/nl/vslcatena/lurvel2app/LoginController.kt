package nl.vslcatena.lurvel2app

import nl.vslcatena.lurvel2app.connections.FirebaseConnection
import nl.vslcatena.lurvel2app.connections.LurvelConnection
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController {

    @PostMapping("/login")
    fun login(
        @RequestParam(value = "username")
        username: String,
        @RequestParam(value = "password")
        password: String
    ): Any {

        val user = LurvelConnection.getUser(username, password) ?: return "Invalid credentials"

        val token = FirebaseConnection.createToken(user)

        return mapOf(
            "token" to token,
            "user" to user
        )
    }
}