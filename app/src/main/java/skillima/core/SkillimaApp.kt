package skillima.core

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import skillima.core.supabase.di.coreModule
import skillima.data.auth.di.authDataModule
import skillima.screens.auth.di.authPresentationModule

class SkillimaApp : Application(){
    override fun onCreate() {
        super.onCreate()

        GlobalContext.startKoin {
            modules(authPresentationModule, coreModule, authDataModule)
            androidContext(this@SkillimaApp)
        }
    }
}