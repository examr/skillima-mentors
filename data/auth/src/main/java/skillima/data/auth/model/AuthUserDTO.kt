package skillima.data.auth.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import skillima.mentors.module.AuthUser

@Serializable
data class AuthUserDTO(
    @SerialName("id") val id : String?,
    @SerialName("email") val email : String?,
    @SerialName("full_name") val name : String?,
    @SerialName("avatar_url") val profilePhoto : String?,
    @SerialName("bio") val bio : String?,
    @SerialName("github_url") val githubUrl : String?,
    @SerialName("linkedin_url") val linkedinUrl : String?,
    @SerialName("x_url") val xUrl : String?,
    @SerialName("fcm_token") val fcmToken : String?,
    @SerialName("created_at") val createdAt : String?,
    @SerialName("updated_at") val updatedAt : String?
)

fun AuthUserDTO.toAuthUser() = AuthUser(
    id = id ?: "",
    email = email ?: "",
    name = name ?: "",
    profilePhoto = profilePhoto ?: "",
    bio = bio ?: "",
    githubUrl = githubUrl ?: "",
    linkedinUrl = linkedinUrl ?: "",
    xUrl = xUrl ?: "",
    fcmToken = fcmToken ?: "",
    createdAt = createdAt ?: "",
    updatedAt = updatedAt ?: ""
)