package com.csr.common



import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume


fun <T> Flow<T>.collectIn(
    lifecycleOwner: LifecycleOwner,
    onEachValue: (T) -> Unit
) {
    lifecycleOwner.lifecycleScope.launch {
        collect { value ->
            onEachValue(value)
        }
    }
}

fun Activity.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Any?.log(logTag : String = "JAPAN"){
    if(BuildConfig.DEBUG){
        Log.i(logTag, "log: ${this}")
    }
}

fun View.isVisibleAndEnable(value : Boolean){
    this.visibility =  if (value) View.VISIBLE else View.INVISIBLE
    this.isEnabled = value
}


fun Context.getScreenWidth(): Int {
    return resources.displayMetrics.widthPixels
}

fun Context.getScreenHeight(): Int {
    return resources.displayMetrics.heightPixels
}

fun <T> Continuation<T>.safeResume(value: T) {
    try {
        this.resume(value)
    } catch (e: Exception) {
        null
    }
}


fun Context.hasLocationPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
}