package skillima.data.profile.repository

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.first
import skillima.data.local.repository.user.UserLocalRepository
import skillima.data.profile.error.ProfileError
import skillima.data.profile.model.CurrentMentorProfile
import skillima.data.profile.model.MentorProfileDto
import skillima.mentors.supabase.SupabaseConstants
import skillima.mentors.utils.Response

class ProfileRepositoryImpl(
    private val supabaseClient: SupabaseClient,
    private val userLocalRepository: UserLocalRepository,
) : ProfileRepository {

    override suspend fun getCurrentMentorProfile(): Response<CurrentMentorProfile> {
        val user = userLocalRepository.observeUser().first()
            ?: return Response.Error(ProfileError.UserNotFound)

        return when (val statusResult = getMentorVerificationStatus(user.id)) {
            is Response.Success -> Response.Success(
                CurrentMentorProfile(
                    name = user.name,
                    email = user.email,
                    verificationStatus = statusResult.data,
                )
            )

            is Response.Error -> statusResult
            Response.Loading -> Response.Error(ProfileError.Unknown)
        }
    }

    override suspend fun getMentorVerificationStatus(userId: String): Response<String?> {
        return runCatching {
            supabaseClient
                .from(SupabaseConstants.MENTOR_PROFILES)
                .select {
                    filter { eq("user_id", userId) }
                    limit(1)
                }
                .decodeSingleOrNull<MentorProfileDto>()
                ?.verificationStatus
        }.fold(
            onSuccess = { status -> Response.Success(status) },
            onFailure = { e ->
                Log.e("ProfileRepository", "getMentorVerificationStatus failed: ${e.message}")
                Response.Error(ProfileError.Unknown)
            }
        )
    }
}
