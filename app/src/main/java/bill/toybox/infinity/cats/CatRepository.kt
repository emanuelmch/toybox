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

package bill.toybox.infinity.cats

import bill.reaktive.Publisher
import bill.reaktive.Publishers
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

class CatPromise(val id: Int, private val fetch: () -> Cat) {
    private val lazyCat: Cat by lazy { Timber.d("Requested cat $id")
        fetch() }

    val cat: Publisher<Cat>
        get() = Publishers.elements(Unit)
            .signalOnBackground()
     .map { lazyCat }.doOnNext { Timber.d("Got here") }
}

class CatRepository {

    private val api = Retrofit.Builder()
        .baseUrl("https://api.thecatapi.com/v1/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(CatAPI::class.java)

    private val memoryCache = mutableMapOf<Int, CatPromise>()

    operator fun get(index: Int): CatPromise {
        Timber.d("Looking for cat $index")
        if (memoryCache.containsKey(index)) return memoryCache[index]!!

        Timber.d("Cat $index not found, creating promise")
        val newCat = CatPromise(index) {
            Timber.d("Fetching cat $index")
            val x = api.search().execute().body()!![0]
            Timber.d("Cat $index fetched!")
            x
        }
//        val newCatInternal = api.search().execute().body()!![0]
//        val newCatInternal = Cat(url = "https://free-images.com/or/7e0a/cat_mom_kittens_cats.jpg", width=0, height=0)
//        val newCat = CatPromise(index) { newCatInternal }
        memoryCache[index] = newCat
        return newCat
    }
}
