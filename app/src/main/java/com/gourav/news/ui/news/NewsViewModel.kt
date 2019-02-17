package com.gourav.news.ui.news

import android.app.Application

import com.gourav.news.data.NewsRepository
import com.gourav.news.models.Article
import com.gourav.news.models.Specification
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class NewsViewModel(application: Application) : AndroidViewModel(application) {
    private val newsRepository: NewsRepository = NewsRepository.getInstance(application.applicationContext)

    val allSaved: LiveData<List<Article>>
        get() = newsRepository.saved

    fun getNewsHeadlines(specification: Specification): LiveData<List<Article>> {
        return newsRepository.getHeadlines(specification)
    }
}
