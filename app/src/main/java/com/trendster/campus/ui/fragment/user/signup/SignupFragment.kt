package com.trendster.campus.ui.fragment.user.signup

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
import androidx.fragment.app.activityViewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.trendster.campus.R
import com.trendster.campus.databinding.FragmentSignupBinding
import com.trendster.campus.ui.MainActivity
import com.trendster.campus.viewmodels.MainViewModel

class SignupFragment : Fragment() {

    private var _binding : FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private lateinit var etEmail : EditText
    private lateinit var etName : EditText
    private lateinit var etBranch: EditText
    private lateinit var etSemester: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignup: Button

    private lateinit var auth: FirebaseAuth
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSignupBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        etEmail = binding.etSignupEmail
        etName = binding.etSignupName
        etBranch = binding.etSignupBranch
        etSemester = binding.etSignupSem
        etPassword = binding.etPassword
        btnSignup = binding.btnSignup

        signin()

        return binding.root
    }

    private fun signin() {
        btnSignup.setOnClickListener {
            when{
                etEmail.text.isEmpty() -> {
                    etEmail.error = "Fill this"
                    return@setOnClickListener
                }
                etName.text.isEmpty() -> {
                    etName.error = "Fill this"
                    return@setOnClickListener
                }
                etBranch.text.isEmpty() -> {
                    etBranch.error = "Fill this"
                }
                etSemester.text.isEmpty() -> {
                    etSemester.error = "Fill this"
                }
                etPassword.text.isEmpty() -> {
                    etPassword.error = "Fill this"
                }
                else -> {
                    auth.createUserWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
                        .addOnCompleteListener(requireActivity()) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("SIGNUP", "createUserWithEmail:success")
                                val user = auth.currentUser
                                updateUI(user,etName.text.toString(),etBranch.text.toString(),etSemester.text.toString())
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("SIGNUP", "createUserWithEmail:failure", task.exception)
                                Toast.makeText(requireContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()

                            }

                        }
                }

            }
        }
    }

    private fun updateUI(
        user: FirebaseUser?,
        userName: String,
        userBranch: String,
        userSemester: String
    ) {
        if (user != null){
            mainViewModel.saveUserData(user!!.uid, userName, userBranch, userSemester)
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        }
    }
}