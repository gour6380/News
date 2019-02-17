package com.gourav.news.data

import android.content.Context

import com.gourav.news.data.dao.HeadlinesDao
import com.gourav.news.data.dao.SavedDao
import com.gourav.news.data.dao.SourcesDao
import com.gourav.news.models.Article
import com.gourav.news.models.SavedArticle
import com.gourav.news.models.Source
import com.gourav.news.models.Specification
import com.gourav.news.network.NewsApiClient
import com.gourav.news.utils.AppExecutors
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import timber.log.Timber

class NewsRepository
private constructor(context: Context) {

    private val newsApiService: NewsApiClient = NewsApiClient.getInstance(context)
    private val headlinesDao: HeadlinesDao = NewsDatabase.getInstance(context).headlinesDao()
    private val sourcesDao: SourcesDao = NewsDatabase.getInstance(context).sourcesDao()
    private val savedDao: SavedDao = NewsDatabase.getInstance(context).savedDao()
    private val mExecutor: AppExecutors = AppExecutors.instance
    private val networkArticleLiveData: MutableLiveData<List<Article>> = MutableLiveData()
    private val networkSourceLiveData: MutableLiveData<List<Source>> = MutableLiveData()

    val saved: LiveData<List<Article>>
        get() = savedDao.allSaved

    init {
        networkArticleLiveData.observeForever { articles ->
            if (articles != null) {
                mExecutor.diskIO.execute { headlinesDao.bulkInsert(articles) }
            }
        }
        networkSourceLiveData.observeForever { sources ->
            if (sources != null) {
                mExecutor.diskIO.execute { sourcesDao.bulkInsert(sources) }
            }
        }
    }

    fun getHeadlines(specs: Specification): LiveData<List<Article>> {
        val networkData = newsApiService.getHeadlines(specs)
        networkData.observeForever(object : Observer<List<Article>> {
            override fun onChanged(articles: List<Article>?) {
                if (articles != null) {
                    networkArticleLiveData.value = articles
                    networkData.removeObserver(this)
                }
            }
        })
        return headlinesDao.getArticleByCategory(specs.category!!)
    }

    fun getSources(specs: Specification): LiveData<List<Source>> {
        val networkData = newsApiService.getSources(specs)
        networkData.observeForever(object : Observer<List<Source>> {
            override fun onChanged(sources: List<Source>?) {
                if (sources != null) {
                    networkSourceLiveData.value = sources
                    networkData.removeObserver(this)
                }
            }
        })
        return sourcesDao.allSources
    }

    fun isSaved(articleId: Int): LiveData<Boolean> {
        return savedDao.isFavourite(articleId)
    }

    fun removeSaved(articleId: Int) {
        mExecutor.diskIO.execute { savedDao.removeSaved(articleId) }
    }

    fun save(articleId: Int) {
        mExecutor.diskIO.execute {
            val savedArticle = SavedArticle(articleId)
            savedDao.insert(savedArticle)
            Timber.d("Saved in database for id  : %s", articleId)
        }
    }

    companion object {
        private val LOCK = Any()
        private var sInstance: NewsRepository? = null

        @Synchronized
        fun getInstance(context: Context): NewsRepository {
            if (sInstance == null) {
                synchronized(LOCK) {
                    sInstance = NewsRepository(context)
                }
            }
            return sInstance!!
        }
    }
}