package com.trendster.campus.ui.fragment.user.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.trendster.campus.R
import com.trendster.campus.databinding.FragmentSignupBinding
import com.trendster.campus.ui.MainActivity
import com.trendster.campus.viewmodels.userviewmodel.UserViewModel

class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private lateinit var etEmail: EditText
    private lateinit var etName: EditText
    private lateinit var etBranch: TextInputLayout
    private lateinit var etRollNo: EditText
    private lateinit var etSemester: TextInputLayout
    private lateinit var etPassword: EditText
    private lateinit var btnSignup: Button

    private var branch = "CSE"
    private var semester = "1"

    private lateinit var auth: FirebaseAuth
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSignupBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        etEmail = binding.etSignupEmail
        etName = binding.etSignupName
        etRollNo = binding.etSignupRoll
        etBranch = binding.textInputBranchLayout
        etSemester = binding.textInputSemesterLayout
        etPassword = binding.etPassword
        btnSignup = binding.btnSignup

        setupAdapter()

        signUp()

        return binding.root
    }

    private fun signUp() {
        btnSignup.setOnClickListener {
            when {
                etEmail.text.isEmpty() -> {
                    etEmail.error = "Fill this"
                    return@setOnClickListener
                }
                etName.text.isEmpty() -> {
                    etName.error = "Fill this"
                    return@setOnClickListener
                }
                etRollNo.text.isEmpty() -> {
                    etRollNo.error = "Fill this"
                }
                etBranch.editText?.text?.isEmpty() == true -> {
                    etBranch.error = "Fill this"
                }
                etSemester.editText?.text?.isEmpty() == true -> {
                    etSemester.error = "Fill this"
                }
                etPassword.text.isEmpty() -> {
                    etPassword.error = "Fill this"
                }
                else -> {

                    branch = etBranch.editText?.text.toString()
                    semester = etSemester.editText?.text.toString()

                    auth.createUserWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
                        .addOnCompleteListener(requireActivity()) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("SIGNUP", "createUserWithEmail:success")
                                val user = auth.currentUser
                                updateUI(user, etName.text.toString(), etRollNo.text.toString(), branch, semester)
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("SIGNUP", "createUserWithEmail:failure", task.exception)
                                Toast.makeText(
                                    requireContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }
    }

    private fun updateUI(
        user: FirebaseUser?,
        userName: String,
        userRollNo: String,
        userBranch: String,
        userSemester: String
    ) {
        if (user != null) {
            val accessLevel = "user"
            userViewModel.saveUserData(user!!.uid, userName, userRollNo, userBranch, userSemester, accessLevel)
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun setupAdapter() {
        val branchArray = resources.getStringArray(R.array.branch)
        val semesterArray = resources.getStringArray(R.array.semester)

        val branchAdapter = ArrayAdapter(requireContext(), R.layout.exposed_menu_item, branchArray)
        val semesterAdapter = ArrayAdapter(requireContext(), R.layout.exposed_menu_item, semesterArray)
        binding.inputBranch.setAdapter(branchAdapter)
        binding.inputSemester.setAdapter(semesterAdapter)
    }
}
