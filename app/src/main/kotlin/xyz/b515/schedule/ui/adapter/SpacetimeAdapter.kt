package xyz.b515.schedule.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.item_spacetime.view.*
import org.jetbrains.anko.alert
import xyz.b515.schedule.R
import xyz.b515.schedule.db.CourseManager
import xyz.b515.schedule.entity.Spacetime
import java.text.DateFormatSymbols
import java.util.*

/**
 * Created by ZeroGo on 2017/5/25.
 */

class SpacetimeAdapter(var items: ArrayList<Spacetime>, private val context: Context, private val manager: CourseManager, private val flag: Boolean) : RecyclerView.Adapter<SpacetimeAdapter.SpacetimeViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int) =
            SpacetimeViewHolder(LayoutInflater.from(context).inflate(R.layout.item_spacetime, viewGroup, false))

    override fun onBindViewHolder(holder: SpacetimeViewHolder, position: Int) {
        val st = items[position]
        with(holder.v) {
            spacetime_start_week.setText(st.startWeek.toString())
            spacetime_end_week.setText(st.endWeek.toString())
            spacetime_start_time.setText(st.startTime.toString())
            spacetime_end_time.setText(st.endTime.toString())
            spacetime_place.setText(st.location)
        }

        val w = object : TextWatcher {
            fun Editable.toInt(): Int = this.toString().toIntOrNull() ?: 0
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                with(st) {
                    startWeek = holder.v.spacetime_start_week.text.toInt()
                    endWeek = holder.v.spacetime_end_week.text.toInt()
                    startTime = holder.v.spacetime_start_time.text.toInt()
                    endTime = holder.v.spacetime_end_time.text.toInt()
                    location = holder.v.spacetime_place.text.toString()
                }
            }
        }
        listOf(holder.v.spacetime_start_week, holder.v.spacetime_end_week, holder.v.spacetime_start_time,
                holder.v.spacetime_end_time, holder.v.spacetime_place).forEach { it.addTextChangedListener(w) }

        val weeks = DateFormatSymbols.getInstance().shortWeekdays.toMutableList()
        weeks.removeAt(0)
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, weeks)
        holder.v.spinner.adapter = adapter
        if (!flag) {
            holder.v.spinner.setSelection(items[position].weekday - 1)
        }

        holder.v.spacetime_delete.setOnClickListener {
            context.alert(R.string.alert_title) {
                messageResource = R.string.alert_message
                positiveButton(R.string.alert_pos) { _ ->
                    manager.deleteSpacetime(items[holder.adapterPosition])
                    items.removeAt(holder.adapterPosition)
                    notifyDataSetChanged()
                }
                negativeButton(R.string.alert_neg) {}
            }.show()
        }
        holder.v.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                items[holder.adapterPosition].weekday = pos + 1
            }

            override fun onNothingSelected(parent: AdapterView<*>) = Unit
        }
    }

    override fun getItemCount() = items.size

    class SpacetimeViewHolder(val v: View) : RecyclerView.ViewHolder(v)
}
