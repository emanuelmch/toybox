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

package bill.toybox.catbox.home.boxes

import android.content.Context
import bill.toybox.catbox.game.GameState
import bill.toybox.catbox.game.GameStateContainer
import bill.toybox.catbox.settings.SettingsRepository
import bill.toybox.infra.ObservableActivity
import bill.toybox.infra.debug

class BoxesPresenter(private val view: BoxesView,
                     private val game: GameStateContainer,
                     private val settings: SettingsRepository) {

    constructor(context: Context, view: BoxesView, game: GameStateContainer = GameStateContainer)
            : this(view, game, SettingsRepository(context))

    fun observe(activity: ObservableActivity) {
        activity.doOnResume {
            settings.watchBoxCount()
                    .doOnNext { game.newGame(it) }
                    .subscribeUntilPause()

            game.gameStateChanged
                    .filter(GameState::isNewGame)
                    .signalOnForeground()
                    .doOnNext { view.startGame(it.boxCount) }
                    .subscribeUntilPause()

            view.boxChosenEvent
                    .signalOnBackground()
                    .debug { "Box #$it chosen" }
                    .map(game::play)
                    .doOnNext {
                        if (it.isCatFound) {
                            Thread.sleep(2000)
                        }
                    }
                    .signalOnForeground()
                    .doOnNext { game ->
                        if (game.isCatFound) {
                            view.onCatFound(game.attempts)
                        } else {
                            view.onEmptyBox(game.attempts)
                        }
                    }
                    .subscribeUntilPause()
        }
    }
}
