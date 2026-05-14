package skillima.data.profile.error

import skillima.data.profile.R.string as str
import skillima.mentors.supabase.SupabaseError

sealed class ProfileError(override val error: Int) : SupabaseError {
    data object Unknown : ProfileError(str.profile_error_unknown)
    data object UserNotFound : ProfileError(str.profile_error_user_not_found)
}
