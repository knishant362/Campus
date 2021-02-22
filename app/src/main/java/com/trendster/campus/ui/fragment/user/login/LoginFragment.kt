package com.trendster.campus.ui.fragment.user.login

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.trendster.campus.R
import com.trendster.campus.databinding.FragmentLoginBinding
import com.trendster.campus.ui.MainActivity
import com.trendster.campus.utils.CustomDialog

class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var etEmail : EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignup: Button
    private lateinit var btnLogin: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        etEmail = binding.etLoginEmail
        etPassword = binding.etLoginPassword
        btnLogin = binding.btnLogin
        btnSignup = binding.btnGoToSignup

        login()
        btnSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }


        return binding.root
    }

    private fun login() {
        btnLogin.setOnClickListener {
            when{

                etEmail.text.isEmpty() -> {
                    etEmail.error = "Fill this"
                    return@setOnClickListener
                }
                etPassword.text.isEmpty() ->{
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
                            }else {
                                // If sign in fails, display a message to the user.
                                Log.w("LoginSign", "signInWithEmail:failure", task.exception)
                                Toast.makeText(requireContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                                updateUI(null)
                            }
                        }
                }

            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null){
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        }

    }

}