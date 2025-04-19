import kotlinx.serialization.Serializable

@Serializable
data class Usersignup(val email: String, val password: String, val otp: String)
