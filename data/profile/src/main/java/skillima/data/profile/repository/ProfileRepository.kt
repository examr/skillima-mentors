package skillima.data.profile.repository

import skillima.data.profile.model.CurrentMentorProfile
import skillima.mentors.utils.Response

/**
 * All mentor-profile related remote operations live here.
 * The [HomeViewModel] uses this instead of hitting Supabase directly.
 */
interface ProfileRepository {
    /**
     * Loads the current logged-in mentor profile for Home.
     * Combines the locally cached user with the latest mentor verification status.
     */
    suspend fun getCurrentMentorProfile(): Response<CurrentMentorProfile>

    /**
     * Fetches the [verificationStatus] for the given [userId] from the `mentor_profiles` table.
     * Returns one of: "pending", "approved", "rejected", or null if no row exists yet.
     */
    suspend fun getMentorVerificationStatus(userId: String): Response<String?>
}
