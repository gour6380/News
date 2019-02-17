package com.gourav.news.widget

import android.annotation.SuppressLint
import android.app.IntentService
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent

import com.gourav.news.data.NewsRepository
import com.gourav.news.models.Article
import androidx.lifecycle.Observer

class SavedNewsService : IntentService("SavedNewsService") {

    @SuppressLint("WrongThread")
    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            val action = intent.action
            when {
                ACTION_GET_NEXT == action -> {
                    val param1 = intent.getIntExtra(PARAM_CURRENT, 0)
                    handleActionNext(param1)
                }
                ACTION_GET_PREVIOUS == action -> {
                    val param1 = intent.getIntExtra(PARAM_CURRENT, 0)
                    handleActionPrevious(param1)
                }
                ACTION_UPDATE_WIDGETS == action -> {
                    val articlesLiveData = NewsRepository.getInstance(applicationContext).saved
                    articlesLiveData.observeForever(object : Observer<List<Article>> {
                        override fun onChanged(articles: List<Article>?) {
                            if (articles != null && articles.isNotEmpty()) {
                                handleUpdateWidgets(articles, 0)
                                articlesLiveData.removeObserver(this)
                            }
                        }
                    })
                }
            }
        }
    }


    private fun handleActionNext(currentIndex: Int) {
        val articlesLiveData = NewsRepository.getInstance(applicationContext).saved
        articlesLiveData.observeForever(object : Observer<List<Article>> {
            override fun onChanged(articles: List<Article>?) {
                if (articles != null && articles.size > currentIndex + 1) {
                    handleUpdateWidgets(articles, currentIndex + 1)
                    articlesLiveData.removeObserver(this)
                }
            }
        })
    }

    private fun handleActionPrevious(currentIndex: Int) {
        val articlesLiveData = NewsRepository.getInstance(applicationContext).saved
        articlesLiveData.observeForever(object : Observer<List<Article>> {
            override fun onChanged(articles: List<Article>?) {
                if (articles != null && articles.size > 0 && currentIndex > 0) {
                    handleUpdateWidgets(articles, currentIndex - 1)
                    articlesLiveData.removeObserver(this)
                }
            }
        })
    }

    private fun handleUpdateWidgets(articles: List<Article>?, selected: Int) {
        val appWidgetManager = AppWidgetManager.getInstance(applicationContext)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(applicationContext, SavedNewsWidget::class.java))
        SavedNewsWidget.updateNewsWidgets(applicationContext, appWidgetManager, articles!!, selected, appWidgetIds)
    }

    companion object {

        const val ACTION_GET_NEXT = "com.example.abhishek.newsapp.widget.action.saved.next"
        const val ACTION_GET_PREVIOUS = "com.example.abhishek.newsapp.widget.action.saved.previous"
        private const val ACTION_UPDATE_WIDGETS = "com.example.abhishek.newsapp.widget.action.update_widgets"

        const val PARAM_CURRENT = "com.example.abhishek.newsapp.widget.extra.current_selected"

    }
}
