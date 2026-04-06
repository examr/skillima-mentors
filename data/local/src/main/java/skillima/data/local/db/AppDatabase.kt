package skillima.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import skillima.data.local.dao.UserDao
import skillima.data.local.dao.UserSkillDao
import skillima.data.local.entity.UserEntity
import skillima.data.local.entity.UserSkillEntity

@Database(
    entities = [UserEntity::class, UserSkillEntity::class],
    version = 2,
    exportSchema = false

)
@TypeConverters(RoleConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun userSkillDao(): UserSkillDao

}
