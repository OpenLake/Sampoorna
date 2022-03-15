package org.openlake.sampoorna.presentation.features.self_care

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import org.openlake.sampoorna.R

class SelfCareAdapter(var context: Context, var arrayList: ArrayList<SelfCareModel>) : BaseAdapter() {

    override fun getItem(position: Int): Any {
        return arrayList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view:View = View.inflate(context, R.layout.card_view_item_grid, null)
        var icons:ImageView = view.findViewById(R.id.grid_item_self_care_icon)
        var name:TextView = view.findViewById(R.id.grid_item_self_care_title)

        var listItem:SelfCareModel = arrayList.get(position)

        icons.setImageResource(listItem.icons!!)
        name.text = listItem.name

        return view
    }
}