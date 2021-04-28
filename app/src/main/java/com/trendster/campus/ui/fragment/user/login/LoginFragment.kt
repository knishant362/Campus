package com.trendster.campus.ui.fragment.user.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.trendster.campus.R
import com.trendster.campus.databinding.FragmentLoginBinding
import com.trendster.campus.ui.MainActivity

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignup: TextView
    private lateinit var btnLogin: Button
    private lateinit var txtForgotPass: TextView
    private lateinit var auth: FirebaseAuth

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            startActivity(Intent(requireContext(), MainActivity::class.java))
            activity?.finish()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        etEmail = binding.etEmail
        etPassword = binding.etPasswordd
        btnLogin = binding.btnLogin
        btnSignup = binding.btnGoToSignup
        txtForgotPass = binding.txtForgotPassword

        login()
        btnSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
        txtForgotPass.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotFragment)
        }
        binding.btnfacultyLogin.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_facultyLoginFragment)
        }

        return binding.root
    }

    private fun login() {
        btnLogin.setOnClickListener {
            when {

                etEmail.text.isEmpty() -> {
                    binding.etEmail.error = "Fill this"
//                    etEmail.error = "Fill this"
                    return@setOnClickListener
                }
                etPassword.text.isEmpty() -> {
                    binding.etEmail.error = "Fill this"
//                    etPassword.error = "Fill this"
                    return@setOnClickListener
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
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        }
    }
}
