package com.trendster.campus.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.trendster.campus.databinding.ActivityUserBinding
import com.trendster.campus.viewmodels.mainviewmodel.MainViewModel
import com.trendster.campus.viewmodels.mainviewmodel.MainViewModelFactory
import com.trendster.campus.viewmodels.userviewmodel.UserViewModel

class UserActivity : AppCompatActivity() {

    private var _binding : ActivityUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
    }
}