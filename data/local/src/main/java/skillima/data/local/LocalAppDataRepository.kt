package skillima.data.local

import kotlinx.coroutines.flow.Flow
import skillima.data.local.model.AppDataConfig

interface LocalAppDataRepository {
    val getAppData : Flow<AppDataConfig>
    suspend fun setOnboardingComplete(type: String)
}