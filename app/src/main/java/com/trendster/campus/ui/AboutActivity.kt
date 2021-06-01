package com.trendster.campus.ui

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.trendster.campus.databinding.ActivityAboutBinding
import com.trendster.campus.viewmodels.mainviewmodel.MainViewModel

class AboutActivity : AppCompatActivity() {

    private var _binding: ActivityAboutBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setupProgressDialog()

        binding.txtGithubNishant.setOnClickListener {
            openUrl("https://github.com/knishant362")
        }
        binding.txtGithubRohit.setOnClickListener {
            openUrl("https://github.com/rohitjakhar")
        }
        binding.closeButton.setOnClickListener {
            finish()
        }
        val userUID = auth.currentUser?.uid
        if (userUID != null) {
            binding.btnSubmit.setOnClickListener {
                when {
                    binding.edtFeedback.editText?.text?.isEmpty() == true -> {
                        binding.edtFeedback.error = "This can't be empty"
                    }
                    else -> {
                        progressDialog.show()
                        mainViewModel.submitFeedBack(userUID, binding.edtFeedback.editText?.text.toString())
                    }
                }
            }
            mainViewModel.readFeedbackUpload.observe(
                this,
                {
                    if (it) {
                        progressDialog.hide()
                        showToast("Feedback Submitted.")
                        finish()
                    } else {
                        progressDialog.hide()
                        showToast("Some Error Occurred")
                    }
                }
            )
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun setupProgressDialog() {
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading")
        progressDialog.setCanceledOnTouchOutside(false)
    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
