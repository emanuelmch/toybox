package bill.catbox.home

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.EmptyDisposable
import timber.log.Timber

class HomePresenter(private val view: HomeView) {

    private var disposable: Disposable = EmptyDisposable.INSTANCE

    fun attach() {
        Timber.d("Presenter::attach")
        disposable = view.boxChosenEvent
                .subscribe {
                    Timber.d("Box #$it chosen")
                }
    }

    fun detach() {
        Timber.d("Presenter::detach")
        disposable.dispose()
    }
}

interface HomeView {
    val boxChosenEvent: Observable<Int>
}
