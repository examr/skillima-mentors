package skillima.mentors

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
class SkillimaApp : Application(){
    override fun onCreate() {
        super.onCreate()

        GlobalContext.startKoin {
            modules(appModules)
            androidContext(this@SkillimaApp)
        }
    }
}