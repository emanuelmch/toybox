package bill.catbox.home.counter

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import bill.catbox.game.GameStateContainer
import bill.reaktive.SubscriptionBag

class AttemptCounterPresenter(private val view: AttemptCounterView,
                              private val game: GameStateContainer = GameStateContainer) : LifecycleObserver {

    private val subscriptions = SubscriptionBag()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun attach() {
        subscriptions += game.gameStateChanged
                .signalOnForeground()
                .doOnNext { view.count = it.attempts }
                .subscribe()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun detach() {
        subscriptions.clear()
    }
}
