package bill.catbox.home

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import bill.catbox.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class HomeNavigationTests {

    @get:Rule
    val homeActivity = ActivityTestRule(HomeActivity::class.java)

    @Test
    fun testNavigatingToSettings() {
        onView(withId(R.id.actionSettings))
                .perform(click())

        //TODO: Try and find a more reliable way of checking we moved screen
        onView(withText(R.string.settings))
                .check(matches(isDisplayed()))
    }
}
