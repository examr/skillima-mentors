package skillima.data.profile.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO for the `mentor_profiles` table.
 * Only maps the columns we need to read at runtime.
 */
@Serializable
data class MentorProfileDto(
    @SerialName("verification_status") val verificationStatus: String? = null,
)
