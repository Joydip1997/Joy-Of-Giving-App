package com.csr.donor.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException
import java.net.URL
import java.util.Calendar
import java.util.concurrent.TimeUnit

fun formatTime(timeInMillis: Int): String {
    val hours = TimeUnit.MILLISECONDS.toHours(timeInMillis.toLong())
    val minutes = TimeUnit.MILLISECONDS.toMinutes(timeInMillis.toLong()) -
            TimeUnit.HOURS.toMinutes(hours)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(timeInMillis.toLong()) -
            TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minutes)

    return if (hours > 0) {
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%02d:%02d", minutes, seconds)
    }
}

fun getGreetingBasedOnTime(): String {
    val calendar = Calendar.getInstance()
    val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)

    return when (hourOfDay) {
        in 0..11 -> "Good Morning"
        in 12..16 -> "Good Afternoon"
        in 17..20 -> "Good Evening"
        else -> "Good Night"
    }
}

fun getBitmapFromUrl(url: String): Bitmap? {
    return try {
        val inputStream = URL(url).openStream()
        BitmapFactory.decodeStream(inputStream)
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}


fun convertToMinuteFormat(timeInMillis: Long,shouldShowMinuteOnly : Boolean = false): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(timeInMillis)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(timeInMillis) -
            TimeUnit.MINUTES.toSeconds(minutes)
    return String.format(if(shouldShowMinuteOnly) "%02dMIN" else "%02d:%02dMIN", minutes, seconds)
}

fun convertToMinFormat(timeInMillis: Long): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(timeInMillis)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(timeInMillis) -
            TimeUnit.MINUTES.toSeconds(minutes)
    return String.format("%02d MIN", minutes, seconds)
}

fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
    val activityManager = context.getSystemService(AppCompatActivity.ACTIVITY_SERVICE) as ActivityManager

    // Get a list of the currently running services
    val runningServices = activityManager.getRunningServices(Int.MAX_VALUE)

    // Loop through the running services and check if the specified service is running
    for (service in runningServices) {
        if (service.service.className == serviceClass.name) {
            // The service is running
            return true
        }
    }

    // The service is not running
    return false
}

fun Activity.openEmailClient(recipient: String, subject: String, body: String) {
    val emailIntent = Intent().apply {
        action = Intent.ACTION_SEND
        data = Uri.parse("mailto:")
        type = "text/plain"
        putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
    }
    if (emailIntent.resolveActivity(this.packageManager) != null) {
       try {
           emailIntent.setPackage("com.google.android.gm")
           startActivity(emailIntent)
       }catch (e : Exception){
           showToast( e.message.toString())
       }
    } else {
        showToast( "No app available to send email!!")
    }
}

fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.openAppSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    startActivity(intent)
}

fun Any?.safeInt(): Int {
    return try {
        this.toString().toInt()
    } catch (e: Exception) {
        0
    }
}