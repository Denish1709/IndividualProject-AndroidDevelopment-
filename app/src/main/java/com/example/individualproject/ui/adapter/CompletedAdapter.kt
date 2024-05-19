package com.example.individualproject.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.individualproject.data.model.Word
import com.example.individualproject.databinding.LayoutCompletedItemBinding

class CompletedAdapter(
    private var words: List<Word>
) : RecyclerView.Adapter<CompletedAdapter.CompletedWorkHolder>() {

    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedWorkHolder {
        val binding = LayoutCompletedItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CompletedWorkHolder(binding)
    }

    override fun getItemCount() = words.size

    override fun onBindViewHolder(holder: CompletedWorkHolder, position: Int) {
        val word = words[position]
        holder.bind(word)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setWords(words: List<Word>) {
        this.words = words
        notifyDataSetChanged()
    }

    inner class CompletedWorkHolder(
        private val binding: LayoutCompletedItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(word: Word) {
            binding.word = word
            binding.cvWorks.setOnClickListener {
                listener?.onClick(word)
            }
        }
    }

    interface Listener {
        fun onClick(word: Word)
    }
}