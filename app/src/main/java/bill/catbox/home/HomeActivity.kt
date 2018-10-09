/*
 * Copyright (c) 2018 Emanuel Machado da Silva <emanuel.mch@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package bill.catbox.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import bill.catbox.R
import bill.catbox.infra.inflateChild
import bill.catbox.infra.snackbar
import bill.catbox.infra.toOrdinal
import bill.reactive.Processors
import kotlinx.android.synthetic.main.home_activity.*
import kotlinx.android.synthetic.main.home_item.view.*
import timber.log.Timber

class HomeActivity : AppCompatActivity(), HomeView {

    private val boxAdapter by lazy { BoxAdapter().apply { boxes.adapter = this } }

    override val menuSelectedEvent = Processors.cold<Int>()
    override val boxChosenEvent = Processors.cold<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        lifecycle.addObserver(HomePresenter(this, this))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun startGame(boxCount: Int) {
        Timber.d("Starting the game with $boxCount boxes")
        boxAdapter.boxCount = boxCount
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        menuSelectedEvent.onNext(item.itemId)
        return true
    }

    override fun onCatFound(attempts: Int) {
        boxes.snackbar(resources.getQuantityString(R.plurals.cat_found, attempts, attempts))
    }

    override fun onEmptyBox(attempts: Int) {
        boxes.snackbar(getString(R.string.empty_box, attempts.toOrdinal()))
    }

    private inner class BoxAdapter : RecyclerView.Adapter<BoxViewHolder>() {

        var boxCount = 0
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun getItemCount() = boxCount

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
                BoxViewHolder(parent.inflateChild(R.layout.home_item))

        override fun onBindViewHolder(holder: BoxViewHolder, position: Int) {
            holder.bind(position)
        }
    }

    private inner class BoxViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val boxButton: Button = itemView.buttonBox

        fun bind(position: Int) {
            boxButton.text = getString(R.string.box_number, position + 1)
            boxButton.setOnClickListener { boxChosenEvent.onNext(position) }
        }
    }
}
