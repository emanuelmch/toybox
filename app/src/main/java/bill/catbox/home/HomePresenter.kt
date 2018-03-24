package bill.catbox.home

import bill.catbox.game.GameEngine
import bill.catbox.game.GameState
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.EmptyDisposable
import timber.log.Timber

class HomePresenter(private val view: HomeView) {

    private val game = GameEngine()
    private var disposable: Disposable = EmptyDisposable.INSTANCE

    // TODO: Remove this using the reduce Observable
    private var gameState = GameState()

    fun attach() {
        Timber.d("Presenter::attach")
        gameState = game.newGame()
        disposable = view.boxChosenEvent
                .subscribe {
                    Timber.d("Box #$it chosen")
                    gameState = game.play(gameState, it)
                    if (gameState.isCatFound) {
                        view.onCatFound(gameState.moveCount)
                    } else {
                        view.onEmptyBox(gameState.moveCount)
                    }
                }
    }

    fun detach() {
        Timber.d("Presenter::detach")
        disposable.dispose()
    }
}

interface HomeView {
    val boxChosenEvent: Observable<Int>

    fun onCatFound(attempts: Int)
    fun onEmptyBox(attempts: Int)
}
