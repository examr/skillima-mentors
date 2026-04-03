package skillima.data.local.repository

import kotlinx.coroutines.flow.Flow
import skillima.data.local.model.AppDataConfig

interface LocalAppDataRepository {
    val getAppData : Flow<AppDataConfig>
    suspend fun setLoggedIn(value: Boolean)

    suspend fun setOnboardingComplete(type: String)
}