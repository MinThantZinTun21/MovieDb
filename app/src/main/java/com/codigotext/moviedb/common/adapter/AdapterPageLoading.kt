package com.codigotext.moviedb.common.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codigotext.moviedb.R
class AdapterPageLoading(private val retry: () -> Unit) :
    LoadStateAdapter<AdapterPageLoading.LoadingStateViewHolder>() {
    class LoadingStateViewHolder(itemView: View, retry: () -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val tvErrorMessage: TextView = itemView.findViewById(R.id.tvErrorMessage)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)
        // private val tvEmptyView: TextView? = itemView.tvEmptyView
        private val btnRetry: Button = itemView.findViewById(R.id.btnRetry)

        init {
            btnRetry.setOnClickListener {
                retry.invoke()
            }
        }

        fun bindState(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                tvErrorMessage.text = loadState.error.localizedMessage
            }
            progressBar.isVisible = loadState is LoadState.Loading
            tvErrorMessage.isVisible = loadState !is LoadState.Loading
            //  tvEmptyView!!.isVisible=loadState is LoadState.NotLoading
            btnRetry.isVisible = loadState !is LoadState.Loading


        }
    }

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.bindState(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState,
    ): LoadingStateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_loading_footer, parent, false)
        return LoadingStateViewHolder(view, retry)
    }

}
