package skillima.data.local.mapper

import skillima.data.local.entity.UserEntity
import skillima.mentors.module.AuthUser
import skillima.mentors.module.Role

fun AuthUser.toEntity(): UserEntity {
    return UserEntity(
        id = this.id,
        name = this.name,
        email = this.email,
        role = Role.Mentor,
        profilePhoto = this.profilePhoto,
        bio = this.bio,
        githubUrl = this.githubUrl,
        linkedinUrl = this.linkedinUrl,
        xUrl = this.xUrl,
        fcmToken = this.fcmToken,
    )
}