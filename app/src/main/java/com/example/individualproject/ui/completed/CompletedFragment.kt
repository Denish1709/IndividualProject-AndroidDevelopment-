package com.example.individualproject.ui.completed

import android.app.AlertDialog
import android.os.Bundle
import android.provider.SyncStateContract.Constants
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AlertDialogLayout
import androidx.appcompat.widget.SearchView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Query
import com.example.individualproject.R
import com.example.individualproject.data.model.Word
import com.example.individualproject.databinding.FragmentCompletedBinding
import com.example.individualproject.databinding.SortViewBinding
import com.example.individualproject.ui.adapter.CompletedAdapter
import com.example.individualproject.ui.container.ContainerFragmentDirections
import com.example.individualproject.ui.container.ContainerViewModel
import kotlinx.coroutines.launch

class CompletedFragment : Fragment() {

    private lateinit var binding: FragmentCompletedBinding
    private lateinit var adapter: CompletedAdapter
    private  var originalWord : List<Word> = emptyList()
    private val parentViewModel: ContainerViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )
    private val viewModel: CompletedViewModel by viewModels { CompletedViewModel.Factory }
    private var selectedSortBy = "title"
    private var selectedSortOrder = "asc"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCompletedBinding.inflate(
            layoutInflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()

        observeCompletedViewModel()

        lifecycleScope.launch {
            parentViewModel.refreshHome.collect {
                viewModel.getCompletedWords()
            }
        }

        binding.ivSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterWords(newText)
                return false
            }

        })

        binding.ivSort.setOnClickListener{
            val alertView = SortViewBinding.inflate(layoutInflater)
            val deleteDialog = AlertDialog.Builder(requireContext())
            deleteDialog.setView(alertView.root)
            val dialog = deleteDialog.create()

            alertView.rbTitle.setOnClickListener{
                alertView.rbDate.isChecked = false
                selectedSortBy = "title"
            }

            alertView.rbDate.setOnClickListener{
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
    }

    private fun setupAdapter() {
        adapter = CompletedAdapter(emptyList())
        adapter.listener = object : CompletedAdapter.Listener{
            override fun onClick(word: Word) {
                findNavController().navigate(
                    ContainerFragmentDirections.actionContainerFragmentToDetailFragment(word.id!!)
                )
            }
        }
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvCompleted.adapter = adapter
        binding.rvCompleted.layoutManager = layoutManager
    }

    private fun observeCompletedViewModel() {
        viewModel.getCompletedWords()

        viewModel.words.observe(viewLifecycleOwner) { words ->
            originalWord = words
            adapter.setWords(words)
            binding.tvNoNewWords.isInvisible = adapter.itemCount != 0
        }

        lifecycleScope.launch {
            parentViewModel.refreshHome.collect{
                viewModel.getCompletedWords()
            }
        }
    }

    private fun filterWords(query: String?) {
        if (query.isNullOrBlank()) {
            adapter.setWords(originalWord)
        } else {
            val filteredWord = originalWord.filter {
                it.title.contains(query, ignoreCase = true)
            }
            adapter.setWords(filteredWord)
        }
    }

}