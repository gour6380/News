package com.gourav.news.ui

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.os.Bundle

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.gourav.news.BuildConfig
import com.gourav.news.R
import com.gourav.news.data.NewsRepository
import com.gourav.news.databinding.ActivityMainBinding
import com.gourav.news.ui.headlines.HeadlinesFragment
import com.gourav.news.ui.news.NewsFragment
import com.gourav.news.ui.news.OptionsBottomSheet
import com.gourav.news.ui.sources.SourceFragment
import com.gourav.news.widget.SavedNewsWidget
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import timber.log.Timber

class MainActivity : AppCompatActivity(), OptionsBottomSheet.OptionsBottomSheetListener {
    private val fragmentManager = supportFragmentManager
    private lateinit var binding: ActivityMainBinding
    private var headlinesFragment: HeadlinesFragment? = null
    private var sourceFragment: SourceFragment? = null
    private var newsFragment: NewsFragment? = null
    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val bundle = Bundle()
        when (item.itemId) {
            R.id.navigation_headlines -> {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, headlinesFragment!!)
                        .commit()
                bundle.putString(
                        FirebaseAnalytics.Param.ITEM_CATEGORY,
                        getString(R.string.title_headlines)
                )
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_saved -> {
                if (newsFragment == null) {
                    newsFragment = NewsFragment.newInstance(null)
                }
                supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, newsFragment!!)
                        .commit()
                bundle.putString(
                        FirebaseAnalytics.Param.ITEM_CATEGORY,
                        getString(R.string.title_saved)
                )
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_sources -> {
                if (sourceFragment == null) {
                    sourceFragment = SourceFragment.newInstance()
                }
                supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, sourceFragment!!)
                        .commit()
                bundle.putString(
                        FirebaseAnalytics.Param.ITEM_CATEGORY,
                        getString(R.string.title_sources)
                )
                return@OnNavigationItemSelectedListener true
            }
        }
        mFirebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        false
    }
    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // Bind data using DataBinding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        mFirebaseAnalytics!!.setAnalyticsCollectionEnabled(true)

        if (savedInstanceState == null) {
            // Add a default fragment
            headlinesFragment = HeadlinesFragment.newInstance()
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, headlinesFragment!!)
                    .commit()
        }

        setupToolbar()

        val appWidgetManager = AppWidgetManager.getInstance(this)

        val saved = NewsRepository.getInstance(this).saved
        saved.observe(this, Observer { articles ->
            if (articles != null) {
                val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(applicationContext, SavedNewsWidget::class.java))
                if (articles.size == 0) {
                    SavedNewsWidget.updateNewsWidgets(applicationContext, appWidgetManager, articles, -1, appWidgetIds)
                } else {
                    SavedNewsWidget.updateNewsWidgets(applicationContext, appWidgetManager, articles, 0, appWidgetIds)
                }
            }
        })
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = getString(R.string.app_name)
            //Remove trailing space from toolbar
            binding.toolbar.setContentInsetsAbsolute(10, 10)
        }
    }

    override fun onSaveToggle(text: String) {
        if (snackbar == null) {
            snackbar = Snackbar.make(binding.coordinator, "Hello", Snackbar.LENGTH_SHORT)
            val params = snackbar!!.view.layoutParams as CoordinatorLayout.LayoutParams
            params.setMargins(
                    resources.getDimension(R.dimen.snackbar_margin_vertical).toInt(),
                    0,
                    resources.getDimension(R.dimen.snackbar_margin_vertical).toInt(),
                    resources.getDimension(R.dimen.snackbar_margin_horizontal).toInt()
            )
            snackbar!!.view.layoutParams = params
            snackbar!!.view.setPadding(
                    resources.getDimension(R.dimen.snackbar_padding).toInt(),
                    resources.getDimension(R.dimen.snackbar_padding).toInt(),
                    resources.getDimension(R.dimen.snackbar_padding).toInt(),
                    resources.getDimension(R.dimen.snackbar_padding).toInt()
            )
        }
        if (snackbar!!.isShown) {
            snackbar!!.dismiss()
        }
        snackbar!!.setText(text)
        snackbar!!.show()
    }
}
