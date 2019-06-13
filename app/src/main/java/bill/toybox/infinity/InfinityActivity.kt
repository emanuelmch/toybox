/*
 * Copyright (c) 2019 Emanuel Machado da Silva <emanuel.mch@gmail.com>
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

package bill.toybox.infinity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.NetworkOnMainThreadException
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import bill.toybox.R
import bill.toybox.infinity.cats.Cat
import bill.toybox.infinity.cats.CatRepository
import bill.toybox.infra.ObservableActivity
import bill.toybox.infra.inflateChild
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.infinity_activity.*
import kotlinx.android.synthetic.main.infinity_item.view.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class InfinityActivity : ObservableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.infinity_activity)

        val infinityView = InfinityView(infiniteCats)
        InfinityPresenter(infinityView).observe(this)
    }

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, InfinityActivity::class.java)
            context.startActivity(intent)
        }
    }
}
