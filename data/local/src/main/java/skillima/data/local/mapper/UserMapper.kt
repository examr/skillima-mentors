package skillima.data.local.mapper

import skillima.mentors.module.UserData
import skillima.data.local.entity.UserEntity

fun UserData.toEntity(): UserEntity {
    return UserEntity(
        id = this.userId,
        name = this.name,
        email = this.email,
        role = this.role
    )
}