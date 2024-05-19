package com.example.individualproject.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.individualproject.databinding.FragmentContainerBinding
import com.example.individualproject.ui.container.ContainerFragment

class ContainerAdapter(
    fragment: ContainerFragment,
    val tabs: List<Fragment>
): FragmentStateAdapter(fragment) {

    override fun getItemCount() = tabs.size

    override fun createFragment(position: Int): Fragment {
        return tabs[position]
    }
}