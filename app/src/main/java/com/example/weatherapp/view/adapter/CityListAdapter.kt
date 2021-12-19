package com.example.weatherapp.view.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.base.BaseRecycleAdapter
import com.example.weatherapp.models.SingleCity

class CityListAdapter(context: Context, layoutResource: Int, private val listener:OnCardClickListener) :
    BaseRecycleAdapter<CityListAdapter.ViewHolder, SingleCity>(context, layoutResource) {

    override fun onCreateView(viewHolder: View): ViewHolder {
        return ViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val city = get(position)

        holder.apply {

            name.text= city.name
            desc.text= city.weather!![0].description
            val temperature= city.main?.temp?.minus(273)?.toInt()
            temp.text= temperature.toString()

            // on click
            rowItem.setOnClickListener {
                listener.onCardClick(city)
            }
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.txt_name)
        val desc: TextView = itemView.findViewById(R.id.txt_desc)
        val temp: TextView = itemView.findViewById(R.id.txt_temp)
        val rowItem: ConstraintLayout = itemView.findViewById(R.id.row_layout)
    }

    interface OnCardClickListener {
        fun onCardClick(singleCity: SingleCity)
    }

}