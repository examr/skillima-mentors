package skillima.data.local.utils

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object AppDatabaseMigrations {

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS user_skills (
                    id TEXT NOT NULL PRIMARY KEY,
                    name TEXT NOT NULL,
                    slug TEXT NOT NULL,
                    icon_url TEXT
                )
            """)
        }
    }

    // Next time you add a table/column, just add here:
    // val MIGRATION_2_3 = object : Migration(2, 3) { ... }

    val ALL = arrayOf(MIGRATION_1_2)
}