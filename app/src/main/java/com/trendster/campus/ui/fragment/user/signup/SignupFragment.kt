package com.trendster.campus.ui.fragment.user.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import cn.xm.weidongjian.progressbuttonlib.ProgressButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.trendster.campus.R
import com.trendster.campus.databinding.FragmentSignupBinding
import com.trendster.campus.ui.fragment.onboarding.OnboardingActivity
import com.trendster.campus.viewmodels.userviewmodel.UserViewModel

class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private lateinit var etEmail: TextInputLayout
    private lateinit var etName: TextInputLayout
    private lateinit var etBranch: TextInputLayout
    private lateinit var etRollNo: TextInputLayout
    private lateinit var etSemester: TextInputLayout
    private lateinit var etPassword: TextInputLayout
    private lateinit var btnSignup: ProgressButton

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

        binding.btnBackSignup.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnGoToLoginHere.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    private fun signUp() {
        btnSignup.setOnClickListener {
            when {
                etEmail.editText?.text?.isEmpty() == true -> {
                    etEmail.error = "Fill this"
                    return@setOnClickListener
                }
                etName.editText?.text?.isEmpty() == true -> {
                    etName.error = "Fill this"
                    return@setOnClickListener
                }
                etRollNo.editText?.text?.isEmpty() == true -> {
                    etRollNo.error = "Fill this"
                    return@setOnClickListener
                }
                etBranch.editText?.text?.isEmpty() == true -> {
                    etBranch.error = "Fill this"
                    return@setOnClickListener
                }
                etSemester.editText?.text?.isEmpty() == true -> {
                    etSemester.error = "Fill this"
                    return@setOnClickListener
                }
                etPassword.editText?.text?.isEmpty() == true -> {
                    etPassword.error = "Fill this"
                    return@setOnClickListener
                }
                else -> {

                    btnSignup.startRotate()
                    branch = etBranch.editText?.text.toString()
                    semester = etSemester.editText?.text.toString()

                    auth.createUserWithEmailAndPassword(etEmail.editText?.text.toString(), etPassword.editText?.text.toString())
                        .addOnCompleteListener(requireActivity()) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("SIGNUP", "createUserWithEmail:success")
                                val user = auth.currentUser
                                updateUI(user, etName.editText?.text.toString(), etRollNo.editText?.text.toString(), branch, semester)
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("SIGNUP", "createUserWithEmail:failure", task.exception)
                                btnSignup.animError()
                                Toast.makeText(
                                    requireContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                btnSignup.removeDrawable()
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
            userViewModel.saveUserData(user.uid, userName, userRollNo, userBranch, userSemester, accessLevel)
            btnSignup.animFinish()
            startActivity(Intent(requireContext(), OnboardingActivity::class.java))
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
