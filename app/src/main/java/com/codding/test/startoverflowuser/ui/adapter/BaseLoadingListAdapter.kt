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

abstract class  BaseLoadingListAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_ROW = 0x01
        const val VIEW_TYPE_LOADING = 0x02
    }

    var lastAccessTimeFormat = SimpleDateFormat(TimeConstant.LAST_ACCESS_TIME_FORMAT)
    var lastAccessCalendar : Calendar = Calendar.getInstance()

    // Data variables
    private var dataList : MutableList<T?> = mutableListOf()
    private var sofUserRowListener : SofUserRowListener? = null

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun getData() : MutableList<T?> {
        return dataList
    }


    override fun getItemViewType(position: Int): Int {
        if (dataList.get(position) == null) {
            return VIEW_TYPE_LOADING
        }
        return VIEW_TYPE_ROW
    }

    fun getItemAt(position: Int) : T? {
        return dataList.get(position)
    }

    /**
     * Append more data at the end of list
     */
    fun addMoreData(data : MutableList<T>) {
        // Remove loading view to append more data
        if (dataList.size > 0) {
            dataList.removeAt(dataList.size - 1)
        }
        dataList.addAll(data)
        if (data.size > 0) dataList.add(null)
        notifyDataSetChanged()
    }


    /**
     * Add listener for row/favorite click
     */
    fun setUserRowListener(lsn : SofUserRowListener) {
        sofUserRowListener = lsn
    }

    /**
     * Add listener for row/favorite click
     */
    fun getUserClickListener() : SofUserRowListener? {
        return sofUserRowListener
    }

    /**
     * Return display date String by input time (in Second)
     */
    fun getDisplayDate(time : Long) : String {
        lastAccessCalendar.timeInMillis = time * 1000 // (return data is in Second)
        return lastAccessTimeFormat.format(lastAccessCalendar.time)
    }


    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var loadingView = itemView.findViewById<ProgressBar>(R.id.progressBar)
    }

}