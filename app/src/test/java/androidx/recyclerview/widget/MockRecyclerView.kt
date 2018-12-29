package androidx.recyclerview.widget

import io.mockk.every
import io.mockk.mockk
import java.lang.reflect.Field
import java.lang.reflect.Modifier

class MockRecyclerView {

    private class MockAdapterDataObservable : RecyclerView.AdapterDataObservable() {
        override fun notifyChanged() = Unit
    }

    companion object {
        fun create(): RecyclerView {
            val view: RecyclerView = mockk()
            var adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>? = null

            every { view.adapter } answers { adapter }
            every {
                view.adapter = any()
            } propertyType RecyclerView.Adapter::class answers {
                val field = RecyclerView.Adapter::class.java.getDeclaredField("mObservable")
                field.isAccessible = true

                val modifiers = Field::class.java.getDeclaredField("modifiers")
                modifiers.isAccessible = true
                modifiers.setInt(field, field.modifiers and Modifier.FINAL.inv())

                field.set(value, MockAdapterDataObservable())
                adapter = value

                modifiers.setInt(field, field.modifiers or Modifier.FINAL)
                modifiers.isAccessible = false
                field.isAccessible = false
            }

            return view
        }
    }
}
