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

package bill.catbox.game

import timber.log.Timber

class GameEngine {

    // TODO: Remove boxCount's default value
    fun newGame(boxCount: Int = 5) = GameState(boxCount)

    fun play(state: GameState, boxChecked: Int): GameState {
        Timber.d("Player checked box $boxChecked:")
        val catFoundNodes = mutableSetOf<GameNode>()
        val emptyBoxNodes = mutableSetOf<GameNode>()

        for (node in state.gameNodes) {
            val location = node.locationHistory.last()
            val moves = node.moves.plus(boxChecked)

            if (location == boxChecked) {
                catFoundNodes.add(node.copy(moves = moves))
            } else {
                val lastBox = state.boxCount - 1
                if (location > 0) {
                    emptyBoxNodes.add(GameNode(node.locationHistory.plus(location - 1), moves))
                }
                if (location < lastBox) {
                    emptyBoxNodes.add(GameNode(node.locationHistory.plus(location + 1), moves))
                }
            }
        }

        Timber.d("${emptyBoxNodes.size} new empty box nodes:")
        emptyBoxNodes.forEach { Timber.d("$it") }

        Timber.d("${catFoundNodes.size} new cat found nodes:")
        catFoundNodes.forEach { Timber.d("$it") }

        return if (emptyBoxNodes.isNotEmpty()) {
            state.copy(gameNodes = emptyBoxNodes)
        } else {
            state.copy(gameNodes = catFoundNodes)
        }
    }
}

data class GameState(val boxCount: Int,
                     val gameNodes: Collection<GameNode> = (0 until boxCount).map { GameNode(it) }) {
    val isCatFound = gameNodes.any { it.isCatFound }
    val moveCount = gameNodes.firstOrNull()?.moves?.size ?: 0
}

data class GameNode(val locationHistory: List<Int>,
                    val moves: List<Int> = listOf()) {

    constructor(initialLocation: Int) : this(listOf(initialLocation))

    val isCatFound = moves.isNotEmpty() && moves.last() == locationHistory.get(moves.lastIndex)
}
