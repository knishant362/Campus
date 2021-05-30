package com.trendster.campus.ui.fragment.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.trendster.campus.R
import com.trendster.campus.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container,false)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.onboardingViewPager)
        binding.btnNextPage.setOnClickListener {
            viewPager?.currentItem = 1
        }

        return binding.root

    }
}
