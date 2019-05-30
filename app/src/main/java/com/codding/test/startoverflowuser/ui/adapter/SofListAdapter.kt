package com.codding.test.startoverflowuser.ui.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codding.test.startoverflowuser.R
import com.codding.test.startoverflowuser.inflate
import com.codding.test.startoverflowuser.listener.SofUserRowListener
import com.codding.test.startoverflowuser.modal.SoFUser
import com.squareup.picasso.Picasso

class SofListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        val VIEW_TYPE_ROW = 0x01
        val VIEW_TYPE_LOADING = 0x02
    }

    var sofUser : MutableList<SoFUser?> = mutableListOf()
    var sofUserRowListener : SofUserRowListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == VIEW_TYPE_ROW) {
            var inflateView = parent.inflate(R.layout.sof_user_row)
            var viewHolder = SofListViewHodler(inflateView)

            // Binding click action to row
            viewHolder.itemView.setOnClickListener {
                sofUserRowListener?.let {
                    it.onUserClicked(viewHolder.adapterPosition)
                }
            }
            // Binding favorite toggle action to row
            viewHolder.favorite.setOnClickListener {
                sofUserRowListener?.let {
                    it.onUserFavoritedTogle(viewHolder.adapterPosition)
                }
            }
            return viewHolder;
        } else {
            return LoadingViewHolder(parent.inflate(R.layout.loading_row))
        }

    }

    override fun getItemCount(): Int {
        return sofUser.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SofListViewHodler) {
            var currentUser = sofUser.get(position)
            currentUser?.let {
                holder.location.text = it.location
                holder.accessDate.text = it.accessDate
                holder.reputaions.text = it.reputation.toString()
                holder.userName.text = it.userName
                holder.updateAvatar(it.profileImg);
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        if (sofUser.get(position) == null) {
            return VIEW_TYPE_LOADING
        }
        return VIEW_TYPE_ROW
    }


    fun addMoreData(data : MutableList<SoFUser>) {
        // Remove loading view to append more data
        if (sofUser.size > 0) {
            sofUser.removeAt(sofUser.size - 1)
        }
        sofUser.addAll(data)
        sofUser.add(null)
        notifyDataSetChanged()
    }

    fun setUserRowListener(lsn : SofUserRowListener) {
        sofUserRowListener = lsn
    }

    class SofListViewHodler(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var favorite = itemView.findViewById<ImageView>(R.id.imgFavorite)
        var avartar = itemView.findViewById<ImageView>(R.id.imgUserAvatar)
        var userName = itemView.findViewById<TextView>(R.id.txtUserName)
        var location = itemView.findViewById<TextView>(R.id.txtLocation)
        var reputaions = itemView.findViewById<TextView>(R.id.txtReputation)
        var accessDate = itemView.findViewById<TextView>(R.id.txtLastAccess)


        fun updateAvatar(url: String) {
            Picasso.get().load(url).into(avartar)
        }
    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var progressBar = itemView.findViewById<ProgressBar>(R.id.processBar)
    }
}