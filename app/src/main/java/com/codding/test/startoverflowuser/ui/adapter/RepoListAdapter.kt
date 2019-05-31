package com.codding.test.startoverflowuser.ui.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codding.test.startoverflowuser.R
import com.codding.test.startoverflowuser.inflate
import com.codding.test.startoverflowuser.modal.Reputation

class RepoListAdapter : BaseLoadingListAdapter <Reputation>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ROW) {
            var inflateView = parent.inflate(R.layout.reputation_detail_row)
            var viewHolder = RepuViewHolder(inflateView)

            // Binding click action to row
            viewHolder.itemView.setOnClickListener {
                getUserClickListener()?.let {
                    it.onUserClicked(viewHolder.adapterPosition)
                }
            }
            return viewHolder
        } else {
            return LoadingViewHolder(parent.inflate(R.layout.loading_row))
        }

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RepuViewHolder) {
            var currentUser = getData().get(position)
            currentUser?.let {
                holder.repuChange.text = it.repuChange
                holder.postId.text = it.postId
                holder.createAt.text = getDisplayDate(it.createAt.toLong())
            }
        }

    }


    class RepuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var repuType = itemView.findViewById<ImageView>(R.id.imgRepoType)
        var repuChange = itemView.findViewById<TextView>(R.id.txtRepoChangeValue)
        var createAt = itemView.findViewById<TextView>(R.id.txtRepoCreateValue)
        var postId = itemView.findViewById<TextView>(R.id.txtRepoPostIdValue)

    }

}