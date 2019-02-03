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

package bill.catbox.home.boxes

import bill.catbox.game.GameNode
import bill.catbox.game.GameState
import bill.catbox.game.GameStateContainer
import bill.catbox.settings.SettingsRepository
import bill.catbox.test.ReactiveTestRule
import bill.reaktive.Publishers
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BoxesPresenterTests {

    @get:Rule
    val reactiveTestRule = ReactiveTestRule()

    private lateinit var view: BoxesView
    private lateinit var game: GameStateContainer

    private lateinit var presenter: BoxesPresenter

    @Before
    fun before() {
        view = mockk(relaxed = true)
        game = mockk(relaxed = true)
        val settingsRepository: SettingsRepository = mockk(relaxed = true)

        presenter = BoxesPresenter(view, game, settingsRepository)
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
        every { view.boxChosenEvent } returns Publishers.elements(0)

        val catFoundGameNode = GameNode(listOf(0), listOf(0))
        val catFoundState = GameState(1, listOf(catFoundGameNode))
        every { game.play(any()) } returns catFoundState

        presenter.attach()

        verify {
            view.onCatFound(any())
        }
    }
}
