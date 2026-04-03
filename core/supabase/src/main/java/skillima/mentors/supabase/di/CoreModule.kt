package skillima.mentors.supabase.di

import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import org.koin.dsl.module
import skillima.mentors.supabase.SupabaseConstants
import java.net.UnknownHostException

@OptIn(SupabaseInternal::class)
var supabaseModule = module {
    single {
        createSupabaseClient(
            supabaseUrl = SupabaseConstants.SUPABASE_URL,
            supabaseKey = SupabaseConstants.SUPABASE_KEY
        ) {
            install(Auth)
            install(Postgrest)
        }
    }
}