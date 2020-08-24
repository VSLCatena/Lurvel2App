package nl.vslcatena.lurvel2app

import nl.vslcatena.lurvel2app.connections.FirebaseConnection
import nl.vslcatena.lurvel2app.connections.LurvelConnection
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import retrofit2.http.PATCH

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

        FirebaseConnection.updateUserData(user)
        val token = FirebaseConnection.createToken(user)

        return mapOf(
            "token" to token,
            "user" to user
        )
    }

    @PATCH("/update")
    fun update(
        @RequestParam(value = "userId")
        userId: String
    ): Any? {
        // userId must be hexadecimal
        if (!userId.matches(Regex("^[0-9a-fA-F]+$")))
            return "Something went wrong"

        // Get the committees from Lurvel
        val committees = LurvelConnection.getUserCommittees(userId) ?: return "Something went wrong"

        // Update the committees
        FirebaseConnection.updateUserCommittees(userId, committees)

        return null
    }
}