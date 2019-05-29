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

package bill.toybox.catbox

import android.preference.PreferenceManager
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import bill.toybox.R
import bill.toybox.catbox.home.HomeActivity
import bill.toybox.catbox.settings.SettingsActivity
import bill.toybox.catbox.test.awaitDisappear
import bill.toybox.catbox.test.waitForView
import bill.toybox.catbox.test.withQuantityText
import bill.toybox.catbox.test.withText
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.google.android.material.R as material

@RunWith(AndroidJUnit4::class)
class CatboxTests {

    @get:Rule
    val espressoTestRule = IntentsTestRule(HomeActivity::class.java)

    @Test
    fun testNavigatingToSettings() {
        onView(withId(R.id.actionSettings))
                .perform(click())

        intended(hasComponent(SettingsActivity::class.java.name))
    }

    @Test
    fun testFindingTheCat() {
        onView(withText(R.string.box_number, 2)).perform(click())
        onView(withId(material.id.snackbar_text))
                .check(matches(withText(R.string.empty_box, "1st")))

        onView(withId(material.id.snackbar_text))
                .perform(awaitDisappear())

        onView(withText(R.string.box_number, 2)).perform(click())

        waitForView(withId(material.id.snackbar_text))
                .check(matches(withQuantityText(R.plurals.cat_found, 2)))
    }

    @Test
    fun testMultipleFailedAttempts() {
        onView(withText(R.string.box_number, 1)).perform(click())
        waitForView(withId(material.id.snackbar_text))
                .check(matches(withText(R.string.empty_box, "1st")))

        onView(withText(R.string.box_number, 1)).perform(click())
        waitForView(withId(material.id.snackbar_text))
                .check(matches(withText(R.string.empty_box, "2nd")))

        onView(withText(R.string.box_number, 1)).perform(click())
        waitForView(withId(material.id.snackbar_text))
                .check(matches(withText(R.string.empty_box, "3rd")))
    }

    companion object {

        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            PreferenceManager.getDefaultSharedPreferences(context).edit().apply {
                putInt("pref_boxCount", 3)
                commit()
            }
        }
    }
}
