package com.csr.common.data

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

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




    var isOnboardingAlreadyDonne: Boolean get() = prefs.getBoolean(IS_USER_ONBOARDING_IN_ALREADY_DONE, false)
        set(value) {
            editor.putBoolean(IS_USER_ONBOARDING_IN_ALREADY_DONE, value)
            editor.apply()
        }

    var distance: Int
        get() = prefs.getInt(DISTANCE, 5) * 1000
        set(value) {
            editor.putInt(DISTANCE, value)
            editor.apply()
        }

    var scrapTypes: List<String>
        get() {
            val json = prefs.getString(SCRAP_TYPES, null)
            return if (json != null) {
                val type = object : TypeToken<List<String>>() {}.type
                Gson().fromJson(json, type)
            } else {
                emptyList()
            }
        }
        set(value) {
            val json = Gson().toJson(value)
            editor.putString(SCRAP_TYPES, json)
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
        const val DISTANCE = "DISTANCE"
        const val MOBILE_NUMBER = "MOBILE_NUMBER"
        private const val SCRAP_TYPES = "SCRAP_TYPES"
    }

    init {
        prefs = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE)
        editor = prefs.edit()

    }
}