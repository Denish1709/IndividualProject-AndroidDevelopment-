package com.example.individualproject.ui.adapter

import android.location.GnssAntennaInfo.Listener
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.individualproject.data.model.Word
import com.example.individualproject.databinding.LayoutCardViewBinding
import com.example.individualproject.generated.callback.OnClickListener

class WordAdapter(
    private var words: List<Word>
): RecyclerView.Adapter<WordAdapter.WordViewHolder>() {

    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val binding = LayoutCardViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return WordViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return words.size
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word = words[position]
        holder.bind(word)
    }

    fun setWords(words: List<Word>) {
        this.words = words
        notifyDataSetChanged()
    }

    inner class WordViewHolder(
        private val binding: LayoutCardViewBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(word: Word) {
            binding.tvTitle.text = word.title
            binding.tvDefinition.text = word.meaning
            binding.cvWords.setOnClickListener {
                listener?.onClick(word)
            }
        }
    }

    interface Listener {
        fun onClick(word: Word)
    }
}