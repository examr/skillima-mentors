package skillima.core.supabase.di

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import org.koin.dsl.module

var coreModule = module {
    single{
        createSupabaseClient(
            supabaseUrl = "https://svvtcqwwmmvyqgxgaklu.supabase.co" ,
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InN2dnRjcXd3bW12eXFneGdha2x1Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjUyOTQ1MTksImV4cCI6MjA4MDg3MDUxOX0.ILhkzhoCH0j93Akr8usQvg1e201SVf2Sp-ism1yLNvM"
        ){
            install(Auth)
            install(Postgrest)
        }
    }

}