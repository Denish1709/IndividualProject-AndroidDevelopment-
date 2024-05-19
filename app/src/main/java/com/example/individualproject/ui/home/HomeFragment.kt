package com.example.individualproject.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.view.isInvisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Query
import com.example.individualproject.data.model.Word
import com.example.individualproject.databinding.AlertSortViewBinding
import com.example.individualproject.databinding.FragmentHomeBinding
import com.example.individualproject.ui.adapter.WordAdapter
import com.example.individualproject.ui.container.ContainerFragmentDirections
import com.example.individualproject.ui.container.ContainerViewModel
import com.example.individualproject.ui.details.DetailFragment
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: WordAdapter
    private lateinit var originalWord: List<Word>

    private val viewModel: HomeViewModel by viewModels{
        HomeViewModel.Factory
    }

    private val parentViewModel: ContainerViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    private var selectedSortBy = "Title"
    private var selectedSortOrder = "asc"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        observeHomeViewModel()

        lifecycleScope.launch {
            parentViewModel.refreshHome.collect{
                viewModel.getAllWord()

            }
        }

        binding.ivSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterWords(newText)
                return true
            }
        })

        binding.ivSort.setOnClickListener{
            val alertView = AlertSortViewBinding.inflate(layoutInflater)
            val deleteDialog = AlertDialog.Builder(requireContext())
            deleteDialog.setView(alertView.root)
            val dialog = deleteDialog.create()

            alertView.rbTitle.setOnClickListener {
                alertView.rbDate.isChecked = false
                selectedSortBy = "title"
            }
            alertView.rbDate.setOnClickListener {
                alertView.rbTitle.isChecked = false
                selectedSortBy = "date"
            }

            alertView.rbAscending.setOnClickListener{
                alertView.rbDescending.isChecked = false
                selectedSortOrder = "asc"
            }

            alertView.rbDescending.setOnClickListener {
                alertView.rbAscending.isChecked = false
                selectedSortOrder = "desc"
            }

            alertView.btnDone.setOnClickListener {
                val sortedList = originalWord.sortedWith(compareBy<Word> {
                    if (selectedSortBy == "date") it.date else it.title
                }.run {
                    if (selectedSortOrder == "asc") this else reversed()
                })

                adapter.setWords(sortedList)
                dialog.dismiss()
            }

            dialog.show()
        }

        binding.fabAdd.setOnClickListener{
            findNavController().navigate(
                ContainerFragmentDirections.actionContainerFragmentToAddFragment()
            )
        }
    }

    private fun setAdapter() {
        adapter = WordAdapter(emptyList())
        adapter.listener = object : WordAdapter.Listener{
            override fun onClick(word: Word) {
                findNavController().navigate(
                    ContainerFragmentDirections.actionContainerFragmentToDetailFragment(word.id!!)
                )
            }
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvFiles.adapter = adapter
        binding.rvFiles.layoutManager = layoutManager

    }

    private fun observeHomeViewModel() {
        viewModel.getAllWord()

        viewModel.words.observe(viewLifecycleOwner) { words ->
            originalWord = words
            adapter.setWords(words)
            binding.tvNoNewWords.isInvisible = adapter.itemCount != 0
        }

        lifecycleScope.launch {
            parentViewModel.refreshHome.collect {
                viewModel.getAllWord()
            }
        }
    }

    private fun filterWords(query: String?) {
        if (query.isNullOrBlank()) {
            adapter.setWords(originalWord)
        } else {
            val filteredWords = originalWord.filter {
                it.title.contains(query, ignoreCase = true)
            }
            adapter.setWords(filteredWords)
        }
    }

}