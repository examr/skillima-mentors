package skillima.core.notifications.repository

interface NotificationTokenRepository {
    /** Called when FCM issues a new token; persist it as needed. */
    fun onTokenRefreshed(token: String)
}
