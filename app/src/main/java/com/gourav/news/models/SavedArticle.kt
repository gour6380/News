package com.gourav.news.models

import java.sql.Timestamp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = Article::class, parentColumns = ["id"], childColumns = ["news_id"])], indices = [Index(value = ["news_id"], unique = true)], tableName = "saved")
class SavedArticle(@field:ColumnInfo(name = "news_id")
                   val newsId: Int) {
    @PrimaryKey
    @ColumnInfo(name = "time_saved")
    var timestamp: Timestamp? = null

    init {
        this.timestamp = Timestamp(System.currentTimeMillis())
    }
}
