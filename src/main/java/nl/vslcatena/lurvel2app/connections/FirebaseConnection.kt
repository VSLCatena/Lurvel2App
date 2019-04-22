package nl.vslcatena.lurvel2app.connections

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.UserRecord
import com.google.firebase.cloud.FirestoreClient
import nl.vslcatena.lurvel2app.FileUtils
import nl.vslcatena.lurvel2app.models.User

object FirebaseConnection {
    init {
        val serviceAccount = FileUtils.fromExternal("serviceAccountKey.json").inputStream()

        val options = FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setDatabaseUrl("https://vsl-catena.firebaseio.com/")
            .build()

        FirebaseApp.initializeApp(options)
    }

    fun createToken(user: User): String {
        FirestoreClient.getFirestore().document("users/${user.id}").set(mapOf(
            "name" to user.name,
            "lidnummer" to user.id
        )).get()

        return FirebaseAuth.getInstance().createCustomToken(user.id)
    }
}