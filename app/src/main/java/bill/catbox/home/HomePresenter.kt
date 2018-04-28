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
    private var gameState = GameState(0)

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
