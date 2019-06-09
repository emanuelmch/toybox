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

package bill.toybox.test

import bill.reaktive.Publisher
import bill.toybox.infra.ObservableActivity
import io.mockk.every
import io.mockk.spyk
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class MockObservableActivity : ObservableActivity(), TestRule {

    private val resumeListeners = mutableSetOf<ObservableActivity.() -> Unit>()
    private val pauseOnceListeners = mutableSetOf<() -> Unit>()

    public override fun onResume() {
        resumeListeners.forEach { it.invoke(this) }
    }

    public override fun onPause() {
        pauseOnceListeners.forEach { it.invoke() }
        pauseOnceListeners.clear()
    }

    override fun apply(base: Statement, description: Description?) = object : Statement() {
        override fun evaluate() {
            resumeListeners.clear()
            pauseOnceListeners.clear()

            base.evaluate()

            onPause()
        }
    }

    companion object {
        fun create(): MockObservableActivity {
            val activity = spyk(MockObservableActivity())

            every { activity.doOnResume(any()) } answers { activity.resumeListeners.add(firstArg()) }
            every { activity["subscribeUntilPause"](any<Publisher<Any>>()) } answers {
                val subscription = firstArg<Publisher<Any>>().subscribe()
                activity.pauseOnceListeners.add(subscription::cancel)
            }

            return activity
        }
    }
}
