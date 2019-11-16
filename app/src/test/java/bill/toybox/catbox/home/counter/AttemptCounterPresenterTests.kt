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

package bill.toybox.catbox.home.counter

import bill.reaktive.Publishers
import bill.toybox.catbox.game.GameState
import bill.toybox.catbox.game.GameStateContainer
import bill.toybox.test.MockObservableActivity
import bill.toybox.test.ReactiveTestRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AttemptCounterPresenterTests {

    @get:Rule
    val reactiveTestRule = ReactiveTestRule()

    @get:Rule
    val activity = MockObservableActivity.create()

    private lateinit var view: AttemptCounterView
    private lateinit var game: GameStateContainer

    private lateinit var presenter: AttemptCounterPresenter

    @Before
    fun before() {
        view = mockk(relaxed = true)
        game = mockk(relaxed = true)

        presenter = AttemptCounterPresenter(view, game)
    }

    @Test
    fun `should update the view when the attempt count changes`() {
        val gameState = Publishers.open<GameState>()
        every { game.gameStateChanged } returns gameState

        presenter.setup(activity)
        activity.onResume()

        gameState.onNext(createState(attemptCount = 2))
        verify {
            view.count = 2
        }

        gameState.onNext(createState(attemptCount = 4))
        verify {
            view.count = 4
        }
    }
}

private fun createState(attemptCount: Int) =
        mockk<GameState>().apply {
            every { attempts } returns attemptCount
        }
