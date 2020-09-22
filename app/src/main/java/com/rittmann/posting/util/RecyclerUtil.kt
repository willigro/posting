package com.rittmann.posting.util

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


object RecyclerUtil {

    fun createLinearFixed(
        recyclerView: RecyclerView,
        adapter: RecyclerView.Adapter<*>
    ): RecyclerView {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter = adapter
        return recyclerView
    }

    fun createGridFixed(
        recyclerView: RecyclerView,
        adapter: RecyclerView.Adapter<*>,
        grid: Int
    ): RecyclerView {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(recyclerView.context, grid)
        recyclerView.adapter = adapter
        return recyclerView
    }
}