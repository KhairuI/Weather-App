package com.example.weatherapp.base

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


abstract class BaseRecycleAdapter<E : RecyclerView.ViewHolder, T>(context: Context, resource: Int) :
    RecyclerView.Adapter<E>() {
    // Fields ======================================================================================
    private var mSource = mutableListOf<T>()
    var mContext: Context = context
    private var mInflater = mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var mLayoutRes = resource
    abstract fun onCreateView(viewHolder: View): E
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): E {
        val viewHolder: View = mInflater.inflate(mLayoutRes, parent, false)
        return onCreateView(viewHolder)
    }

    override fun getItemCount(): Int {
        return mSource.size
    }

    operator fun get(position: Int): T {
        return mSource[position]
    }

    fun clear(notify: Boolean) {
        mSource.clear()
        if (notify) notifyDataSetChanged()
    }

    fun addAll(elements: List<T>, notify: Boolean) {
        mSource.addAll(elements)
        if (notify) notifyDataSetChanged()
    }

    fun add(ele: T, notify: Boolean) {
        mSource.add(ele)
        if (notify) notifyDataSetChanged()
    }

    fun add(idx: Int, ele: T, notify: Boolean) {
        mSource.add(idx, ele)
        if (notify) notifyDataSetChanged()
    }

    operator fun set(idx: Int, ele: T, notify: Boolean) {
        mSource[idx] = ele
        if (notify) notifyDataSetChanged()
    }

    fun remove(ele: T, notify: Boolean) {
        mSource.remove(ele)
        if (notify) notifyDataSetChanged()
    }

    fun remove(idx: Int, notify: Boolean) {
        mSource.removeAt(idx)
        if (notify) notifyDataSetChanged()
    }

    fun merge(c: Collection<T>, notify: Boolean) {
        mSource.addAll(c)
        if (notify) notifyDataSetChanged()
    }

    fun replace(c: Collection<T>, notify: Boolean) {
        mSource.clear()
        mSource.addAll(c)
        if (notify) notifyDataSetChanged()
    }

}