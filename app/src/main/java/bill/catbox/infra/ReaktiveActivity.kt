package bill.catbox.infra

import android.content.Context
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import bill.reaktive.OpenPublisher
import bill.reaktive.Publisher
import bill.reaktive.Publishers

abstract class ReaktiveActivity : AppCompatActivity() {
    private val optionsItemSelectedProcessor: OpenPublisher<Int> by lazy { Publishers.open<Int>() }

    val optionsItemSelected: Publisher<Int>
        get() = optionsItemSelectedProcessor

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.itemId?.let { optionsItemSelectedProcessor.onNext(it) }
        return true
    }
}

val Context.optionsItemSelected: Publisher<Int>
    get() = (this as? ReaktiveActivity)?.optionsItemSelected ?: Publishers.empty()
