package com.gourav.news.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Dao

import com.gourav.news.models.Source

@Dao
interface SourcesDao {

    @get:Query("SELECT * FROM sources")
    val allSources: LiveData<List<Source>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun bulkInsert(sources: List<Source>)
}
