package com.csr.common.data

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class AppPrefs(context: Context) {
    fun clear() {
        editor.clear().apply()
    }

    private val prefs: SharedPreferences
    private val editor: SharedPreferences.Editor


    var mobileNumber: Long
        get() = prefs.getLong(MOBILE_NUMBER, 0)
        set(value) {
            editor.putLong(MOBILE_NUMBER, value)
            editor.apply()
        }



    var userId: String?
        get() = prefs.getString(USER_ID, null)
        set(value) {
            editor.putString(USER_ID, value)
            editor.apply()
        }

    var authId: String?
        get() = prefs.getString(AUTH_ID, null)
        set(value) {
            editor.putString(AUTH_ID, value)
            editor.apply()
        }


    var isPremium: Boolean
        get() = prefs.getBoolean(IS_PREMIUM, false)
        set(value) {
            editor.putBoolean(IS_PREMIUM, value)
            editor.apply()
        }

    var purchaseToken: String?
        get() = prefs.getString(CURRENT_PURCHASE_TOKEN, null)
        set(value) {
            editor.putString(CURRENT_PURCHASE_TOKEN, value)
            editor.apply()
        }

    var hasUserStoppedBackgroundMusic: Boolean
        get() = prefs.getBoolean(BACKGROUND_MUSIC_STOPPED, true)
        set(value) {
            editor.putBoolean(BACKGROUND_MUSIC_STOPPED, value)
            editor.apply()
        }

    var backgroundMusicVolume: Float
        get() = prefs.getFloat(BACKGROUND_MUSIC_VOLUME, 0.5f)
        set(value) {
            editor.putFloat(BACKGROUND_MUSIC_VOLUME, value)
            editor.apply()
        }

    var isOnboardingAlreadyDonne: Boolean get() = prefs.getBoolean(IS_USER_ONBOARDING_IN_ALREADY_DONE, false)
        set(value) {
            editor.putBoolean(IS_USER_ONBOARDING_IN_ALREADY_DONE, value)
            editor.apply()
        }


    var isUserReviewedTheAppAllReady: Boolean get() = prefs.getBoolean(IS_USER_ALL_READY_REVIEWED_APP, false)
        set(value) {
            editor.putBoolean(IS_USER_ALL_READY_REVIEWED_APP, value)
            editor.apply()
        }


    var sessionCount: Int get() = prefs.getInt(SESSION_COUNT, 0)
        set(value) {
            editor.putInt(SESSION_COUNT, value)
            editor.apply()
        }

    companion object {
        const val PREF_NAME = "APP_PREF"
        const val USER_ID = "USER_ID"
        const val AUTH_ID = "AUTH_ID"
        const val IS_PREMIUM = "isPremium"
        const val CURRENT_PURCHASE_TOKEN = "CURRENT_PURCHASE_TOKEN"
        const val BACKGROUND_MUSIC_STOPPED = "BACKGROUND_MUSIC_STOPPED"
        const val IS_USER_ONBOARDING_IN_ALREADY_DONE = "IS_USER_ONBOARDING_IN_ALREADY_DONE"
        const val SOUND_TIMER_VALUE = "SOUND_TIMER_VALUE"
        const val MOBILE_NUMBER = "MOBILE_NUMBER"
        const val BACKGROUND_MUSIC_VOLUME = "BACKGROUND_MUSIC_VOLUME"
        const val IS_USER_ALL_READY_REVIEWED_APP = "IS_USER_ALL_READY_REVIEWED_APP"
        const val SESSION_COUNT = "SESSION_COUNT"
    }

    init {
        prefs = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE)
        editor = prefs.edit()

    }
}