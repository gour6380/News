package com.gourav.news.ui.sources

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.gourav.news.R
import com.gourav.news.adapters.SourceAdapter
import com.gourav.news.models.Source
import com.gourav.news.models.Specification
import java.util.Locale
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import com.gourav.news.databinding.FragmentSourceBinding

class SourceFragment : Fragment(), SourceAdapter.SourceAdapterListener {

    private val sourceAdapter = SourceAdapter(null, this)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater,
                R.layout.fragment_source, container, false) as FragmentSourceBinding

        setupViewModel()
        binding.rvSources.adapter = sourceAdapter
        if (context != null) {
            val divider = DividerItemDecoration(context!!, DividerItemDecoration.VERTICAL)
            divider.setDrawable(resources.getDrawable(R.drawable.recycler_view_divider))
            binding.rvSources.addItemDecoration(divider)
        }

        return binding.root
    }

    private fun setupViewModel() {
        val viewModel = ViewModelProviders.of(this).get(SourceViewModel::class.java)
        val specification = Specification()
        specification.language = Locale.getDefault().language
        specification.country = null.toString()
        viewModel.getSource(specification).observe(this, Observer { sources ->
            if (sources != null) {
                sourceAdapter.setSources(sources)
            }
        })
    }


    override fun onSourceButtonClicked(source: Source) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(source.url))
        startActivity(intent)
    }

    companion object {

        fun newInstance(): SourceFragment {
            return SourceFragment()
        }
    }
}
