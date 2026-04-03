package skillima.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import skillima.data.local.dao.UserDao
import skillima.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RoleConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
