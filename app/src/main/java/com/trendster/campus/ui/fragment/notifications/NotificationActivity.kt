package com.trendster.campus.ui.fragment.notifications

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.trendster.campus.R
import com.trendster.campus.databinding.ActivityNotificationBinding
import com.trendster.campus.models.NotificationData
import com.trendster.campus.models.PushNotification
import com.trendster.campus.services.RetrofitInstance
import com.trendster.campus.ui.MainActivity
import com.trendster.campus.viewmodels.MainViewModel
import com.trendster.campus.viewmodels.MainViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TOPIC = "/topics/myTopic"

class NotificationActivity : AppCompatActivity() {
    val TAG =  "Notification Activity"
    private var _binding : ActivityNotificationBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mainViewModelFactory = MainViewModelFactory(this)
        mainViewModel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        binding.btnSend.setOnClickListener {
            val title = binding.etTitle
            val message = binding.etDesc
            if (title.text.isNotEmpty() && message.text.isNotEmpty()){
                PushNotification(
                        NotificationData(title.text.toString(), message.text.toString()),
                        TOPIC
                ).also {
                    sendNotification(it)
                }
                mainViewModel.saveNotifications(title.text.toString(), message.text.toString())
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
        }
    }


    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if (response.isSuccessful) {
                Log.d(TAG, "Response: ${Gson().toJson(response)}")
            }else{
                Log.d(TAG, response.errorBody().toString())
            }
        }catch (e: Exception) {
            Log.d(TAG, e.toString())
        }

    }
}