package com.trendster.campus.ui.fragment.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.core.view.isEmpty
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.trendster.campus.R
import com.trendster.campus.databinding.FragmentProfileBinding
import com.trendster.campus.utils.USER_BRANCH
import com.trendster.campus.utils.USER_NAME
import com.trendster.campus.utils.USER_SEMESTER
import com.trendster.campus.viewmodels.mainviewmodel.MainViewModel

class ProfileFragment : DialogFragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth
    private var myBranch = "CSE"
    private var mySemester = "1"
    private var uploadStatus = "loading"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        val userUID = auth.currentUser?.uid
        if (userUID != null) {
            mainViewModel.loadUserProfile(userUID)
        }

        setupAdapter()

        mainViewModel.readUserProfile.observe(
            viewLifecycleOwner,
            { user ->
                val data = user?.data

                if (data != null) {
                    val userName = data[USER_NAME] as CharSequence?
                    val userBranch = data[USER_BRANCH] as CharSequence?
                    val userSemester = data[USER_SEMESTER] as CharSequence?

                    binding.txtStudentName.text = userName
                    binding.txtBranch.text = userBranch
                    binding.txtSeme.text = userSemester
                }
            }
        )

        binding.inputBranch.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent?.getItemAtPosition(position).toString()
            myBranch = selectedItem
//            showToast("Selected : $selectedItem")
        }

        binding.inputSemester.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent?.getItemAtPosition(position).toString()
            mySemester = selectedItem
//            showToast("Selected : $selectedItem")
        }

        if (userUID != null) {
            updateProfile(userUID)
        }

//        binding.btnLogout.setOnClickListener {
//            auth.signOut()
//            startActivity(Intent(requireContext(), UserActivity::class.java))
//            requireActivity().finish()
//        }

        return binding.root
    }

    private fun updateProfile(userUID: String) {

        binding.btnProfileUpdate.setOnClickListener {
            when {
                binding.textInputStudentName.editText?.text?.isEmpty() == true -> {
                    binding.textInputStudentName.error = "Fill this"
                    return@setOnClickListener
                }

                else -> {

                    val studentName = binding.textInputStudentName.editText?.text?.toString()
                    val studentBranch = binding.textInputStudentName.editText?.text?.toString()
                    val studentSemester = binding.textInputStudentName.editText?.text?.toString()

                    if (studentName != null && studentBranch != null && studentSemester != null) {
                        mainViewModel.updateUserProfile(
                            requireContext(),
                            userUID,
                            studentName,
                            myBranch,
                            mySemester,
                            findNavController(),
                        )

                        val imm: InputMethodManager =
                            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
                    }
                }
            }
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
