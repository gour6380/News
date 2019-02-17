package com.gourav.news.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Dao

import com.gourav.news.models.Article
import com.gourav.news.models.SavedArticle

@Dao
interface SavedDao {

    @get:Query("SELECT articles.* FROM articles, saved " +
            "WHERE articles.id == saved.news_id " +
            "ORDER BY saved.time_saved")
    val allSaved: LiveData<List<Article>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(article: SavedArticle)

    @Query("SELECT COUNT(news_id) > 0 FROM saved WHERE news_id = :articleId")
    fun isFavourite(articleId: Int): LiveData<Boolean>

    @Query("DELETE FROM saved WHERE news_id=:articleId")
    fun removeSaved(articleId: Int)
}
