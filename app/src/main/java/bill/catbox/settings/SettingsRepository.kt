/*
 * Copyright (c) 2018 Emanuel Machado da Silva <emanuel.mch@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package bill.catbox.settings

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import bill.reaktive.Publisher
import bill.reaktive.Publishers

private const val boxCountPreference = "pref_boxCount"

open class SettingsRepository(context: Context) {

    private val watcher = PreferenceWatcher(context)

    open fun watchBoxCount() = watcher.watchInt(boxCountPreference)
}

class PreferenceWatcher(private val sharedPrefs: SharedPreferences) {

    constructor(context: Context) : this(PreferenceManager.getDefaultSharedPreferences(context))

    fun watchInt(key: String, defaultValue: Int = 0): Publisher<Int> {
        lateinit var callback: SharedPreferences.OnSharedPreferenceChangeListener
        return Publishers
                .onSubscribe<Unit> { subscriber ->
                    callback = SharedPreferences.OnSharedPreferenceChangeListener { _, changedKey ->
                        if (changedKey == key) subscriber.onNext(Unit)
                    }

                    sharedPrefs.registerOnSharedPreferenceChangeListener(callback)
                }
                .doOnFinish { sharedPrefs.unregisterOnSharedPreferenceChangeListener(callback) }
                .startWith(Unit)
                .map { sharedPrefs.getInt(key, defaultValue) }
                .distinctUntilChanged()
    }
}
