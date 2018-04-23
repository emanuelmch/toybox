package bill.catbox

import android.app.Application
import android.preference.PreferenceManager
import timber.log.Timber

class CatBoxApplication() : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
    }
}
