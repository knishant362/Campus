package com.trendster.campus.ui.fragment.schedule.attendance

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trendster.campus.R
import com.trendster.campus.databinding.FragmentAttendanceBinding

class AttendanceFragment : Fragment() {

    private var _binding : FragmentAttendanceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAttendanceBinding.inflate(inflater, container, false)







        return binding.root
    }

}