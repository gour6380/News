package com.gourav.news.data.dao

import com.gourav.news.models.Article

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Dao

@Dao
interface HeadlinesDao {

    @get:Query("SELECT * FROM articles")
    val allArticles: LiveData<List<Article>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun bulkInsert(articles: List<Article>)

    @Query("SELECT * FROM articles WHERE category=:category ORDER BY published_at DESC")
    fun getArticleByCategory(category: String): LiveData<List<Article>>
}
