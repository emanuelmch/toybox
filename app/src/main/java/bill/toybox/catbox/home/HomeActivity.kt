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

package bill.toybox.catbox.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import bill.toybox.R
import bill.toybox.catbox.appbar.AppBarPresenter
import bill.toybox.catbox.home.boxes.BoxesPresenter
import bill.toybox.catbox.home.boxes.BoxesView
import bill.toybox.catbox.home.counter.AttemptCounterPresenter
import bill.toybox.catbox.home.counter.AttemptCounterView
import bill.toybox.infra.ObservableActivity
import kotlinx.android.synthetic.main.home_view.*

class HomeActivity : ObservableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_view)

        AppBarPresenter().setup(this)

        val boxesView = BoxesView(this.boxes)
        BoxesPresenter(this, boxesView).setup(this)

        val counterView = AttemptCounterView(this.attemptCounter)
        AttemptCounterPresenter(counterView).setup(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, HomeActivity::class.java)
            context.startActivity(intent)
        }
    }
}

