package com.trendster.campus.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.trendster.campus.R
import com.trendster.campus.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    private var _binding : ActivityAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.txtGithubNishant.setOnClickListener {
            openUrl("https://github.com/knishant362")
        }
        binding.txtGithubRohit.setOnClickListener {
            openUrl("https://github.com/rohitjakhar")
        }
        binding.closeButton.setOnClickListener {
            finish()
        }
    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}
