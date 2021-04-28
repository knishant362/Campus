package com.trendster.campus.ui.fragment.user.faculty

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.trendster.campus.R
import com.trendster.campus.databinding.FragmentFacultyLoginBinding
import com.trendster.campus.ui.faculty.FacultyActivity

class FacultyLoginFragment : Fragment() {

    private var _binding: FragmentFacultyLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignup: TextView
    private lateinit var btnLogin: Button
    private lateinit var txtForgotPass: TextView
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFacultyLoginBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        etEmail = binding.etFLoginEmail
        etPassword = binding.etFLoginPassword
        btnLogin = binding.btnfacultyLogin
        btnSignup = binding.btnGoToSignup
        txtForgotPass = binding.txtForgotPassword

        login()
        btnSignup.setOnClickListener {
            findNavController().navigate(R.id.action_facultyLoginFragment_to_facultySignupFragment)
        }
        txtForgotPass.setOnClickListener {
            findNavController().navigate(R.id.action_facultyLoginFragment_to_forgotFragment)
        }

        return binding.root
    }
    private fun login() {
        btnLogin.setOnClickListener {
            when {
                etEmail.text.isEmpty() -> {
                    etEmail.error = "Fill this"
                    return@setOnClickListener
                }
                etPassword.text.isEmpty() -> {
                    etPassword.error = "Fill this"
                }
                else -> {
                    auth.signInWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
                        .addOnCompleteListener(requireActivity()) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("LoginSign", "signInWithEmail:success")
                                val user = auth.currentUser
                                updateUI(user)
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("LoginSign", "signInWithEmail:failure", task.exception)
                                Toast.makeText(
                                    requireContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                updateUI(null)
                            }
                        }
                }
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(requireContext(), FacultyActivity::class.java))
            requireActivity().finish()
        }
    }
}
