package skillima.mentors.utils

import skillima.mentors.supabase.SupabaseError

sealed interface Response<out T> {
    data object Loading : Response<Nothing>

    data class Error(val exception: SupabaseError) : Response<Nothing>

    data class Success<T>(val data: T) : Response<T>
}