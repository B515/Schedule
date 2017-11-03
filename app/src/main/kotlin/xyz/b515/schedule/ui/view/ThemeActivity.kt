package xyz.b515.schedule.ui.view

import android.content.res.Resources
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_theme.*
import kotlinx.android.synthetic.main.item_theme.view.*
import xyz.b515.schedule.R
import xyz.b515.schedule.util.ThemeHelper
import xyz.b515.schedule.util.ThemeRes

class ThemeActivity : AppCompatActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme)

        setSupportActionBar(theme_toolbar)
        theme_toolbar.setNavigationOnClickListener { onBackPressed() }

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = ThemeAdapter()
    }

    internal inner class ThemeAdapter : RecyclerView.Adapter<ThemeAdapter.ThemeItemHolder>() {
        val resources: Resources = getResources()
        val theme: Resources.Theme = getTheme()
        private val white = resources.getColor(android.R.color.white, theme)
        private val grey = resources.getColor(R.color.secondary_text, theme)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeItemHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_theme, parent, false)
            return ThemeItemHolder(view)
        }

        override fun onBindViewHolder(holder: ThemeItemHolder, position: Int) {
            val res = ThemeRes.values()[position]
            val themeColor = resources.getColor(res.color, theme)
            holder.themeId = res.style
            holder.v.btn_choose.setOnClickListener { ThemeHelper.setTheme(this@ThemeActivity, holder.themeId) }
            holder.v.tv_title.setText(res.title)
            holder.v.tv_title.setTextColor(themeColor)
            if (ThemeHelper.currentTheme == res.style) {
                with(holder.v.btn_choose) {
                    isChecked = true
                    setTextColor(themeColor)
                    setText(R.string.theme_using)
                }
                holder.v.color_dot.setTextColor(white)
            } else {
                with(holder.v.btn_choose) {
                    isChecked = false
                    setTextColor(grey)
                    setText(R.string.theme_use)
                }
                holder.v.color_dot.setTextColor(themeColor)
            }
            val gradientDrawable = GradientDrawable()
            gradientDrawable.setColor(themeColor)
            gradientDrawable.cornerRadius = 40f
            holder.v.color_dot.background = gradientDrawable
        }

        override fun getItemCount() = ThemeRes.values().size

        internal inner class ThemeItemHolder(val v: View, var themeId: Int = 0) : RecyclerView.ViewHolder(v)
    }
}
