package skillima.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import skillima.mentors.module.Role

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val role: Role
)
