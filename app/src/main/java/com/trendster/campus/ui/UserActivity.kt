package com.trendster.campus.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.trendster.campus.R
import com.trendster.campus.databinding.ActivityUserBinding
import com.trendster.campus.viewmodels.MainViewModel
import com.trendster.campus.viewmodels.MainViewModelFactory

class UserActivity : AppCompatActivity() {

    private var _binding : ActivityUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mainViewModelFactory = MainViewModelFactory(this)
        mainViewModel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)
    }
}