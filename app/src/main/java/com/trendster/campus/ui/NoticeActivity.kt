package com.trendster.campus.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.trendster.campus.databinding.ActivityNoticeBinding

class NoticeActivity : AppCompatActivity() {

    private var _binding: ActivityNoticeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        binding.noticeWebView.loadUrl("https://www.cdlsiet.ac.in/notices/")
        showSnackBar()

        binding.closeButton.setOnClickListener {
            finish()
        }
    }

    private fun showSnackBar() {
        val snackBar = Snackbar.make(
            binding.noticeLayout, "Loading Notices...",
            Snackbar.LENGTH_LONG
        )
        snackBar.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
