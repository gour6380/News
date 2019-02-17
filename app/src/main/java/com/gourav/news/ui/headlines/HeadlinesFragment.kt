package com.gourav.news.ui.headlines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.material.tabs.TabLayout
import com.gourav.news.R
import com.gourav.news.databinding.FragmentHeadlinesBinding
import com.gourav.news.network.NewsApi
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

class HeadlinesFragment : Fragment() {
    private val categories = arrayOf(NewsApi.Category.General.name, NewsApi.Category.Business.name, NewsApi.Category.Sports.name, NewsApi.Category.Health.name, NewsApi.Category.Entertainment.name, NewsApi.Category.Technology.name, NewsApi.Category.Science.name)
    private val categoryIcons = intArrayOf(R.drawable.ic_headlines, R.drawable.nav_business, R.drawable.nav_sports, R.drawable.nav_health, R.drawable.nav_entertainment, R.drawable.nav_tech, R.drawable.nav_science)
    private var binding: FragmentHeadlinesBinding? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        this.binding = DataBindingUtil.inflate<ViewDataBinding>(inflater,
                R.layout.fragment_headlines, container, false) as FragmentHeadlinesBinding?

        ViewCompat.setElevation(binding!!.tablayoutHeadlines, resources.getDimension(R.dimen.tab_layout_elevation))

        if (activity != null) {
            val viewPager = ViewPagerAdapter(childFragmentManager, categories)
            binding!!.viewpagerHeadlines.adapter = viewPager
            binding!!.tablayoutHeadlines.setupWithViewPager(binding!!.viewpagerHeadlines)
            setupTabIcons()
        }
        return this.binding!!.root
    }

    private fun setupTabIcons() {
        var tab: TabLayout.Tab?
        for (i in categories.indices) {
            tab = binding!!.tablayoutHeadlines.getTabAt(i)
            if (tab != null) {
                tab.setIcon(categoryIcons[i]).text = categories[i]
            }
        }
    }

    companion object {

        fun newInstance(): HeadlinesFragment {
            return HeadlinesFragment()
        }
    }
}
