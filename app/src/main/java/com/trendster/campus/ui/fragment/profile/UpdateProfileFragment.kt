package com.trendster.campus.ui.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.trendster.campus.R
import com.trendster.campus.databinding.FragmentUpdateProfileBinding
import com.trendster.campus.utils.DEFAULT_USER_BRANCH
import com.trendster.campus.utils.DEFAULT_USER_SEMESTER
import com.trendster.campus.viewmodels.MainViewModel

class UpdateProfileFragment : Fragment() {

    private var _binding : FragmentUpdateProfileBinding? = null
    private val binding get() = _binding!!

    private var branchChip = DEFAULT_USER_BRANCH
    private var semesterChip = DEFAULT_USER_SEMESTER
    private val mainViewModel : MainViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var profileName: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentUpdateProfileBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        val userUID = auth.currentUser?.uid
        profileName = binding.etprofileName

        binding.chipGroupBranch.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val selectedBranchChip = chip.text.toString()
            branchChip = selectedBranchChip
        }

        binding.chipGroupSemester.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val selectedSemesterChip = chip.text.toString()
            semesterChip = selectedSemesterChip
        }

        updateUserInfo(userUID)



        return binding.root
    }

    private fun updateUserInfo(userUID: String?) {
        binding.btnApply.setOnClickListener {
            when{
                profileName.text.isEmpty() -> {
                    profileName.error = "Fill this"
                    return@setOnClickListener
                }
                else -> {
                    if (userUID != null) {
                        mainViewModel.updateUserProfile(
                            userUID,
                            profileName.text.toString(),
                            branchChip,
                            semesterChip
                        )
                        findNavController().navigate(R.id.action_updateProfileFragment_to_profileFragment)
                    }
                }
            }


        }
    }

}