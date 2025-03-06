package com.zeller.terminalapp

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zeller.terminalapp.presentation.MainActivity
import org.hamcrest.CoreMatchers.containsString
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun testDepositButton() {
        // Input an amount
        Espresso.onView(withId(R.id.amountInput))
            .perform(ViewActions.typeText("100.0"), ViewActions.closeSoftKeyboard())

        // Click the deposit button
        Espresso.onView(withId(R.id.depositButton))
            .perform(ViewActions.click())

        // Verify the balance is updated
        Espresso.onView(withId(R.id.balance))
            .check(ViewAssertions.matches(withText("100.0")))
    }

    @Test
    fun testWithdrawButton() {
        // Input an amount
        Espresso.onView(withId(R.id.amountInput))
            .perform(ViewActions.typeText("100.0"), ViewActions.closeSoftKeyboard())

        // Click the deposit button first
        Espresso.onView(withId(R.id.depositButton))
            .perform(ViewActions.click())

        // clear text first
        Espresso.onView(withId(R.id.amountInput))
            .perform(ViewActions.clearText(), ViewActions.closeSoftKeyboard())

        Espresso.onView(withId(R.id.amountInput))
            .perform(ViewActions.typeText("20.0"), ViewActions.closeSoftKeyboard())

        // Click the withdraw button
        Espresso.onView(withId(R.id.withdrawButton))
            .perform(ViewActions.click())

        // Verify the balance is updated
        Espresso.onView(withId(R.id.balance))
            .check(ViewAssertions.matches(withText("80.0")))
    }

    @Test
    fun testWhenNoPastTransactions() {
        // Click the past transactions button
        Espresso.onView(withId(R.id.pastTransactionsButton))
            .perform(ViewActions.click())

        // Verify the no transactions yet displayed
        Espresso.onView(withId(R.id.pastTransactions))
            .check(ViewAssertions.matches(withText(containsString("Oh Seems like you havn\'t done any transactions yet."))))
    }

    @Test
    fun testWhenPastTransactionsAdded() {
        // Input an amount
        Espresso.onView(withId(R.id.amountInput))
            .perform(ViewActions.typeText("100.0"), ViewActions.closeSoftKeyboard())

        // Click the deposit button first
        Espresso.onView(withId(R.id.depositButton))
            .perform(ViewActions.click())

        // clear text first
        Espresso.onView(withId(R.id.amountInput))
            .perform(ViewActions.clearText(), ViewActions.closeSoftKeyboard())

        Espresso.onView(withId(R.id.amountInput))
            .perform(ViewActions.typeText("20.0"), ViewActions.closeSoftKeyboard())

        // Click the withdraw button
        Espresso.onView(withId(R.id.withdrawButton))
            .perform(ViewActions.click())

        // Click the past transactions button
        Espresso.onView(withId(R.id.pastTransactionsButton))
            .perform(ViewActions.click())

        // Verify the no transactions yet displayed
        Espresso.onView(withId(R.id.pastTransactions))
            .check(ViewAssertions.matches(withText(containsString("Past Transactions:"))))
    }
}