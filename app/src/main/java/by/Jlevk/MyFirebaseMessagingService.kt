package by.Jlevk

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e("message", "Message Received...")

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("token", "New Token")
    }
}