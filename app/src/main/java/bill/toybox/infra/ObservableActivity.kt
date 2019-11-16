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
import bill.reaktive.Cancellable
import bill.reaktive.Publisher

abstract class ObservableActivity : AppCompatActivity() {

    private val publishers = mutableSetOf<Publisher<out Any>>()
    private val cancellables = mutableSetOf<Cancellable>()
    var onOptionsItemSelectedObserver: ((ObservableActivity, MenuItem) -> Boolean)? = null

    //region Activity overrides
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onOptionsItemSelectedObserver?.invoke(this, item)

        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        publishers.forEach { cancellables += it.subscribe() }
    }

    override fun onPause() {
        super.onPause()
        cancellables.forEach(Cancellable::cancel)
        cancellables.clear()
    }

    //endregion Activity overrides

    fun bind(publisher: Publisher<out Any>) {
        //FIXME: Check if we're already resumed
        publishers += publisher
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun Publisher<out Any>.bindTo(activity: ObservableActivity) {
    activity.bind(this)
}
