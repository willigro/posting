package com.rittmann.posting.ui.timeline

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.rittmann.posting.R
import com.rittmann.posting.data.basic.Posting
import com.rittmann.posting.data.dao.config.AppDatabase
import com.rittmann.posting.data.repository.PostingRepositoryImpl
import com.rittmann.posting.interfaces.IItemSelected
import com.rittmann.posting.ui.base.BaseFragment
import com.rittmann.posting.util.ConstantsUtil.TAG_APP
import com.rittmann.posting.util.RecyclerUtil
import kotlinx.android.synthetic.main.timeline_fragment.btnNewPost
import kotlinx.android.synthetic.main.timeline_fragment.recycler

class TimelineFragment : BaseFragment(), IItemSelected {

    override var layoutId: Int = R.layout.timeline_fragment

    private var adapter: RecyclerAdapterPosts? = null
    private var _viewModel: Lazy<TimelineViewModel> = viewModels(factoryProducer = {
        TimelineViewModel.FACTORY(PostingRepositoryImpl(AppDatabase.getDatabase(requireContext())!!))
    })

    private val viewModel
        get() = _viewModel.value

    private var userId: Long = 0L

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViews()
        initObservers()

        userId = TimelineFragmentArgs.fromBundle(requireArguments()).userId

        viewModel.getPosts(userId)
    }

    override fun onResume() {
        super.onResume()
        setToolbar(getString(R.string.timeline_title), true)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        viewModel.posts.removeObservers(viewLifecycleOwner)
    }

    override fun initViews() {
        btnNewPost.setOnClickListener {
            TimelineFragmentDirections.actionTimelineFragmentToPostingFragment().apply {
                findNavController().navigate(this)
            }
        }
    }

    override fun initObservers() {
        viewModel.posts.observe(viewLifecycleOwner, Observer { posts ->
            posts?.also {
                if (adapter == null) {
                    adapter = RecyclerAdapterPosts(requireContext(), posts, this) {
                        Log.i(TAG_APP, it.title)
                    }
                } else {
                    adapter!!.update(posts)
                }

                RecyclerUtil.createLinearFixed(
                    recycler, adapter!!
                )
            }
        })
    }

    override fun <T> select(any: T) {
        TimelineFragmentDirections.actionTimelineFragmentToPostingFragment(any as Posting).apply {
            findNavController().navigate(this)
        }
    }
}
