package com.example.individualproject.ui.details

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.individualproject.R
import com.example.individualproject.databinding.AlertCompletedViewBinding
import com.example.individualproject.databinding.AlertDeleteWordViewBinding
import com.example.individualproject.databinding.FragmentDetailBinding
import kotlinx.coroutines.launch

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val detailViewModel: DetailViewModel by viewModels{ DetailViewModel.Factory }
    private var selectedWordId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(
            layoutInflater,
            container,
            false
        )
        arguments?.let {
            selectedWordId = DetailFragmentArgs.fromBundle(it).id
            detailViewModel.getWordById(selectedWordId)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeSelectedWord()
        setupButtons()
    }

    private fun observeSelectedWord() {
        lifecycleScope.launch {
            detailViewModel.selectedWord.observe(viewLifecycleOwner) { word ->
                word?.let {
                    binding.run {
                        tvTitleB.text = word.title
                        tvMeaning.text = word.meaning
                        tvSynonym.text = word.synonyms
                        tvDetail.text = word.details
                    }
                    updatedButtonTextAndBackground(word.status)
                }
            }
        }
    }

    private fun setupButtons() {
        binding.btnUpdate.setOnClickListener {
            findNavController().navigate(
                DetailFragmentDirections.actionDetailFragmentToUpdateFragment(selectedWordId)
            )
        }

        binding.btnDelete.setOnClickListener {
            val alertView = AlertDeleteWordViewBinding.inflate(layoutInflater)
            val deleteDialog = AlertDialog.Builder(requireContext())
            deleteDialog.setView(alertView.root)
            alertView.tvTitle.text = "Delete Confirmation"
            alertView.tvBody.text = "Are you sure you want to delete this word?"

            val temporaryDeleteDialog = deleteDialog.create()

            alertView.btnDelete.setOnClickListener {
                detailViewModel.delete()
                findNavController().navigate(
                    DetailFragmentDirections.actionDetailFragmentToContainerFragment()
                )
                Toast.makeText(
                    requireContext(),
                    "Succesfully Deleted",
                    Toast.LENGTH_SHORT
                ).show()
                temporaryDeleteDialog.dismiss()
            }

            alertView.btnCancel.setOnClickListener {
                temporaryDeleteDialog.dismiss()
            }
            temporaryDeleteDialog.show()
        }

        binding.btnDone.setOnClickListener {
            detailViewModel.selectedWord.value?.let { word ->
                val alertView = AlertCompletedViewBinding.inflate(layoutInflater)
                val deleteDialog = AlertDialog.Builder(requireContext())
                deleteDialog.setView(alertView.root)
                alertView.tvTitle.text = "Are you sure?"
                if (word.status == true) {
                    alertView.tvBody.text = "You want to move back this to new word?"
                } else {
                    alertView.tvBody.text = "You want to move this word to complete?"
                }

                val temporaryDeleteDialog = deleteDialog.create()

                alertView.btnYes.setOnClickListener {
                    detailViewModel.moveWord()
                    findNavController().navigate(
                        DetailFragmentDirections.actionDetailFragmentToContainerFragment()
                    )
                    temporaryDeleteDialog.dismiss()
                }

                alertView.btnNo.setOnClickListener {
                    temporaryDeleteDialog.dismiss()
                }
                temporaryDeleteDialog.show()
            }
        }
    }


    fun updatedButtonTextAndBackground(status: Boolean?) {
        status?.let {
            binding.btnDone.apply {
                text = if (it) "Undo" else "Done"
                backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        if (it) R.color.green else R.color.red
                    )
                )
            }
        }
    }

}