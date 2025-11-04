package com.hyperdesign.myapplication.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.hyperdesign.myapplication.R
import com.hyperdesign.myapplication.presentation.main.MainActivity

class PushNotificationServices : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FirebaseApp", "New token: $token")
        // Save the token locally
//        val tokenManager = TokenManager(applicationContext)
//        tokenManager.saveFcmToken(token)
        //TODO: send token to server
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FirebaseApp", "onMessageReceived triggered")

        // Handle notification message
        remoteMessage.data.let {
            val title = it["title"]
            val body = it["body"]
            val imageUrl = it["image"]


            // Optional: Process the click_action (such as opening a specific activity/fragment)
            Log.d("FirebaseApp", "Notification Title: $title, Body: $body , image:$imageUrl")

            remoteMessage.data.let { data ->
                val type = data["type"]
                val id = data["action_id"]
                Log.d("FirebaseApp", "Data: Type: $type, ID: $id")
                // Show notification
                showNotification(title, body, 1,imageUrl,id, type)
            }
        }
    }




    @SuppressLint("MissingPermission")
    private fun showNotification(title: String?, body: String?,  notificationId: Int, imageUrl: String?,id: String?, type: String?) {
        val channelId = "hadramout_channel"
        createNotificationChannel(channelId, "Hadramout Notifications")

        val intent = Intent(this, MainActivity::class.java)
        handleNotificationType(type, id, intent)

        val pendingIntent = PendingIntent.getActivity(
            this,
            type.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle(title ?: getString(R.string.app_name))
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

            if (!imageUrl.isNullOrEmpty()) {
                Glide.with(this)
                    .asBitmap()
                    .load(imageUrl)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            notificationBuilder.setLargeIcon(resource)
                                .setStyle(NotificationCompat.BigPictureStyle().bigPicture(resource))
                            NotificationManagerCompat.from(this@PushNotificationServices)
                                .notify(notificationId, notificationBuilder.build())
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                            // Handle cleanup if needed
                        }
                    })
            } else {
                NotificationManagerCompat.from(this@PushNotificationServices)
                    .notify(notificationId, notificationBuilder.build())
            }

        val notificationId = (System.currentTimeMillis() and 0xfffffff).toInt()
        NotificationManagerCompat.from(this).notify(notificationId, notificationBuilder.build())
    }




    private fun handleNotificationType(type: String?, id: String?, intent: Intent) {
        Log.d("FirebaseApp", "type: $type, id: $id ")
        when (type) {
            "order" -> { // Open order details

                intent.action = "ACTION_OPENACTION_OPEN_ORDER_ORDER"
                intent.data = Uri.parse("deliveryapp://tracking/$id")
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            "promotion" -> { // Open promotions page
                intent.action = "DEFAULT_ACTION"
            }
            "chat" -> {
                // Open chat window
                intent.action = "ACTION_OPENACTION_OPEN_ORDER_ORDER"
                intent.data = Uri.parse("deliveryapp://chat/$id")
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            else -> { // Default action
                intent.action = "DEFAULT_ACTION"
            }
        }
    }


    private fun createNotificationChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for $channelName"
                enableLights(true)
                enableVibration(true)
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }


}
