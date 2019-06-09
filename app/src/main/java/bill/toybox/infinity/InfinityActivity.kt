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
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import bill.toybox.R
import bill.toybox.infinity.cats.CatRepository
import bill.toybox.infra.ObservableActivity
import bill.toybox.infra.debug
import bill.toybox.infra.inflateChild
import bill.toybox.infra.snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.infinity_activity.*
import kotlinx.android.synthetic.main.infinity_item.view.*

class InfinityActivity : ObservableActivity() {

    val urls = mutableListOf<String>()
    val cats = CatRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.infinity_activity)

//        infiniteCats.adapter = InfiniteCatsAdapter(listOf("https://cdn2.thecatapi.com/images/fF6nbC_w9.jpg", "https://cdn2.thecatapi.com/images/8mu.jpg"))
        getNextCat()
    }

    private fun getNextCat() {
        cats.random(10)
            .signalOnForeground()
            .doOnNext {
                urls += it
                if (urls.size == 10 && infiniteCats.adapter == null) {
                    infiniteCats.adapter = InfiniteCatsAdapter(urls)
                }
            }
            .doOnError {
                infiniteCats.snackbar("Deu erro! " + it.message)
            }
            .subscribeUntilPause()
    }


    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, InfinityActivity::class.java)
            context.startActivity(intent)
        }
    }
}

private class InfiniteCatsAdapter(val cats: List<String>) : RecyclerView.Adapter<CatViewHolder>() {

    override fun getItemCount() = cats.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CatViewHolder(parent.inflateChild(R.layout.infinity_item))

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        Picasso.get()
            .load(cats[position])
            .fit()
            .centerCrop()
            .into(holder.catImage)
    }

}

private class CatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val catImage: ImageView = itemView.catImage
}
