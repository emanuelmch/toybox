package bill.catbox.settings

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

private const val boxCountPreference = "pref_boxCount"

class SettingsRepository(context: Context) {

    private val watcher = PreferenceWatcher(context)

    fun watchBoxCount() = watcher.watchInt(boxCountPreference)
}

class PreferenceWatcher(private val sharedPrefs: SharedPreferences) {

    constructor(context: Context) : this(PreferenceManager.getDefaultSharedPreferences(context))

    fun watchInt(key: String, defaultValue: Int = 0): Observable<Int> =
            Observable
                    .create<Unit> { emitter ->
                        val callback = SharedPreferences.OnSharedPreferenceChangeListener { _, changedKey ->
                            if (changedKey == key) emitter.onNext(Unit)
                        }

                        emitter.setCancellable { sharedPrefs.unregisterOnSharedPreferenceChangeListener(callback) }
                        sharedPrefs.registerOnSharedPreferenceChangeListener(callback)
                    }
                    .startWith(Unit)
                    .map { sharedPrefs.getInt(key, defaultValue) }
                    .distinctUntilChanged()
}
