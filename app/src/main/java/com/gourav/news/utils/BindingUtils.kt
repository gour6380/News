@file:Suppress("NAME_SHADOWING")

package com.gourav.news.utils

import android.net.Uri
import android.text.TextUtils
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gourav.news.R
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*


object BindingUtils {

    @JvmStatic
    private fun getElapsedTime(utcTimeString: Long): String {
        var timeElapsedInSeconds = (System.currentTimeMillis() - utcTimeString) / 1000

        return when {
            timeElapsedInSeconds < 60 -> "less than 1m"
            timeElapsedInSeconds < 3600 -> {
                timeElapsedInSeconds /= 60
                timeElapsedInSeconds.toString() + "m"
            }
            timeElapsedInSeconds < 86400 -> {
                timeElapsedInSeconds /= 3600
                timeElapsedInSeconds.toString() + "h"
            }
            else -> {
                timeElapsedInSeconds /= 86400
                timeElapsedInSeconds.toString() + "d"
            }
        }
    }

    @JvmStatic
    fun getSourceAndTime(sourceName: String, date: Timestamp): String {
        return sourceName + " • " + getElapsedTime(date.time)
    }

    @BindingAdapter("bind:url", "bind:articleUrl")
    @JvmStatic
    fun loadThumbnailImage(imageView: ImageView, url: String?, articleUrl: String) {
        var url = url
        val context = imageView.context
        if (url == null) {
            val iconUrl = "https://besticon-demo.herokuapp.com/icon?url=%s&size=80..120..200"
            url = String.format(iconUrl, Uri.parse(articleUrl).authority)
        }

        val requestOptions = RequestOptions()
        requestOptions.placeholder(ContextCompat.getDrawable(context,R.color.cardBackground))

        Glide.with(context).setDefaultRequestOptions(requestOptions)
                .load(url)
                .apply(NewsGlideModule.roundedCornerImage(RequestOptions(), imageView.context, 4))
                .into(imageView)
    }

    @BindingAdapter("bind:urlToImage", "bind:articleUrl")
    @JvmStatic
    fun loadImage(imageView: ImageView, url: String?, articleUrl: String) {
        var url = url
        val context = imageView.context
        if (url == null) {
            val iconUrl = "https://besticon-demo.herokuapp.com/icon?url=%s&size=80..120..200"
            url = String.format(iconUrl, Uri.parse(articleUrl).authority)
        }
        val requestOptions = RequestOptions()
        requestOptions.placeholder(ContextCompat.getDrawable(context,R.color.cardBackground))

        Glide.with(context).setDefaultRequestOptions(requestOptions)
                .load(url)
                .apply(NewsGlideModule.roundedCornerImage(RequestOptions(), imageView.context, 4))
                .into(imageView)
    }


    @JvmStatic
    fun truncateExtra(content: String?): String {
        return content?.replace("(\\[\\+\\d+ chars])".toRegex(), "") ?: ""
    }

    @JvmStatic
    fun formatDateForDetails(date: Timestamp): String {
        val format = SimpleDateFormat("dd MMM yyyy | hh:mm aaa", Locale.getDefault())
        return format.format(Date(date.time))
    }

    @BindingAdapter("bind:sourceUrl")
    @JvmStatic
    fun loadSourceImage(imageView: ImageView, sourceUrl: String) {
        var sourceUrl = sourceUrl
        val context = imageView.context
        val iconUrl = "https://besticon-demo.herokuapp.com/icon?url=%s&size=80..120..200"
        sourceUrl = String.format(iconUrl, Uri.parse(sourceUrl).authority)

        val requestOptions = RequestOptions()
        requestOptions.placeholder(ContextCompat.getDrawable(context,R.color.cardBackground))

        Glide.with(context).setDefaultRequestOptions(requestOptions)
                .load(sourceUrl)
                .apply(NewsGlideModule.roundedCornerImage(RequestOptions(), imageView.context, 4))
                .into(imageView)
    }

    @JvmStatic
    fun getSourceName(category: String, country: String): String {
        val builder = StringBuilder()
        builder.append(capitalise(category))

        if (!TextUtils.isEmpty(category) && !TextUtils.isEmpty(country)) {
            builder.append(" • ")
        }

        val locale = Locale("en", country)
        builder.append(locale.displayCountry)
        return builder.toString()
    }

    @JvmStatic
    private fun capitalise(s: String): String {
        return if (TextUtils.isEmpty(s)) s else s.substring(0, 1).toUpperCase() + s.substring(1)

    }

}


