package com.trendster.campus.ui.fragment.user.forgot

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.trendster.campus.databinding.FragmentForgotBinding

class ForgotFragment : Fragment() {

    private var _binding: FragmentForgotBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var etEmail: EditText
    private lateinit var btnForgot: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgotBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        etEmail = binding.etForgotEmail
        btnForgot = binding.btnForgotPass

        btnForgot.setOnClickListener {
            forgotPassword()
        }

        return binding.root
    }

    private fun forgotPassword() {
        when {
            etEmail.text.isEmpty() -> {
                etEmail.error = "Fill this"
                return
            }
            !Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString()).matches() -> {
                etEmail.error = "Use correct format"
                return
            }
            else -> {
                auth.sendPasswordResetEmail(etEmail.text.toString())
                    .addOnSuccessListener {
                        Toast.makeText(
                            requireContext(),
                            "Instructions has been sent to entered Email",
                            Toast.LENGTH_LONG
                        )
                            .show()
//                        findNavController().navigate(R.id.action_forgotFragment_to_loginFragment)
                        findNavController().navigateUp()
                    }.addOnFailureListener { info ->
                        Toast.makeText(
                            requireContext(),
                            info.message,
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
            }
        }
    }
}
