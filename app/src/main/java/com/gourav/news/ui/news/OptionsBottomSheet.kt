package com.gourav.news.ui.news

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.gourav.news.R
import com.gourav.news.data.NewsRepository
import androidx.databinding.DataBindingUtil
import com.gourav.news.databinding.FragmentOptionsBottomSheetBinding
import timber.log.Timber

class OptionsBottomSheet : BottomSheetDialogFragment(), View.OnClickListener {
    private var title: String? = null
    private var url: String? = null
    private var ids: Int = 0
    private var isSaved: Boolean = false
    private var listener: OptionsBottomSheetListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            title = arguments!!.getString(PARAM_TITLE)
            url = arguments!!.getString(PARAM_URL)
            ids = arguments!!.getInt(PARAM_ID)
            isSaved = arguments!!.getBoolean(PARAM_SAVED)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentOptionsBottomSheetBinding>(inflater, R.layout.fragment_options_bottom_sheet, container, false)

        if (isSaved) {
            binding.btnSave.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_saved_item, 0, 0, 0)
        }
        binding.btnShare.setOnClickListener(this)
        binding.btnOpenInBrowser.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)
        return binding.root
    }

    override fun onClick(v: View) {
        val intent: Intent
        when (v.id) {
            R.id.btn_open_in_browser -> {
                intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                fragment!!.dismiss()
                startActivity(intent)
            }
            R.id.btn_share -> {
                val shareText = title + "\n" + url
                intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(Intent.EXTRA_TEXT, shareText)
                intent.type = "text/plain"
                this.dismiss()
                startActivity(intent)
            }
            R.id.btn_save -> {
                if (isSaved) {
                    NewsRepository.getInstance(context!!).removeSaved(ids)
                    listener!!.onSaveToggle(getString(R.string.message_item_removed))
                } else {
                    NewsRepository.getInstance(context!!).save(ids)
                    listener!!.onSaveToggle(getString(R.string.message_item_saved))
                }
                Timber.d("Saved for ids  : %s", ids)
                dismiss()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as OptionsBottomSheetListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement OptionsBottomSheetListener")
        }

    }

    interface OptionsBottomSheetListener {
        fun onSaveToggle(text: String)
    }

    companion object {

        private const val PARAM_TITLE = "param-title"
        private const val PARAM_URL = "param-url"
        private const val PARAM_ID = "param-ids"
        private const val PARAM_SAVED = "param-saved"
        private var fragment: OptionsBottomSheet? = null

        fun getInstance(title: String, url: String, id: Int, isSaved: Boolean): OptionsBottomSheet {
            fragment = OptionsBottomSheet()
            val args = Bundle()
            args.putString(PARAM_TITLE, title)
            args.putString(PARAM_URL, url)
            args.putInt(PARAM_ID, id)
            args.putBoolean(PARAM_SAVED, isSaved)
            fragment!!.arguments = args
            return fragment!!
        }
    }
}// Required empty public constructor
