package skillima.data.local.repository.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import skillima.data.local.model.AppDataConfig
import skillima.mentors.datastore.DatastoreHelper
import skillima.mentors.utils.EnableObserver
import skillima.mentors.utils.NotificationObserver

class LocalAppDataRepositoryImpl(
    val datastoreHelper: DatastoreHelper,
    val notificationObserver: NotificationObserver,
) : LocalAppDataRepository {
    override val getAppData: Flow<AppDataConfig>
        get() = combine(
            datastoreHelper.isLoggedIn,
            datastoreHelper.isOnboardingCompleteFlow,
            notificationObserver.observeNotification(),
            datastoreHelper.isGuildSelectionCompleted
        ) { isLoggedIn, isOnboardingComplete, notificationStatus, isGuildCompleted ->
            AppDataConfig(
                loggedIn = isLoggedIn,
                notificationEnabled = when (notificationStatus) {
                    EnableObserver.NotificationStatus.ENABLE -> true
                    EnableObserver.NotificationStatus.DISABLE -> false
                },
                firstTime = !isOnboardingComplete,
                isGuildSelected = isGuildCompleted
            )
        }

    override suspend fun setOnboardingComplete(type: String) {
        datastoreHelper.setOnboardingComplete(type)
    }

    override suspend fun setLoggedIn(value: Boolean) {
        datastoreHelper.saveLoggedIn(value)
    }

}