package bill.catbox.game

class GameEngine {

    fun newGame() = GameState()

    fun play(state: GameState, box: Int) =
            state.copy(moveCount = state.moveCount + 1, isCatFound = box == 0)
}

data class GameState(val moveCount: Int = 0,
                     val isCatFound: Boolean = false)
