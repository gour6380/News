package com.gourav.news.ui.news

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View

import com.gourav.news.R
import com.gourav.news.data.NewsRepository
import com.gourav.news.databinding.ActivityDetailBinding
import com.gourav.news.models.Article
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer

class DetailActivity : AppCompatActivity() {
    private var binding: ActivityDetailBinding? = null
    private var article: Article? = null
    private var isSaved: Boolean = false
    private var newsRepository: NewsRepository? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        makeUiFullscreen()
        setupToolbar()
        setupArticleAndListener()
        newsRepository = NewsRepository.getInstance(this)

        getSavedState()

        binding!!.ivSave.setOnClickListener {
            if (isSaved) {
                newsRepository!!.removeSaved(article!!.id)
            } else {
                newsRepository!!.save(article!!.id)
            }
        }
    }

    private fun getSavedState() {
        if (article != null) {
            newsRepository!!.isSaved(article!!.id).observe(this, Observer { aBoolean ->
                if (aBoolean != null) {
                    isSaved = aBoolean
                    if (isSaved) {
                        binding!!.ivSave.setImageResource(R.drawable.ic_saved_item)
                    } else {
                        binding!!.ivSave.setImageResource(R.drawable.ic_save)
                    }
                }
            })
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding!!.toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }
    }

    private fun makeUiFullscreen() {
        binding!!.root.fitsSystemWindows = true
        val uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        window.decorView.systemUiVisibility = uiOptions
    }

    private fun setupArticleAndListener() {
        val bundle = intent.extras
        if (bundle != null && bundle.containsKey(PARAM_ARTICLE)) {
            val article = bundle.getParcelable<Article>(PARAM_ARTICLE)
            if (article != null) {
                this.article = article
                binding!!.article = article
                setupShareButton(article)
                setupButtonClickListener(article)
            }
        }
    }

    private fun setupShareButton(article: Article) {
        binding!!.ivShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val shareText = article.title + "\n" + article.url
            intent.putExtra(Intent.EXTRA_TEXT, shareText)
            intent.type = "text/plain"

            startActivity(intent)
        }
    }

    private fun setupButtonClickListener(article: Article) {
        binding!!.btnReadFull.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.fade_enter_transition, R.anim.slide_down_animation)
    }

    companion object {
        const val PARAM_ARTICLE = "param-article"
    }
}