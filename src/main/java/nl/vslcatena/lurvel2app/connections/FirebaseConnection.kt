package nl.vslcatena.lurvel2app.connections

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.FirestoreOptions
import com.google.cloud.firestore.SetOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.UserRecord
import com.google.firebase.cloud.FirestoreClient
import nl.vslcatena.lurvel2app.models.Committee
import nl.vslcatena.lurvel2app.utils.FileUtils
import nl.vslcatena.lurvel2app.models.User
import nl.vslcatena.lurvel2app.utils.Env

object FirebaseConnection {
    private val firestore: Firestore

    init {

        val file = FileUtils.fromExternal("serviceAccountKey.json")

        // If an .env file doesn't exist, create one using the .env.example file in our resource folder
        if (!file.exists()) {
            file.createNewFile()
            file.writeText(
                FileUtils.inputFromResources("serviceAccountKey.json.example")
                    ?.bufferedReader()
                    ?.readText()
                    ?: ""
            )
        }
        val serviceAccount = file.inputStream()
        val credentials = GoogleCredentials.fromStream(serviceAccount)

        val options = FirebaseOptions.builder()
            .setCredentials(credentials)
            .setDatabaseUrl(Env.FIREBASE_URL)
            .build()

        FirebaseApp.initializeApp(options)


        firestore = FirestoreClient.getFirestore()
    }

    fun updateCommittees() {
        val committeeCollection = FirestoreClient.getFirestore().collection("committees")
        // If we got a null, something went wrong, we cancel the update
        val committees = LurvelConnection.getAllCommittees() ?: return

        // We start with the list of committees, and if it exists in firebase we remove it from this list
        val committeesToAdd: MutableList<Committee> = ArrayList(committees)

        // We start with an empty list, and if a firebase result isn't in our list of committees, we remove it later
        val committeesToRemove: MutableList<String> = ArrayList()

        // We grab all the committees from firebase
        val fbCommittees = committeeCollection.get().get().documents
        fbCommittees.forEach { snapshot ->
            val committeeId = snapshot.getString("id")

            // If the ID couldn't be retrieved, then we can just delete it
            if (committeeId == null) {
                snapshot.reference.delete()
                return@forEach
            }

            // If the committee from firebase doesn't match any of the committees from our AD, we want to remove it
            // from firebase
            if (committees.none { it.id == committeeId }) {
                committeesToRemove.add(snapshot.id)
                return@forEach
            }

            // Here we know it does exist in firebase already, so we can remove it from committeesToAdd
            committeesToAdd.removeIf { it.id == committeeId }
        }

        // Add all new committees
        committeesToAdd.forEach {
            committeeCollection.document(it.id).set(it)
        }

        // Remove all old committees
        committeesToRemove.forEach {
            committeeCollection.document(it).delete()
        }
    }

    fun createToken(user: User): String {
        return FirebaseAuth.getInstance().createCustomToken(user.id)
    }

    fun updateUserData(user: User) {
        firestore.document("users/${user.id}").set(mapOf(
            "name" to user.name,
            "memberNumber" to user.memberNumber,
            "committees" to user.committees?.toList()
        ), SetOptions.merge()).get()
    }

    fun updateUserCommittees(userId: String, committees: Array<String>) {
        firestore.document("users/${userId}").set(mapOf(
            "committees" to committees.toList()
        ), SetOptions.merge()).get()
    }
}