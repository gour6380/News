package com.gourav.news.ui.sources

import android.app.Application

import com.gourav.news.data.NewsRepository
import com.gourav.news.models.Source
import com.gourav.news.models.Specification
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class SourceViewModel(application: Application) : AndroidViewModel(application) {
    private val newsRepository: NewsRepository = NewsRepository.getInstance(application.applicationContext)

    internal fun getSource(specification: Specification): LiveData<List<Source>> {
        return newsRepository.getSources(specification)
    }
}
