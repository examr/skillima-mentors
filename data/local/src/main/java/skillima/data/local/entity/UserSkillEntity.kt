package skillima.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_skills")
data class UserSkillEntity(
    @PrimaryKey val id: String,
    val name: String,
    val slug: String,
    @ColumnInfo(name = "icon_url") val iconUrl: String?
)