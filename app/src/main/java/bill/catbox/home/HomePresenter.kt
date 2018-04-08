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
import bill.catbox.game.GameEngine
import bill.catbox.game.GameState
import bill.catbox.infra.plusAssign
import bill.catbox.navigation.Navigator
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class HomePresenter(private val view: HomeView, context: Context) {

    private val game = GameEngine()
    private val navigator = Navigator(context)

    private val disposables = CompositeDisposable()

    // TODO: Remove this using the reduce Observable
    private var gameState = GameState()

    fun attach() {
        Timber.d("Presenter::attach")
        gameState = game.newGame()
        disposables += view.boxChosenEvent
                .subscribe {
                    Timber.d("Box #$it chosen")
                    gameState = game.play(gameState, it)
                    if (gameState.isCatFound) {
                        view.onCatFound(gameState.moveCount)
                    } else {
                        view.onEmptyBox(gameState.moveCount)
                    }
                }

        disposables += view.menuSelectedEvent
                .subscribe(navigator::navigateFromMenu)
    }

    fun detach() {
        Timber.d("Presenter::detach")
        disposables.clear()
    }
}

interface HomeView {
    val boxChosenEvent: Observable<Int>
    val menuSelectedEvent: Observable<Int>

    fun onCatFound(attempts: Int)
    fun onEmptyBox(attempts: Int)
}
