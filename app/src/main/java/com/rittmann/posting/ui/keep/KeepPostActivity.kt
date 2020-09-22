package com.rittmann.posting.ui.keep

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuInflater
import android.view.View
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.rittmann.posting.R
import com.rittmann.posting.data.basic.Posting
import com.rittmann.posting.data.dao.config.AppDatabase
import com.rittmann.posting.data.preferences.ManagerDataStore
import com.rittmann.posting.data.repository.AccountRepositoryImpl
import com.rittmann.posting.data.repository.PostingRepositoryImpl
import com.rittmann.posting.internal.isValid
import com.rittmann.posting.internal.toast
import com.rittmann.posting.ui.base.BaseActivity
import com.rittmann.posting.util.ConstantsUtil.POST_ARGS
import com.rittmann.widgets.dialog.DialogUtil
import kotlinx.android.synthetic.main.fragment_keep_post.btnNewPost
import kotlinx.android.synthetic.main.fragment_keep_post.container_register
import kotlinx.android.synthetic.main.fragment_keep_post.container_update
import kotlinx.android.synthetic.main.fragment_keep_post.edtPostDescription
import kotlinx.android.synthetic.main.fragment_keep_post.edtPostTitle
import kotlinx.android.synthetic.main.fragment_keep_post.labelPostDescriptionReadMode
import kotlinx.android.synthetic.main.fragment_keep_post.labelPostTitleReadMode

class KeepPostActivity : BaseActivity() {

    private val appDatabase = AppDatabase.getDatabase(this)!!
    private val _viewModel = viewModels<KeepPostViewModel>(factoryProducer = {
        KeepPostViewModel.FACTORY(
            AccountRepositoryImpl(appDatabase, ManagerDataStore(this)),
            PostingRepositoryImpl(appDatabase)
        )
    })

    private val viewModel
        get() = _viewModel.value

    private var post: Posting? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_keep_post)
        intent?.extras?.getParcelable<Posting>(POST_ARGS)?.also {
            post = it
        }

        initViews()
        initObservers()
    }

    override fun onResume() {
        super.onResume()
        if (isUpdate()) {
            setToolbar(getString(R.string.update_post_title))
            menuClick {
                PopupMenu(this, it).apply {
                    val inflater: MenuInflater = menuInflater
                    inflater.inflate(R.menu.menu_keep_posting, menu)

                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.update -> {
                                updatePost()
                            }
                            R.id.delete -> {
                                viewModel.deletePost(post!!)
                            }
                        }
                        false
                    }

                    show()
                }
            }
            showPostData()
            container_update.visibility = View.VISIBLE
        } else {
            setToolbar(getString(R.string.new_post_title))
            container_register.visibility = View.VISIBLE
        }
    }

    private fun showPostData() {
        labelPostTitleReadMode.text = post!!.title
        labelPostDescriptionReadMode.text = post!!.description
    }

    private fun updatePost() {
        container_register.visibility = View.VISIBLE
        container_update.visibility = View.GONE

        edtPostTitle.setText(post!!.title)
        edtPostDescription.setText(post!!.description)

        btnNewPost.apply {
            setText(R.string.update_posting)
            setOnClickListener {
                viewModel.updatePost(
                    post!!.id,
                    edtPostTitle.text.toString(),
                    edtPostDescription.text.toString()
                )
            }
        }
    }

    private fun isUpdate(): Boolean = if (post == null) false else post!!.id > 0

    private fun initViews() {
        btnNewPost.setOnClickListener {
            viewModel.newPost(
                edtPostTitle.text.toString(),
                edtPostDescription.text.toString()
            )
        }
    }

    private fun initObservers() {
        viewModel.postRegistered.observe(this, Observer { isCreated ->
            if (isCreated!!) {
                toast(getString(R.string.post_registered))
                returnToTimeLine()
            } else {
                showDialogError(getString(R.string.register_error))
            }
        })

        viewModel.postUpdated.observe(this, Observer { isUpdated ->
            if (isUpdated!!) {
                toast(getString(R.string.post_updated))
            } else {
                showDialogError(getString(R.string.update_error))
            }
        })

        viewModel.postDeleted.observe(this, Observer { isDeleted ->
            if (isDeleted!!) {
                toast(getString(R.string.post_deleted))
                returnToTimeLine()
            } else {
                showDialogError(getString(R.string.delete_error))
            }
        })

        viewModel.titleValidation.observe(this, Observer { isValid ->
            edtPostTitle.isValid(isValid, R.string.title_error)
        })

        viewModel.descriptionValidation.observe(this, Observer { isValid ->
            edtPostDescription.isValid(isValid, R.string.description_error)
        })
    }

    private fun returnToTimeLine() {
        finish()
    }

    private fun showDialogError(msg: String) {
        DialogUtil().init(
            this,
            message = msg,
            ok = true,
            show = true
        )
    }

    companion object {
        fun getIntent(context: Context, post: Posting?) =
            Intent(context, KeepPostActivity::class.java).apply {
                putExtra(POST_ARGS, post)
            }
    }
}
