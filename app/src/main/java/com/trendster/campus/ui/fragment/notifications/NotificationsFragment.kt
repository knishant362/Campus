package com.trendster.campus.ui.fragment.notifications

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.todkars.shimmer.ShimmerRecyclerView
import com.trendster.campus.R
import com.trendster.campus.adapters.NotificationAdapter
import com.trendster.campus.databinding.FragmentNoticeBinding
import com.trendster.campus.databinding.FragmentNotificationsBinding
import com.trendster.campus.viewmodels.MainViewModel

class NotificationsFragment : Fragment() {

    private var _binding : FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel : MainViewModel by activityViewModels()
    private lateinit var mAdapter: NotificationAdapter
    private lateinit var notifRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        notifRecyclerView = binding.notifRecycler
        mAdapter = NotificationAdapter()
        notifRecyclerView.adapter = mAdapter
        notifRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        mainViewModel.readAccessLevel.observe(viewLifecycleOwner, {access ->
            if (access == "admin"){
                binding.fabNotification.visibility = View.VISIBLE
            }
        })

        binding.fabNotification.setOnClickListener {
            findNavController().navigate(R.id.action_notificationsFragment_to_notificationActivity)
        }

        mainViewModel.loadNotifications()
        mainViewModel.readNotifications.observe(viewLifecycleOwner, {data ->
            mAdapter.setData(data)
            mAdapter.notifyDataSetChanged()
        })

        return binding.root
    }
}