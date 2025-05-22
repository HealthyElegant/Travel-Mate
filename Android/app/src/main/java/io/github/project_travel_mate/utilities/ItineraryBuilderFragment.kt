package io.github.project_travel_mate.utilities

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import io.github.project_travel_mate.R
import objects.Itinerary
import utils.ItineraryGenerator
import database.AppDataBase
import dao.ItineraryDao
import utils.Constants

class ItineraryBuilderFragment : Fragment(), ItineraryAdapter.DragStartListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItineraryAdapter
    private lateinit var database: AppDataBase
    private lateinit var itineraryDao: ItineraryDao
    private lateinit var touchHelper: ItemTouchHelper
    private var activityContext: Activity? = null

    companion object {
        private const val ARG_DAYS = "days"

        fun newInstance(days: Int): ItineraryBuilderFragment {
            val fragment = ItineraryBuilderFragment()
            val args = Bundle()
            args.putInt(ARG_DAYS, days)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityContext = context as Activity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_itinerary_builder, container, false)
        recyclerView = view.findViewById(R.id.itinerary_recycler)
        database = AppDataBase.getAppDatabase(activityContext)
        itineraryDao = database.itineraryDao()

        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(activityContext)
        val userId = prefs.getString(Constants.USER_ID, "local") ?: "local"

        val days = arguments?.getInt(ARG_DAYS, 3) ?: 3

        val itinerary = Itinerary(userId = userId, name = "Trip", days = days)
        val id = itineraryDao.insertItinerary(itinerary).toInt()
        val items = ItineraryGenerator.generate(days, id)
        itineraryDao.insertItems(items)

        adapter = ItineraryAdapter(itineraryDao.getItems(id).toMutableList(), this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        val callback = ItineraryDragCallback(adapter)
        touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(recyclerView)

        return view
    }

    override fun onStartDrag(holder: RecyclerView.ViewHolder) {
        touchHelper.startDrag(holder)
    }
}
