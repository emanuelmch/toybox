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
import bill.catbox.test.nonNull
import bill.reactive.Publishers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Answers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class HomePresenterTests {

    @get:Rule
    val reactivesRule = ReactiveTestRule()

    @Mock(answer = Answers.RETURNS_MOCKS)
    private lateinit var view: HomeView

    @Mock
    private lateinit var game: GameEngine

    @Mock
    private lateinit var navigator: Navigator

    @Mock(answer = Answers.RETURNS_MOCKS)
    private lateinit var settingsRepository: SettingsRepository

    private lateinit var presenter: HomePresenter

    @Before
    fun before() {
        MockitoAnnotations.initMocks(this)

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
        `when`(view.boxChosenEvent).thenReturn(Publishers.elements(0))

        val catFoundGameNode = GameNode(listOf(0), listOf(0))
        val catFoundState = GameState(1, listOf(catFoundGameNode))
        `when`(game.play(nonNull(), anyInt())).thenReturn(catFoundState)

        presenter.attach()

        verify(view).onCatFound(anyInt())
    }
}
