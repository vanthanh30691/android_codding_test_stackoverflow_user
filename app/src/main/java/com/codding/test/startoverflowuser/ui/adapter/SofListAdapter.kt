package com.codding.test.startoverflowuser.ui.adapter

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codding.test.startoverflowuser.R
import com.codding.test.startoverflowuser.inflate
import com.codding.test.startoverflowuser.listener.SofUserRowListener
import com.codding.test.startoverflowuser.network.respond.SoFUser
import com.squareup.picasso.Picasso

class SofListAdapter : RecyclerView.Adapter<SofListAdapter.SofListViewHodler>() {

    var sofUser : MutableList<SoFUser> = mutableListOf()
    var sofUserRowListener : SofUserRowListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SofListViewHodler {
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
    }

    override fun getItemCount(): Int {
        return sofUser.size
    }

    override fun onBindViewHolder(holder: SofListViewHodler, position: Int) {
        var currentUser = sofUser.get(position)
        holder.location.text = currentUser.location
        holder.accessDate.text = currentUser.accessDate
        holder.reputaions.text = currentUser.reputation.toString()
        holder.userName.text = currentUser.userName

        holder.updateAvatar(currentUser.profileImg);
    }

    /**
     * Empty sof list and replace all data
     */
    fun setAllSofData(data : MutableList<SoFUser>) {
        sofUser.addAll(data)
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
}