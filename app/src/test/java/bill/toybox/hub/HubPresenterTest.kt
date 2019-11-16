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

package bill.toybox.hub

import bill.reaktive.Publishers
import bill.toybox.catbox.home.HomeActivity
import bill.toybox.test.MockObservableActivity
import bill.toybox.test.ReactiveTestRule
import io.mockk.*
import org.junit.*

class HubPresenterTest {

    @get:Rule
    val reactiveTestRule = ReactiveTestRule()

    @get:Rule
    val activity = MockObservableActivity.create()

    private lateinit var view: HubView

    private lateinit var presenter: HubPresenter

    @Before
    fun before() {
        view = mockk(relaxed = true)

        presenter = HubPresenter(view)
    }

    @Test
    fun `should navigate to Catbox's activity when the image is clicked`() {
        val clicks = Publishers.open<Unit>()
        every { view.clicks } returns clicks

        presenter.setup(activity)
        activity.onResume()

        clicks.onNext(Unit)

        verify {
            HomeActivity.startActivity(activity)
        }
    }

    companion object {

        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            mockkObject(HomeActivity.Companion)
            every { HomeActivity.startActivity(any()) } just runs
        }

        @JvmStatic
        @AfterClass
        fun afterClass() {
            unmockkAll()
        }
    }
}
