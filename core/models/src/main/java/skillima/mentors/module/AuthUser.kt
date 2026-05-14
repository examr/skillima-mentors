package skillima.mentors.module

data class AuthUser(
    val id: String,
    val email: String,
    val name: String,
    val profilePhoto: String,
    val bio: String,
    val githubUrl: String,
    val linkedinUrl: String,
    val xUrl: String,
    val fcmToken: String,
    val createdAt: String,
    val updatedAt: String
)