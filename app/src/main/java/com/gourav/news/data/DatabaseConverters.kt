package com.gourav.news.data

import java.sql.Timestamp

import androidx.room.TypeConverter

object DatabaseConverters {
    @JvmStatic
    @TypeConverter
    fun toJavaTimestamp(timestamp: Long?): Timestamp? {
        return if (timestamp == null) null else Timestamp(timestamp)
    }
    @JvmStatic
    @TypeConverter
    fun toDatabaseTimestamp(timestamp: Timestamp?): Long {
        return timestamp?.time ?: Timestamp(System.currentTimeMillis()).time
    }
}
