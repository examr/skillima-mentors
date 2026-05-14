package skillima.mentors.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DatastoreHelper(private val context: Context) {

    companion object {
        private val Context.userPreferences: DataStore<Preferences> by preferencesDataStore("user")


        private object SessionKeys {
            val IS_LOGGED_IN = booleanPreferencesKey("IS_LOGGED_IN")
            val LOGIN_TIMESTAMP = stringPreferencesKey("LOGIN_TIMESTAMP")
            val APP_THEME = stringPreferencesKey("APP_THEME")
            val IS_ONBOARDING_COMPLETE = booleanPreferencesKey("IS_ONBOARDING_COMPLETE")
            val IS_GUILD_SELECTION_COMPLETE = booleanPreferencesKey("IS_GUILD_SELECTION_COMPLETE")
            val IS_PROFILE_COMPLETE = booleanPreferencesKey("IS_PROFILE_COMPLETE")
            val ONBOARDING_ACCOUNT_TYPE =
                stringPreferencesKey("ONBOARDING_ACCOUNT_TYPE")
        }


    }


    val isLoggedIn: Flow<Boolean> =
        context.userPreferences.data.map { it[SessionKeys.IS_LOGGED_IN] ?: false }


    val loginTimestampFlow: Flow<String> =
        context.userPreferences.data.map { it[SessionKeys.LOGIN_TIMESTAMP] ?: "" }


    val themeFlow: Flow<String> =
        context.userPreferences.data.map { it[SessionKeys.APP_THEME] ?: "FOLLOW_SYSTEM" }


    val isOnboardingCompleteFlow : Flow<Boolean> = context.userPreferences.data.map { it[SessionKeys.IS_ONBOARDING_COMPLETE] == true }
    val isGuildSelectionCompleted : Flow<Boolean> = context.userPreferences.data.map { it[SessionKeys.IS_GUILD_SELECTION_COMPLETE] == true }
    val isProfileComplete : Flow<Boolean> = context.userPreferences.data.map { it[SessionKeys.IS_PROFILE_COMPLETE] == true }

    val onboardingAccountTypeFlow : Flow<String> = context.userPreferences.data.map { it[SessionKeys.ONBOARDING_ACCOUNT_TYPE] ?: "GUEST" }


    suspend fun saveLoggedIn(isUserLoggedIn: Boolean) {
        context.userPreferences.edit { preferences ->
            preferences[SessionKeys.IS_LOGGED_IN] = isUserLoggedIn
        }
    }

    suspend fun saveTheme(themeFlow: String) {
        context.userPreferences.edit { preferences ->
            preferences[SessionKeys.APP_THEME] = themeFlow
        }
    }

    suspend fun setOnboardingComplete(type: String) {
        context.userPreferences.edit { preferences ->
            preferences[SessionKeys.IS_ONBOARDING_COMPLETE] = true
            preferences[SessionKeys.ONBOARDING_ACCOUNT_TYPE] = type

        }
    }

    suspend fun updateAccountType(type: String) {
        context.userPreferences.edit { preferences ->
            preferences[SessionKeys.ONBOARDING_ACCOUNT_TYPE] = type
        }
    }

    suspend fun setGuildSelectionComplete(value: Boolean) {
        context.userPreferences.edit { preferences ->
            preferences[SessionKeys.IS_GUILD_SELECTION_COMPLETE] = value
        }
    }

    suspend fun setProfileComplete(value: Boolean) {
        context.userPreferences.edit { preferences ->
            preferences[SessionKeys.IS_PROFILE_COMPLETE] = value
        }
    }
}