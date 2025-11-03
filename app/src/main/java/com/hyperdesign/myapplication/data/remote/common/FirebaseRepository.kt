package com.hyperdesign.myapplication.data.remote.common

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

class FirebaseRepository{

    suspend fun getFirebaseToken(): String? {
        return try {
            FirebaseMessaging.getInstance().token.await()
        } catch (e: Exception) {
            Log.e("Firebase", "Fetching FCM token failed", e)
            null
        }
    }
}
