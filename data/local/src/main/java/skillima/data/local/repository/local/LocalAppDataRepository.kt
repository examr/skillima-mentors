package skillima.data.local.repository.local

import kotlinx.coroutines.flow.Flow
import skillima.data.local.model.AppDataConfig

interface LocalAppDataRepository {
    val getAppData: Flow<AppDataConfig>
    suspend fun setLoggedIn(value: Boolean)
    suspend fun setOnboardingComplete(type: String)
    suspend fun setGuildSelectionComplete(value: Boolean)
    suspend fun setProfileComplete(value: Boolean)
}