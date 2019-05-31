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
import com.codding.test.startoverflowuser.util.TimeConstant
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class SofListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        val VIEW_TYPE_ROW = 0x01
        val VIEW_TYPE_LOADING = 0x02
    }

    // Data variables
    var sofUser : MutableList<SoFUser?> = mutableListOf()
    var favoriteList : MutableSet<String> = mutableSetOf()

    // Format time variables
    var lastAccessTimeFormat = SimpleDateFormat(TimeConstant.LAST_ACCESS_TIME_FORMAT)
    var lastAccessCalendar = Calendar.getInstance()

    var isFavoriteMode = false
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
                holder.reputaions.text = it.reputation.toString()
                holder.userName.text = it.userName
                holder.updateAvatar(it.profileImg);

                // Update favorite state
                if (favoriteList.contains(currentUser.userId)) {
                    holder.favorite.setBackgroundResource(R.drawable.favorite_checked)
                } else {
                    holder.favorite.setBackgroundResource(R.drawable.favorite)
                }

                // Update last access date
                lastAccessCalendar.timeInMillis = it.accessDate.toLong() * 1000 // (return data is in Second)
                holder.accessDate.text = lastAccessTimeFormat.format(lastAccessCalendar.time)

            }
        } else if (holder is LoadingViewHolder) {
            if (isFavoriteMode) holder.loadingView.visibility = View.GONE
            else holder.loadingView.visibility = View.VISIBLE
        }

    }

    override fun getItemViewType(position: Int): Int {
        if (sofUser.get(position) == null) {
            return VIEW_TYPE_LOADING
        }
        return VIEW_TYPE_ROW
    }

    fun getItemAt(position: Int) : SoFUser? {
        return sofUser.get(position)
    }

    fun toogleAdapterMode(mode : Boolean) {
        sofUser.clear()
        notifyDataSetChanged()
        isFavoriteMode = mode
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


    /**
     * Append more data at the end of list
     */
    fun addMoreData(data : MutableList<SoFUser>) {
        // Remove loading view to append more data
        if (sofUser.size > 0) {
            sofUser.removeAt(sofUser.size - 1)
        }
        sofUser.addAll(data)
        sofUser.add(null)
        notifyDataSetChanged()
    }

    /**
     * Add listener for row/favorite click
     */
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
        var loadingView = itemView.findViewById<ProgressBar>(R.id.progressBar)
    }
}