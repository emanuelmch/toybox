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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import bill.catbox.game.GameEngine
import bill.catbox.game.GameState
import bill.catbox.navigation.Navigator
import bill.catbox.settings.SettingsRepository
import bill.reaktive.Publisher
import bill.reaktive.Publishers
import bill.reaktive.SubscriptionBag
import timber.log.Timber
import java.util.concurrent.TimeUnit

class HomePresenter(private val view: HomeView,
                    private val game: GameEngine,
                    private val navigator: Navigator,
                    private val settings: SettingsRepository
) : LifecycleObserver {

    constructor(view: HomeView, context: Context)
            : this(view, GameEngine(), Navigator(context), SettingsRepository(context))

    private val disposables = SubscriptionBag()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun attach() {
        Timber.d("Presenter::attach")

        // TODO: Remove this variable using a reduce Observable
        var gameState = GameState(0)

        disposables += settings.watchBoxCount()
                .subscribe {
                    gameState = game.newGame(it)
                    view.startGame(it)
                }

        disposables += view.boxChosenEvent
                .signalOnBackground()
                .doOnNext {
                    Timber.d("Box #$it chosen")
                    gameState = game.play(gameState, it)
                }
                .map {
                    if (gameState.isCatFound) {
                        Publishers.elements(it).delay(2, TimeUnit.SECONDS).blockingLast()
                    } else {
                        it
                    }
                }
                .signalOnForeground()
                .subscribe {
                    if (gameState.isCatFound) {
                        view.onCatFound(gameState.moveCount)
                    } else {
                        view.onEmptyBox(gameState.moveCount)
                    }
                }

        disposables += view.menuSelectedEvent
                .subscribe(navigator::navigateFromMenu)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun detach() {
        Timber.d("Presenter::detach")
        disposables.clear()
    }
}

interface HomeView {
    val boxChosenEvent: Publisher<Int>
    val menuSelectedEvent: Publisher<Int>

    fun onCatFound(attempts: Int)
    fun onEmptyBox(attempts: Int)
    fun startGame(boxCount: Int)
}
