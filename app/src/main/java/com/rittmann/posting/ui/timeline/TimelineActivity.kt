package com.rittmann.posting.ui.timeline

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.rittmann.posting.R
import com.rittmann.posting.data.basic.Posting
import com.rittmann.posting.data.dao.config.AppDatabase
import com.rittmann.posting.data.repository.PostingRepositoryImpl
import com.rittmann.posting.interfaces.IItemSelected
import com.rittmann.posting.ui.base.BaseActivity
import com.rittmann.posting.ui.keep.KeepPostActivity
import com.rittmann.posting.util.ConstantsUtil
import com.rittmann.posting.util.RecyclerUtil
import kotlinx.android.synthetic.main.timeline_fragment.btnNewPost
import kotlinx.android.synthetic.main.timeline_fragment.recycler

class TimelineActivity : BaseActivity(), IItemSelected {

    private var adapter: RecyclerAdapterPosts? = null
    private var _viewModel: Lazy<TimelineViewModel> = viewModels(factoryProducer = {
        TimelineViewModel.FACTORY(PostingRepositoryImpl(AppDatabase.getDatabase(this)!!))
    })

    private val viewModel
        get() = _viewModel.value

    private var userId: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.timeline_fragment)
        setToolbar(getString(R.string.timeline_title), true)
        userId = TimelineActivityArgs.fromBundle(intent!!.extras!!).userId
        initViews()
        initObservers()
    }

    private fun initViews() {
        btnNewPost.setOnClickListener {
            startActivity(KeepPostActivity.getIntent(this, null))
        }
    }

    private fun initObservers() {
        viewModel.postsLiveData(userId).observe(this, Observer { posts ->
            posts?.also {
                if (adapter == null) {
                    adapter = RecyclerAdapterPosts(this@TimelineActivity, posts, this) {
                        Log.i(ConstantsUtil.TAG_APP, it.title)
                    }

                    RecyclerUtil.createLinearFixed(
                        recycler, adapter!!
                    )
                } else {
                    adapter!!.change(posts, recycler)
                }
            }
        })
    }

    override fun <T> select(any: T) {
        startActivity(KeepPostActivity.getIntent(this, any as Posting))
    }
}
