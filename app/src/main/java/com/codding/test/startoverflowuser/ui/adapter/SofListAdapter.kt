package com.codding.test.startoverflowuser.ui.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.codding.test.startoverflowuser.R
import com.codding.test.startoverflowuser.inflate
import com.codding.test.startoverflowuser.modal.SoFUser
import com.squareup.picasso.Picasso

class SofListAdapter(private val picasso: Picasso) : BaseLoadingListAdapter <SoFUser>() {

    var favoriteList : MutableSet<String> = mutableSetOf()
    var isFavoriteMode = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == VIEW_TYPE_ROW) {
            var inflateView = parent.inflate(R.layout.sof_user_row)
            var viewHolder = SofListViewHodler(inflateView)

            // Binding click action to row
            viewHolder.itemView.setOnClickListener {
                getUserClickListener()?.let {
                    it.onUserClicked(viewHolder.adapterPosition)
                }
            }
            // Binding favorite toggle action to row
            viewHolder.favorite.setOnClickListener {
                getUserClickListener()?.let {
                    it.onUserFavoritedTogle(viewHolder.adapterPosition)
                }
            }
            return viewHolder;
        } else {
            return LoadingViewHolder(parent.inflate(R.layout.loading_row))
        }

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SofListViewHodler) {
            var currentUser = getData().get(position)
            currentUser?.let {
                holder.location.text = it.location
                holder.reputaions.text = it.reputation.toString()
                holder.userName.text = it.userName
                holder.updateAvatar(picasso, it.profileImg)

                // Update favorite state
                if (favoriteList.contains(currentUser.userId)) {
                    holder.favorite.setBackgroundResource(R.drawable.favorite_checked)
                } else {
                    holder.favorite.setBackgroundResource(R.drawable.favorite)
                }

                // Update last access date
                holder.accessDate.text = getDisplayDate( it.accessDate.toLong())

            }
        } else if (holder is LoadingViewHolder) {
            if (isFavoriteMode) holder.loadingView.visibility = View.GONE
            else holder.loadingView.visibility = View.VISIBLE
        }

    }


    fun toogleAdapterMode(mode : Boolean) {
        var dataSize = getData().size
        getData().clear()
        isFavoriteMode = mode
        notifyItemRangeRemoved(0, dataSize)
    }

    /**
     * Use this list to detect whether current user is favorite
     */
    fun buildFavoriteList(favoriteUser : List<String>) {
        // Convert to Set to increase checking performance
        favoriteList.clear()
        favoriteList.addAll(favoriteUser)

        notifyDataSetChanged()
    }

    class SofListViewHodler(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var favorite = itemView.findViewById<ImageView>(R.id.imgFavorite)
        var avartar = itemView.findViewById<ImageView>(R.id.imgUserAvatar)
        var userName = itemView.findViewById<TextView>(R.id.txtUserName)
        var location = itemView.findViewById<TextView>(R.id.txtLocation)
        var reputaions = itemView.findViewById<TextView>(R.id.txtReputation)
        var accessDate = itemView.findViewById<TextView>(R.id.txtLastAccess)

        fun updateAvatar(picasso: Picasso, url: String) {
            picasso.load(url).into(avartar)
        }
    }

}