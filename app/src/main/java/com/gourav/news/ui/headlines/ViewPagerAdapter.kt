package com.gourav.news.ui.headlines

import com.gourav.news.network.NewsApi
import com.gourav.news.ui.news.NewsFragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fm: FragmentManager, categories: Array<String>) : FragmentPagerAdapter(fm) {
    private val newsFragments: Array<NewsFragment?> = arrayOfNulls(categories.size)


    init {
        for (i in categories.indices) {
            newsFragments[i] = NewsFragment.newInstance(NewsApi.Category.valueOf(categories[i]))
        }
    }

    override fun getItem(i: Int): Fragment {
        return newsFragments[i]!!
    }

    override fun getCount(): Int {
        return newsFragments.size
    }
}
