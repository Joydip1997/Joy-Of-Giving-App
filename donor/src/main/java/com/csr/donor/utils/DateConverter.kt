package com.csr.donor.utils

import android.icu.util.Calendar
import android.text.format.DateUtils
import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class DateConverter {
    private val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
    fun getDate(string: String?): String {
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date: Date
        try {
            date = inputFormat.parse(string)
            inputFormat.timeZone = TimeZone.getDefault()
            return DateUtils.getRelativeTimeSpanString(
                date.time,
                Calendar.getInstance().timeInMillis,
                DateUtils.MINUTE_IN_MILLIS
            ) as String
        } catch (e: ParseException) {
            e.printStackTrace()
            Log.i("TAG", "getDate: " + e.message)
        }
        return ""
    }
}