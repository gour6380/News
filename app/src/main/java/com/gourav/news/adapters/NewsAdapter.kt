package com.gourav.news.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.gourav.news.R
import com.gourav.news.databinding.NewsItemBinding
import com.gourav.news.models.Article
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

class NewsAdapter(private var articles: List<Article>?, private val listener: NewsAdapterListener) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    private var layoutInflater: LayoutInflater? = null

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): NewsViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val binding = DataBindingUtil.inflate<NewsItemBinding>(layoutInflater!!, R.layout.news_item, parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(newsViewHolder: NewsViewHolder, i: Int) {
        newsViewHolder.binding.news = articles!![i]
        newsViewHolder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return if (articles == null) 0 else articles!!.size
    }

    fun setArticles(articles: List<Article>?) {
        if (articles != null) {
            this.articles = articles
            notifyDataSetChanged()
        }
    }

    interface NewsAdapterListener {
        fun onNewsItemClicked(article: Article)

        fun onItemOptionsClicked(article: Article)
    }

    inner class NewsViewHolder(val binding: NewsItemBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            this.binding.ivOptions.setOnClickListener(this)
            this.binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val index = this.adapterPosition
            if (v is ImageView) {
                listener.onItemOptionsClicked(articles!![index])
            } else {
                listener.onNewsItemClicked(articles!![index])
            }
        }
    }
}
