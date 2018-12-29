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

package bill.catbox.home

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.android.synthetic.main.home_view.view.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class HomeViewTests {

    @Test
    fun `should attach an Adapter with the correct box count to the RecyclerView`() {
        val boxes: RecyclerView = mockk()
        val context: Context = mockk()
        val rootView: ViewGroup = mockk(relaxed = true)

        var adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>? = null

        every { boxes.adapter } answers { adapter }

        every { rootView.context } returns context
        every { rootView.boxes } returns boxes

        every {
            boxes.adapter = any()
        } propertyType RecyclerView.Adapter::class answers {
            val spy = spyk(value as BoxAdapter)

            var boxCount = 0
            every { spy.boxCount } answers { boxCount }
            every {
                spy.boxCount = any()
            } propertyType Int::class answers {
                boxCount = value
            }

            adapter = spy
        }

        val view = HomeView(rootView)
        view.startGame(3)

        assertThat(adapter, `is`(notNullValue()))
        assertThat(adapter!!.itemCount, `is`(3))
    }
}
