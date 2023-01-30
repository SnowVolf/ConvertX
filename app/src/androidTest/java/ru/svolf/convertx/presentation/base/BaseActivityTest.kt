package ru.svolf.convertx.presentation.base

import android.content.Intent
import android.content.IntentFilter
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/*
 * Created by SVolf on 29.01.2023, 23:29
 * This file is a part of "ConvertX" project
 */
@RunWith(AndroidJUnit4::class)
class BaseActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(BaseActivity::class.java)


    @Test
    fun testThemeReceiver() {
        val context = activityRule.activity
        val intentFilter = IntentFilter("org.openintents.action.REFRESH_THEME")
        val receiver = context.mThemeReceiver
        val intent = Intent("org.openintents.action.REFRESH_THEME")

        receiver.onReceive(context, intent)

        assertTrue(context.isFinishing)
        assertTrue(context.isTaskRoot)
        assertTrue(intentFilter.hasAction("org.openintents.action.REFRESH_THEME"))
    }
}