/*
 * Copyright (c) 2019 Emanuel Machado da Silva <emanuel.mch@gmail.com>
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

package bill.toybox.infra

import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import bill.reaktive.Publisher

private enum class Lifecycle { RESUME, PAUSE }

abstract class ObservableActivity : AppCompatActivity() {

    private val lifecycleObservers = mutableSetOf<Observer>()
    var onOptionsItemSelectedObserver: ((ObservableActivity, MenuItem) -> Boolean)? = null

    //region Activity overrides
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            onOptionsItemSelectedObserver?.invoke(this, item)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        lifecycleObservers.filter { it.lifecycle == Lifecycle.RESUME }
                .forEach(Observer::run)
    }

    override fun onPause() {
        super.onPause()
        lifecycleObservers.filter { it.lifecycle == Lifecycle.PAUSE }
                .forEach(Observer::run)
    }

    override fun onDestroy() {
        lifecycleObservers.clear()
        super.onDestroy()
    }
    //endregion Activity overrides

    fun doOnResume(action: ObservableActivity.() -> Unit) {
        lifecycleObservers.add(Observer(Lifecycle.RESUME, action))
    }

    fun <T> Publisher<T>.subscribeUntilPause() {
        val subscription = subscribe()
        lifecycleObservers += Observer(Lifecycle.PAUSE, subscription::cancel, oneTimeRun = true)
    }

    private inner class Observer(val lifecycle: Lifecycle,
                                 val action: ObservableActivity.() -> Unit,
                                 val oneTimeRun: Boolean = false) {

        constructor(lifecycle: Lifecycle, noArgsAction: () -> Unit, oneTimeRun: Boolean = false) :
                this(lifecycle, action = { noArgsAction.invoke() }, oneTimeRun = oneTimeRun)

        fun run() {
            action.invoke(this@ObservableActivity)
            if (oneTimeRun) lifecycleObservers -= this
        }
    }
}
