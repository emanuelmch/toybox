package bill.catbox.settings

import android.content.SharedPreferences
import bill.catbox.test.testSubscribe
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.Spy

class PreferenceWatcherTest {

    @Spy
    private lateinit var sharedPrefs: MockSharedPreferences

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testWatchInt_SharedPrefRegistering() {
        val disposable = PreferenceWatcher(sharedPrefs).watchInt("", 1984).subscribe()

        verify(sharedPrefs).registerOnSharedPreferenceChangeListener(any())
        verify(sharedPrefs, never()).unregisterOnSharedPreferenceChangeListener(any())

        disposable.dispose()

        verify(sharedPrefs).unregisterOnSharedPreferenceChangeListener(any())
    }

    @Test
    fun testWatchInt_DefaultValue() {
        val subscriber = PreferenceWatcher(sharedPrefs)
                .watchInt("key", 1984)
                .testSubscribe()

        subscriber.assertValuesOnly(1984)
        subscriber.dispose()
        subscriber.assertNoErrors()
    }

    @Test
    fun testWatchInt_ValueChanges() {
        sharedPrefs.setInt("key", 123)
        val subscriber = PreferenceWatcher(sharedPrefs).watchInt("key").testSubscribe()
        sharedPrefs.setInt("key", 124)

        subscriber.assertValuesOnly(123, 124)
        subscriber.dispose()
        subscriber.assertNoErrors()
    }

    @Test
    fun testWatchInt_MultipleSubscribers() {
        sharedPrefs.setInt("key", 123)
        val subscriber1 = PreferenceWatcher(sharedPrefs).watchInt("key").testSubscribe()
        sharedPrefs.setInt("key", 124)
        val subscriber2 = PreferenceWatcher(sharedPrefs).watchInt("key").testSubscribe()
        sharedPrefs.setInt("key", 125)

        subscriber1.assertValuesOnly(123, 124, 125)
        subscriber1.dispose()
        subscriber1.assertNoErrors()

        sharedPrefs.setInt("key", 126)

        subscriber2.assertValuesOnly(124, 125, 126)
        subscriber2.dispose()
        subscriber2.assertNoErrors()
    }
}

private open class MockSharedPreferences : SharedPreferences {
    private val intValues = mutableMapOf<String, Int>()
    private val listeners = mutableListOf<SharedPreferences.OnSharedPreferenceChangeListener>()

    override fun getInt(key: String, defValue: Int) =
            intValues.getOrDefault(key, defValue)

    fun setInt(key: String, value: Int) {
        intValues[key] = value
        listeners.forEach { it.onSharedPreferenceChanged(this, key) }
    }

    override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        if (listener == null) return

        listeners.add(listener)
    }

    override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        if (listener == null) return

        if (listeners.remove(listener) == false) {
            throw IllegalArgumentException("Trying to unregister non-registered Listener")
        }
    }

    //region Unused methods
    override fun contains(key: String?): Boolean {
        TODO("not implemented")
    }

    override fun getBoolean(key: String?, defValue: Boolean): Boolean {
        TODO("not implemented")
    }

    override fun getAll(): MutableMap<String, *> {
        TODO("not implemented")
    }

    override fun edit(): SharedPreferences.Editor {
        TODO("not implemented")
    }

    override fun getLong(key: String?, defValue: Long): Long {
        TODO("not implemented")
    }

    override fun getFloat(key: String?, defValue: Float): Float {
        TODO("not implemented")
    }

    override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String> {
        TODO("not implemented")
    }

    override fun getString(key: String?, defValue: String?): String {
        TODO("not implemented")
    }
    //endregion Unused methods
}
