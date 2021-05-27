package com.trendster.campus.ui.fragment.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trendster.campus.adapters.NotificationAdapter
import com.trendster.campus.databinding.FragmentNotificationsBinding
import com.trendster.campus.viewmodels.mainviewmodel.MainViewModel

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var mAdapter: NotificationAdapter
    private lateinit var notifRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        notifRecyclerView = binding.notificationsRecycler
        mAdapter = NotificationAdapter()
        notifRecyclerView.adapter = mAdapter
        notifRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        mainViewModel.loadNotifications()
        mainViewModel.readNotifications.observe(
            viewLifecycleOwner,
            { data ->
                mAdapter.setData(data)
                mAdapter.notifyDataSetChanged()
            }
        )

        return binding.root
    }
}
