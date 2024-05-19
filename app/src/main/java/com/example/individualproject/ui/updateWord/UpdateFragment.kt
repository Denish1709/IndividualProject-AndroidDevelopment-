package com.example.individualproject.ui.updateWord

import android.os.Bundle
import android.provider.SyncStateContract.Constants
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.individualproject.R
import com.example.individualproject.databinding.FragmentUpdateBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class UpdateFragment : Fragment() {

    private lateinit var binding: FragmentUpdateBinding
    private val updateViewModel: UpdateViewModel by viewModels { UpdateViewModel.Factory }
    private var selectedWordId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpdateBinding.inflate(
            layoutInflater,
            container,
            false
        )

        updateViewModel.selectedWord.observe(viewLifecycleOwner) { word ->
            word.let {
                updateViewModel.setWord(it)
            }
        }

        arguments?.let {
            selectedWordId = UpdateFragmentArgs.fromBundle(it).id
            updateViewModel.getWordById(selectedWordId)
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = updateViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        lifecycleScope.launch {
            updateViewModel.finish.collect{
                Log.d("updateFragment", "Update Finished")
                findNavController().popBackStack()
            }
        }

        updateViewModel.snackbar.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }


}