package skillima.data.user

import io.github.jan.supabase.exceptions.BadRequestRestException
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.UnauthorizedRestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import skillima.mentors.utils.error.NetworkError

fun mapToDataError(error: Throwable): NetworkError {

    val e = (error as? Exception) ?: throw error

    return when (e) {
        is UnauthorizedRestException -> NetworkError.UserUnauthorized
        is HttpRequestException -> NetworkError.HttpRequestException
        is BadRequestRestException -> NetworkError.BadRequest
        is HttpRequestTimeoutException -> NetworkError.Timeout
        else -> NetworkError.UnknownError
    }


}