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

package bill.reactive

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class ProcessorTests {

    @Test
    fun `distinctUntilChanged publishes repeating emissions`() {
        Publishers.elements(1, 1, 2)
                .distinctUntilChanged()
                .test()
                .assertNoErrors()
                .assertValuesOnly(1, 2)
    }

    @Test
    fun `map publishes the value created by the mapping function`() {
        Publishers.elements(1)
                .map { it * 2 }
                .test()
                .assertNoErrors()
                .assertValuesOnly(2)
    }

    @Test
    fun `startWith publishes the initial value first`() {
        Publishers.elements(1)
                .startWith(0)
                .test()
                .assertNoErrors()
                .assertValuesOnly(0, 1)
    }

    @Test
    fun `doOnNext runs its function whenever something is published`() {
        var hasBeenCalled = false
        Publishers.elements(Unit)
                .doOnNext { hasBeenCalled = true }
                .subscribe()

        assertThat(hasBeenCalled, `is`(true))
    }


    @Test
    fun `doOnNext doesn't runs its function when nothing is published`() {
        var hasBeenCalled = false
        Publishers.elements<Unit>()
                .doOnNext { hasBeenCalled = true }
                .subscribe()

        assertThat(hasBeenCalled, `is`(false))
    }

    @Test
    fun `doOnFinish runs its function when the Publisher succeeds`() {
        var hasBeenCalled = false
        Publishers.elements(Unit)
                .doOnFinish { hasBeenCalled = true }
                .subscribe()

        assertThat(hasBeenCalled, `is`(true))
    }

    @Test
    fun `doOnFinish runs its function when the Publisher is cancelled`() {
        var hasBeenCalled = false
        Publishers.onSubscribe<Unit> { }
                .doOnFinish { hasBeenCalled = true }
                .subscribe()
                .cancel()

        assertThat(hasBeenCalled, `is`(true))
    }

    @Test
    fun `doOnFinish doesn't run its function when the Publisher is left open`() {
        var hasBeenCalled = false
        Publishers.onSubscribe<Unit> { }
                .doOnFinish { hasBeenCalled = true }
                .subscribe()

        assertThat(hasBeenCalled, `is`(false))
    }

}