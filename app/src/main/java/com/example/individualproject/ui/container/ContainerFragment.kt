package com.example.individualproject.ui.container

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.example.individualproject.R
import com.example.individualproject.core.Constants
import com.example.individualproject.databinding.FragmentContainerBinding
import com.example.individualproject.ui.adapter.ContainerAdapter
import com.example.individualproject.ui.completed.CompletedFragment
import com.example.individualproject.ui.home.HomeFragment
import com.google.android.material.tabs.TabLayoutMediator

class ContainerFragment : Fragment() {
    private lateinit var binding: FragmentContainerBinding
    private val viewModel: ContainerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentContainerBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vpTabs.adapter = ContainerAdapter(this, listOf(HomeFragment(), CompletedFragment()))

        TabLayoutMediator(binding.tlTabs, binding.vpTabs) {tab, position ->
            when (position) {
                0 -> tab.text = "New Word"
                else -> tab.text = "Completed Words"
            }
        }.attach()

        setFragmentResultListener(Constants.ADD_WORK_FRAGMENT) { _, result ->
            val refresh = result.getBoolean(Constants.REFRESH, true)

            if(refresh) viewModel.refreshHome()
        }
    }

}