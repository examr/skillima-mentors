package skillima.data.local.model

data class AppDataConfig(
    val loggedIn: Boolean,
    val notificationEnabled: Boolean,
    val firstTime: Boolean,
    val isGuildSelected: Boolean,
    val isProfileComplete: Boolean = false,
)