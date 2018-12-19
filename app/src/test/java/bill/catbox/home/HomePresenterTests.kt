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

import bill.catbox.game.GameEngine
import bill.catbox.game.GameNode
import bill.catbox.game.GameState
import bill.catbox.navigation.Navigator
import bill.catbox.settings.SettingsRepository
import bill.catbox.test.ReactiveTestRule
import bill.reaktive.Publishers
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomePresenterTests {

    @get:Rule
    val reactiveTestRule = ReactiveTestRule()

    private lateinit var view: HomeView
    private lateinit var game: GameEngine

    private lateinit var presenter: HomePresenter

    @Before
    fun before() {
        view = mockk(relaxed = true)
        game = mockk(relaxed = true)
        val navigator: Navigator = mockk(relaxed = true)
        val settingsRepository: SettingsRepository = mockk(relaxed = true)

        presenter = HomePresenter(view, game, navigator, settingsRepository)
    }

    @Test
    fun `should attach and detach without exceptions`() {
        presenter.apply {
            attach()
            detach()
        }
    }

    @Test
    fun `should call view_onCatFound when cat is found`() {
        //FIXME: Should be a TestOpenPublisher that's actually closed
        val boxChosenEvents = Publishers.open<Int>()
        every { view.boxChosenEvent } returns boxChosenEvents

        val catFoundGameNode = GameNode(listOf(0), listOf(0))
        val catFoundState = GameState(1, listOf(catFoundGameNode))
        every { game.play(any(), any()) } returns catFoundState

        presenter.attach()
        boxChosenEvents.onNext(0)

        verify {
            view.onCatFound(any())
        }
    }
}
