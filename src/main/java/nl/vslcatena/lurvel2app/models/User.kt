package nl.vslcatena.lurvel2app.models

data class User(
    val id: String,
    val description: String?,
    val name: String,
    val phoneNumber: String?,
    val email: String?,
    val committees: List<Committee>
)