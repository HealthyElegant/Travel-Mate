package io.github.project_travel_mate.utilities

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import objects.ItineraryItem
import io.github.project_travel_mate.R

class ItineraryAdapter(private val items: MutableList<ItineraryItem>, private val dragStartListener: DragStartListener)
    : RecyclerView.Adapter<ItineraryAdapter.ViewHolder>() {

    interface DragStartListener {
        fun onStartDrag(holder: RecyclerView.ViewHolder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_itinerary, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.text.text = item.activity
        holder.handle.setOnTouchListener { _, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                dragStartListener.onStartDrag(holder)
            }
            false
        }
    }

    override fun getItemCount(): Int = items.size

    fun onItemMove(from: Int, to: Int) {
        val item = items.removeAt(from)
        items.add(to, item)
        notifyItemMoved(from, to)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text: TextView = view.findViewById(R.id.row_text)
        val handle: ImageView = view.findViewById(R.id.row_handle)
    }
}
