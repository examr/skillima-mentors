package skillima.core.supabase

import androidx.annotation.StringRes

interface SupabaseError {
   @get:StringRes
   val error: Int
}