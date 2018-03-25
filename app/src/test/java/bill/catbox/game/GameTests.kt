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