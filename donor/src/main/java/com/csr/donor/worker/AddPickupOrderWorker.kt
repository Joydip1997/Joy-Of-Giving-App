package com.csr.donor.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.csr.donor.Module.pickupOrderDTO
import com.csr.donor.R
import com.csr.donor.activity.MainActivity
import com.csr.donor.data.Order.PickupOrderRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class AddPickupOrderWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParams: WorkerParameters,
    private val pickupOrderRepository: PickupOrderRepository,
) : CoroutineWorker(context, workerParams) {

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "ADD_PLACE_REVIEW_WORKER"
        private const val PUSH_NOTIFICATION_ID = 3
        private const val NOTIFICATION_ID = 4
        private const val NOTIFICATION_CHANNEL_NAME = "PLACE REVIEW ADD WORKER"
    }


    override suspend fun doWork(): Result {
        return try {
            // Retrieve the list of byte arrays from input data
            val uploadedImageUrls = inputData.getStringArray("IMAGE_FILES")
            val pickupOrderDTO = pickupOrderDTO ?: return Result.failure()
            setForeground(getForegroundInfo(applicationContext))
            pickupOrderDTO.itemImages = uploadedImageUrls?.toList()

            val isReviewAdded = pickupOrderRepository.placeNewOrder(pickupOrderDTO)
            if (isReviewAdded != null) {
                sendPushNotificationOnSuccessFullPlaceAdd()
            }else{
                sendErrorPushNotification("Something Went Wrong! Please Try Again")
            }
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            sendErrorPushNotification(e.message ?: "Something Went Wrong! Please Try Again")
            Result.failure()
        }
    }

    private fun sendErrorPushNotification(message : String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager




        // Create a notification channel (required for Android 8.0 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }

        // Build the notification
        val notificationBuilder = NotificationCompat.Builder(context,
            NOTIFICATION_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle("Error")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(false)

        // Show the notification
        notificationManager.notify(PUSH_NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun sendPushNotificationOnSuccessFullPlaceAdd() {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val deepLinkIntent = Intent(context, MainActivity::class.java)

        val requestCode = 0 // You can change this value if needed
        val flags =
            PendingIntent.FLAG_IMMUTABLE // Use FLAG_UPDATE_CURRENT to update the PendingIntent if it already exists
        val pendingIntent = PendingIntent.getActivity(
            context,
            requestCode,
            deepLinkIntent,
            flags
        )

        // Create a notification channel (required for Android 8.0 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }

        // Build the notification
        val notificationBuilder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle("Order Scheduled")
            .setContentText("Your Pickup Order Is Scheduled")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        // Show the notification
        notificationManager.notify(PUSH_NOTIFICATION_ID, notificationBuilder.build())
    }



    override suspend fun getForegroundInfo(): ForegroundInfo {
        return getForegroundInfo(applicationContext)
    }

    private fun getForegroundInfo(context: Context) : ForegroundInfo{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(
                NOTIFICATION_ID,
                createNotification(context,"Adding","Your Place Is Adding"),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            ForegroundInfo(
                NOTIFICATION_ID,
                createNotification(context,"Adding","Your Place Is Adding"),
            )
        }
    }

    private fun createNotification(context: Context,title : String,description : String): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setTicker(title)
            .setContentText(description)
            .setSmallIcon(R.drawable.ic_logo)
            .setOngoing(true)
            .build()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val importance = NotificationManager.IMPORTANCE_HIGH // Set the importance level
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME, importance)
        // Register the notification channel with the system
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
