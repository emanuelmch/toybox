package bill.catbox.test

import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.plugins.RxJavaPlugins
import java.util.*

fun <T> Observable<T>.testSubscribe(): TestObserver<T> {
    val list = Collections.synchronizedList(mutableListOf<Throwable>())
    val observer = TestObserver<T>()

    return this
            .doOnSubscribe {
                RxJavaPlugins.reset()
                RxJavaPlugins.setErrorHandler { list.add(it) }
            }
            .doFinally {
                RxJavaPlugins.reset()
                list.forEach { observer.onError(it) }
            }
            .subscribeWith(observer)
}
