package skillima.data.guild.error

import skillima.data.guild.R.string as str
import skillima.mentors.supabase.SupabaseError

sealed class NetworkError(
    override val error: Int,
) : SupabaseError {

    data object Timeout : NetworkError(
        str.request_timeout
    )

    data object UnknownError : NetworkError(
        str.unknown_error
    )

    data object BadRequest : NetworkError(
        str.bad_request
    )

    data object UserUnauthorized : NetworkError(
        str.unauthorized
    )



    data object HttpRequestException : NetworkError(
        str.http_request_exception
    )

    data object RateLimit : NetworkError(
        str.ratelimit
    )

}