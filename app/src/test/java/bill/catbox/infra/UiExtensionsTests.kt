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

package bill.catbox.infra

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

//TODO: Replace all of this with JUnit5 parameterized tests
@RunWith(Parameterized::class)
class `Ui Extensions - To Ordinal - Numbers ending in st`(val number: Int) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0} -> {0}st")
        fun data() = listOf(1, 21, 91, 1001)
    }

    @Test
    fun test() {
        val expected = number.toString() + "st"
        assertThat(number.toOrdinal(), `is`(equalTo(expected)))
    }
}

@RunWith(Parameterized::class)
class `Ui Extensions - To Ordinal - Numbers ending in nd`(val number: Int) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0} -> {0}st")
        fun data() = listOf(2, 22, 92, 1002)
    }

    @Test
    fun test() {
        val expected = number.toString() + "nd"
        assertThat(number.toOrdinal(), `is`(equalTo(expected)))
    }
}

@RunWith(Parameterized::class)
class `Ui Extensions - To Ordinal - Numbers ending in rd`(val number: Int) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0} -> {0}st")
        fun data() = listOf(3, 23, 93, 1003)
    }

    @Test
    fun test() {
        val expected = number.toString() + "rd"
        assertThat(number.toOrdinal(), `is`(equalTo(expected)))
    }
}

@RunWith(Parameterized::class)
class `Ui Extensions - To Ordinal - Numbers ending in th`(val number: Int) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0} -> {0}st")
        fun data() = listOf(4, 10, 11, 12, 13, 34, 94, 1000, 1004)
    }

    @Test
    fun test() {
        val expected = number.toString() + "th"
        assertThat(number.toOrdinal(), `is`(equalTo(expected)))
    }
}
