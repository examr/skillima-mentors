package skillima.data.auth.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO for the `mentor_profiles` table row.
 * Used only to read the [verificationStatus] field on the Home screen.
 */
@Serializable
data class MentorProfileDto(
    @SerialName("verification_status") val verificationStatus: String? = null,
)
