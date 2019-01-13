package bill.catbox.home.counter

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import bill.catbox.game.GameEngine
import bill.reaktive.SubscriptionBag

class AttemptCounterPresenter(private val view: AttemptCounterView,
                              private val game: GameEngine) : LifecycleObserver {

    private val subscriptions = SubscriptionBag()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun attach() {
        subscriptions += game.gameStateChanged
                .signalOnForeground()
                .subscribe { view.count = it.attempts }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun detach() {
        subscriptions.clear()
    }
}
