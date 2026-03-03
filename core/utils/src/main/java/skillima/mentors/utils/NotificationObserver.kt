package skillima.mentors.utils

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class NotificationObserver(
    private val context: Context
) : EnableObserver {

    private val notificationManager = NotificationManagerCompat.from(context)

    override fun observeNotification(): Flow<EnableObserver.NotificationStatus> {
        return callbackFlow {
            val initialStatus = if (notificationManager.areNotificationsEnabled()) {
                EnableObserver.NotificationStatus.ENABLE
            } else {
                EnableObserver.NotificationStatus.DISABLE
            }
            trySend(initialStatus).isSuccess

            // Register a broadcast receiver to listen for changes in notification settings
            val receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    val status = if (notificationManager.areNotificationsEnabled()) {
                        EnableObserver.NotificationStatus.ENABLE
                    } else {
                        EnableObserver.NotificationStatus.DISABLE
                    }
                    trySend(status).isSuccess
                }
            }

            val intentFilter =
                IntentFilter(NotificationManager.ACTION_NOTIFICATION_POLICY_ACCESS_GRANTED_CHANGED)
            context.registerReceiver(receiver, intentFilter)

            awaitClose {
                context.unregisterReceiver(receiver)
            }
        }.distinctUntilChanged()
    }
}

interface EnableObserver {
    fun observeNotification(): Flow<NotificationStatus>

    enum class NotificationStatus {
        ENABLE, DISABLE
    }
}