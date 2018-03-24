package bill.catbox

import android.app.Application
import timber.log.Timber

class CatBoxApplication() : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
