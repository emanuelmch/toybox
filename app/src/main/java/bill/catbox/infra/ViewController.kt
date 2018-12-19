package bill.catbox.infra

import android.content.Context
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import bill.reaktive.OpenPublisher
import bill.reaktive.Publisher
import bill.reaktive.Publishers

interface ViewController {
    val context: Context
    val optionsItemSelected: Publisher<Int>
}

abstract class ViewControllerActivity : AppCompatActivity(), ViewController {
    private val optionsItemSelectedProcessor: OpenPublisher<Int> by lazy { Publishers.open<Int>() }

    final override val context
        get() = this

    final override val optionsItemSelected: Publisher<Int>
        get() = optionsItemSelectedProcessor

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.itemId?.let { optionsItemSelectedProcessor.onNext(it) }
        return true
    }
}

val Context.optionsItemSelected: Publisher<Int>
    get() = (this as? ViewController)?.optionsItemSelected ?: Publishers.empty()
