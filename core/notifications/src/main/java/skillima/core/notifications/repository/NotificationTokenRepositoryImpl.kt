package skillima.core.notifications.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

private const val PREFS_NAME = "skillima_fcm_prefs"
private const val KEY_FCM_TOKEN = "fcm_token"

class NotificationTokenRepositoryImpl(
    context: Context
) : NotificationTokenRepository {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun onTokenRefreshed(token: String) {
        Log.d("FCM", "New token: $token")
        prefs.edit().putString(KEY_FCM_TOKEN, token).apply()
        // TODO: send the token to Supabase / your backend so you can
        //       target push notifications to this device.
    }

    fun getSavedToken(): String? = prefs.getString(KEY_FCM_TOKEN, null)
}
