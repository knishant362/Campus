package com.trendster.campus.ui.fragment.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.trendster.campus.R
import com.trendster.campus.databinding.FragmentThirdBinding
import com.trendster.campus.ui.MainActivity

class ThirdFragment : Fragment() {

    private var _binding: FragmentThirdBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentThirdBinding.inflate(inflater, container, false)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.onboardingViewPager)
        binding.btnFinish.setOnClickListener {
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        }
        binding.btnNextPrevious.setOnClickListener {
            viewPager?.currentItem = 1
        }

        return binding.root
    }
}
