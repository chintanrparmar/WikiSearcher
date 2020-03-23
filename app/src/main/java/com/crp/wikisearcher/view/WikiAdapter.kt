package com.crp.wikisearcher.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.crp.wikisearcher.databinding.WikiSearchItemBinding
import com.crp.wikisearcher.datamodel.Page

class WikiAdapter(private val list: List<Page>, val adapterOnClick: (Any) -> Unit) :
    RecyclerView.Adapter<WikiAdapter.WikiView>() {

    inner class WikiView(private val binding: WikiSearchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(page: Page) {
            binding.itemTitle.text = page.title
            binding.itemDesc.text = page.extract
            binding.itemLogo.load(page.thumbnail?.source)

            binding.root.setOnClickListener {
                page.title?.let { it1 -> adapterOnClick(it1) }
            }
        }
    }


    override fun getItemCount() = list.size


    override fun onBindViewHolder(holder: WikiView, position: Int) = holder.bind(list[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = WikiView(
        WikiSearchItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

}