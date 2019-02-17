package com.gourav.news.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gourav.news.R
import com.gourav.news.databinding.SourceItemBinding
import com.gourav.news.models.Source
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class SourceAdapter(private var sources: List<Source>?, private val listener: SourceAdapterListener) : RecyclerView.Adapter<SourceAdapter.SourceViewHolder>() {
    private var layoutInflater: LayoutInflater? = null
    private var mExpandedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): SourceViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater!!, R.layout.source_item, parent, false)
        return SourceViewHolder(binding as SourceItemBinding)
    }

    override fun onBindViewHolder(sourceViewHolder: SourceViewHolder, i: Int) {
        sourceViewHolder.binding.source = sources!![i]
        val position = sourceViewHolder.adapterPosition
        val isExpanded = position == mExpandedPosition
        sourceViewHolder.binding.tvSourceDesc.visibility = if (isExpanded) View.VISIBLE else View.GONE
        sourceViewHolder.binding.btnOpen.visibility = if (isExpanded) View.VISIBLE else View.GONE
        sourceViewHolder.binding.root.isActivated = isExpanded
        sourceViewHolder.binding.root.setOnClickListener {
            mExpandedPosition = if (isExpanded) -1 else position
            notifyDataSetChanged()
        }
        sourceViewHolder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return if (sources == null) 0 else sources!!.size
    }

    fun setSources(sources: List<Source>?) {
        if (sources != null) {
            this.sources = sources
            notifyDataSetChanged()
        }
    }

    interface SourceAdapterListener {
        fun onSourceButtonClicked(source: Source)
    }

    inner class SourceViewHolder(val binding: SourceItemBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            this.binding.btnOpen.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            listener.onSourceButtonClicked(this.binding.source!!)
        }
    }
}
