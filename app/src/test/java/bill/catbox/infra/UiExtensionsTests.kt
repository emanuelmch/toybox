package bill.catbox.infra

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

//TODO: Replace all of this with JUnit5 parameterized tests
@RunWith(Parameterized::class)
class `Ui Extensions - To Ordinal - Numbers ending in st`(val number: Int) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0} -> {0}st")
        fun data() = listOf(1, 21, 91, 1001)
    }

    @Test
    fun test() {
        val expected = number.toString() + "st"
        assertThat(number.toOrdinal(), `is`(equalTo(expected)))
    }
}

@RunWith(Parameterized::class)
class `Ui Extensions - To Ordinal - Numbers ending in nd`(val number: Int) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0} -> {0}st")
        fun data() = listOf(2, 22, 92, 1002)
    }

    @Test
    fun test() {
        val expected = number.toString() + "nd"
        assertThat(number.toOrdinal(), `is`(equalTo(expected)))
    }
}

@RunWith(Parameterized::class)
class `Ui Extensions - To Ordinal - Numbers ending in rd`(val number: Int) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0} -> {0}st")
        fun data() = listOf(3, 23, 93, 1003)
    }

    @Test
    fun test() {
        val expected = number.toString() + "rd"
        assertThat(number.toOrdinal(), `is`(equalTo(expected)))
    }
}

@RunWith(Parameterized::class)
class `Ui Extensions - To Ordinal - Numbers ending in th`(val number: Int) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0} -> {0}st")
        fun data() = listOf(4, 10, 11, 12, 13, 34, 94, 1000, 1004)
    }

    @Test
    fun test() {
        val expected = number.toString() + "th"
        assertThat(number.toOrdinal(), `is`(equalTo(expected)))
    }
}
