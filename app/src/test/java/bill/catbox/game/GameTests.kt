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

import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class GameEngineTests {

    @Test
    fun testCatWasFound() {
        val previousState = GameState(gameNodes = setOf(GameNode(3)))

        val newState = GameEngine().play(previousState, 3)

        assertThat(newState.isCatFound, `is`(true))
    }

    @Test
    fun testCatWasInFirstBox() {
        val previousState = GameState(gameNodes = setOf(GameNode(0)))

        val newState = GameEngine().play(previousState, 3)

        assertThat(newState.isCatFound, `is`(false))
        assertThat(newState.gameNodes.size, `is`(1))
        assertThat(newState.gameNodes.first(), `is`(equalTo(GameNode(listOf(0, 1), listOf(3)))))
    }

    @Test
    fun testCatWasInLastBox() {
        val previousState = GameState(gameNodes = setOf(GameNode(4)))

        val newState = GameEngine().play(previousState, 3)

        assertThat(newState.isCatFound, `is`(false))
        assertThat(newState.gameNodes.size, `is`(1))
        assertThat(newState.gameNodes.first(), `is`(equalTo(GameNode(listOf(4, 3), listOf(3)))))
    }

    @Test
    fun testCatWasInTheMiddle() {
        val previousState = GameState(gameNodes = setOf(GameNode(3)))

        val newState = GameEngine().play(previousState, 2)

        assertThat(newState.isCatFound, `is`(false))
        assertThat(newState.gameNodes.size, `is`(2))
        assertThat(newState.gameNodes.first(), `is`(equalTo(GameNode(listOf(3, 2), listOf(2)))))
        assertThat(newState.gameNodes.last(), `is`(equalTo(GameNode(listOf(3, 4), listOf(2)))))
    }
}