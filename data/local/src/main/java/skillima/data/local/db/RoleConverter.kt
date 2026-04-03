package skillima.data.local.db

import androidx.room.TypeConverter
import skillima.mentors.module.Role

class RoleConverter {

    @TypeConverter
    fun fromRole(role: Role): String = role.name

    @TypeConverter
    fun toRole(value: String): Role = Role.valueOf(value)
}
