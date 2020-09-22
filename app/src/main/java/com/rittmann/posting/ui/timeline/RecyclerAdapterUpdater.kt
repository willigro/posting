package com.rittmann.posting.ui.timeline

import androidx.recyclerview.widget.RecyclerView

/**
 * INSERT - works with normal ordination (add new item to end of the list)
 * UPDATE - works with normal ordination (add new item to end of the list) with sameItem, but it
 *          isn't performatic (because, if i don't change the content i will still update the list,
 *          then i need to create the sameContent (sameContent was created)
 * DELETE - works with normal ordination (add new item to end of the list)
 * */
abstract class RecyclerAdapterUpdater<VH : RecyclerView.ViewHolder, T>(var list: List<T>) :
    RecyclerView.Adapter<VH>() {

    fun change(newList: List<T>, recyclerView: RecyclerView) {
        val rec = when {
            newList.size > list.size -> {
                Rec.INSERT
            }
            newList.size == list.size -> {
                Rec.UPDATE
            }
            else -> Rec.DELETE
        }

        val index = if (rec != Rec.INSERT) getChangedIndex(list, newList, rec) else -1

        (list as ArrayList<T>).apply {
            clear()
            addAll(newList)
        }

        when (rec) {
            Rec.INSERT -> {
                val position = newList.size - 1
                notifyItemInserted(position)
                recyclerView.scrollToPosition(position)
            }
            Rec.DELETE -> {
                if (index > -1)
                    notifyItemRemoved(index)
            }
            Rec.UPDATE -> {
                if (index > -1)
                    notifyItemChanged(index)
                else
                    notifyDataSetChanged()
            }
        }
    }

    private fun getChangedIndex(list: List<T>, newList: List<T>, rec: Rec): Int {
        var found: Boolean
        for (iOld in list.indices) {
            found = false
            for (iNew in newList.indices) {
                if (same(list[iOld], newList[iNew], rec)) {
                    found = true
                    break
                }
            }

            if (found.not()) {
                return iOld
            }
        }
        return -1
    }

    /*
    * For me, it ins't performatic, but i will leave it at that
    * */
    private fun same(a: T, b: T, rec: Rec): Boolean {
        return if (rec == Rec.UPDATE) sameContent(a, b) else sameItem(a, b)
    }

    abstract fun sameItem(a: T, b: T): Boolean

    abstract fun sameContent(a: T, b: T): Boolean

    private enum class Rec {
        INSERT, UPDATE, DELETE
    }
}