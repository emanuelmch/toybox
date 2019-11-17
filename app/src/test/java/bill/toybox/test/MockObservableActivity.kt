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

import bill.reaktive.Cancellable
import bill.reaktive.Publisher
import bill.toybox.infra.ObservableActivity
import io.mockk.every
import io.mockk.spyk
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class MockObservableActivity : ObservableActivity(), TestRule {

    private val publishers = mutableSetOf<Publisher<out Any>>()
    private val cancellables = mutableSetOf<Cancellable>()

    public override fun onResume() {
        publishers.forEach {
            cancellables += it.subscribe()
        }
    }

    fun mock_bind(publisher: Publisher<out Any>) {
        //FIXME: Check if we're already resumed
        publishers += publisher
    }

    override fun apply(base: Statement, description: Description?) = object : Statement() {
        override fun evaluate() {
            publishers.clear()
            cancellables.clear()

            base.evaluate()

            cancellables.forEach(Cancellable::cancel)
            publishers.clear()
            cancellables.clear()
        }
    }

    companion object {
        fun create(): MockObservableActivity {
            val activity = spyk(MockObservableActivity())

            every { activity.bind(any()) } answers { activity.mock_bind(firstArg()) }

            return activity
        }
    }
}
