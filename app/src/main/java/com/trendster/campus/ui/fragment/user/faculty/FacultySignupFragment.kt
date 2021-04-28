package com.trendster.campus.ui.fragment.user.faculty

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.trendster.campus.databinding.FragmentFacultySignupBinding
import com.trendster.campus.viewmodels.userviewmodel.UserViewModel

class FacultySignupFragment : Fragment() {

    private var _binding: FragmentFacultySignupBinding? = null
    private val binding get() = _binding!!

    private lateinit var etEmail: EditText
    private lateinit var etName: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignup: Button

    private lateinit var auth: FirebaseAuth
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFacultySignupBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        etEmail = binding.etSignupEmail
        etName = binding.etSignupName
        etPassword = binding.etPassword
        btnSignup = binding.btnSignup

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
                                updateUI(user, etName.text.toString())
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
        userName: String
    ) {
        if (user != null) {
            userViewModel.saveFacultyData(user.uid, userName)
//            val action = subName?.let {
//                FacultySignupFragmentDirections.actionFacultyFragmentToStudentsFragment(
//                    it
//                )
//            }
//            if (action != null) {
//                Navigation.findNavController(v).navigate(action)
//            }
//            requireActivity().finish()

            findNavController().navigateUp()
        }
    }
}
