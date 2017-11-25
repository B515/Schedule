package xyz.b515.schedule.ui.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import xyz.b515.schedule.R
import xyz.b515.schedule.databinding.ItemSpacetimeBinding
import xyz.b515.schedule.db.CourseManager
import xyz.b515.schedule.entity.Spacetime
import java.text.DateFormatSymbols
import java.util.*

/**
 * Created by ZeroGo on 2017/5/25.
 */

class SpacetimeAdapter(var items: ArrayList<Spacetime>, private val context: Context, private val manager: CourseManager, private val flag: Boolean) : RecyclerView.Adapter<SpacetimeAdapter.SpacetimeViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): SpacetimeViewHolder {
        val binding = DataBindingUtil.inflate<ItemSpacetimeBinding>(LayoutInflater.from(viewGroup.context), R.layout.item_spacetime, viewGroup, false)
        return SpacetimeViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: SpacetimeViewHolder, position: Int) {
        holder.binding.spacetime = items[position]

        val weeks = DateFormatSymbols.getInstance().shortWeekdays.toMutableList()
        weeks.removeAt(0)
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, weeks)
        holder.binding.spinner.adapter = adapter
        if (!flag) {
            holder.binding.spinner.setSelection(items[position].weekday - 1)
        }

        holder.binding.spacetimeDelete.setOnClickListener {
            AlertDialog.Builder(context)
                    .setTitle(R.string.alert_title)
                    .setMessage(R.string.alert_message)
                    .setPositiveButton(R.string.alert_pos) { _, _ ->
                        manager.deleteSpacetime(items[holder.adapterPosition])
                        items.removeAt(holder.adapterPosition)
                        notifyDataSetChanged()
                    }
                    .setNegativeButton(R.string.alert_neg) { _, _ -> }
                    .show()
        }
        holder.binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                items[holder.adapterPosition].weekday = pos + 1
            }

            override fun onNothingSelected(parent: AdapterView<*>) = Unit
        }
    }

    override fun getItemCount() = items.size

    class SpacetimeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ItemSpacetimeBinding = DataBindingUtil.getBinding(view)
    }
}
