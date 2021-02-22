package com.trendster.campus.ui.fragment.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.trendster.campus.R
import com.trendster.campus.databinding.FragmentProfileBinding
import com.trendster.campus.ui.UserActivity
import com.trendster.campus.utils.USER_BRANCH
import com.trendster.campus.utils.USER_NAME
import com.trendster.campus.utils.USER_SEMESTER
import com.trendster.campus.viewmodels.MainViewModel

class ProfileFragment : Fragment() {

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel : MainViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        val userUID = auth.currentUser?.uid
        if (userUID!=null){
            mainViewModel.loadUserProfile(userUID)
        }
        mainViewModel.readUserProfile.observe(viewLifecycleOwner, {user ->
            val data = user?.data

            if (data != null) {
                binding.txtProfileName.text = data[USER_NAME] as CharSequence?
                binding.txtProfileBranch.text = data[USER_BRANCH] as CharSequence?
                binding.txtProfileSemester.text = data[USER_SEMESTER] as CharSequence?
            }
        })

        binding.fabUpdate.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_updateProfileFragment)
        }

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(requireContext(), UserActivity::class.java))
            requireActivity().finish()
        }

        return binding.root
    }

}