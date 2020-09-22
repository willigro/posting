package com.rittmann.posting.ui.timeline

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.rittmann.posting.R
import com.rittmann.posting.data.basic.Posting
import com.rittmann.posting.interfaces.IItemSelected
import com.rittmann.posting.util.DateUtil

class RecyclerAdapterPosts(
    context: Context,
    private var lists: List<Posting>,
    private val iItemSelected: IItemSelected,
    private val exec: (post: Posting) -> Unit
) : RecyclerAdapterUpdater<RecyclerAdapterPosts.ViewHolderPosts, Posting>(lists) {

    private val inflate = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPosts =
        ViewHolderPosts(
            inflate.inflate(
                R.layout.adapter_posts, parent, false
            )
        )

    override fun getItemCount(): Int = lists.size

    override fun onBindViewHolder(holder: ViewHolderPosts, position: Int) {
        val post = lists[holder.adapterPosition]
        holder.apply {
            title.text = post.title
            description.text = post.description
            date.text = DateUtil.parseDate(post.dateTime)

            layout.setOnClickListener {
                iItemSelected.select(post)
                exec(post)
            }
        }
    }

    /* Used by TimelineFragment */
    fun update(listUpdated: List<Posting>) {
        (lists as ArrayList).apply {
            clear()
            addAll(listUpdated)
        }
        notifyDataSetChanged()
    }

    override fun sameItem(a: Posting, b: Posting): Boolean {
        return a.id == b.id
    }

    override fun sameContent(a: Posting, b: Posting): Boolean {
        return a.sameContent(b)
    }

    class ViewHolderPosts(itemView: View) : ViewHolder(itemView) {
        val layout: View = itemView.findViewById(R.id.postContent)
        val title: TextView = itemView.findViewById(R.id.txtTitle)
        val description: TextView = itemView.findViewById(R.id.txtDescription)
        val date: TextView = itemView.findViewById(R.id.txtDate)
    }
}