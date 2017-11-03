package xyz.b515.schedule.ui.view

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.inputmethod.InputMethodManager
import com.jrummyapps.android.colorpicker.ColorPanelView
import com.jrummyapps.android.colorpicker.ColorPickerDialog
import com.jrummyapps.android.colorpicker.ColorPickerDialogListener
import com.jrummyapps.android.colorpicker.ColorShape
import kotlinx.android.synthetic.main.activity_course_detail.*
import kotlinx.android.synthetic.main.app_bar.*
import xyz.b515.schedule.Constant
import xyz.b515.schedule.R
import xyz.b515.schedule.db.CourseManager
import xyz.b515.schedule.entity.Course
import xyz.b515.schedule.entity.Spacetime
import xyz.b515.schedule.ui.adapter.SpacetimeAdapter
import java.util.*

class CourseDetailActivity : AppCompatActivity() {
    val colorPanelView: ColorPanelView by lazy { findViewById<ColorPanelView>(R.id.cpv_color_panel_view) }
    lateinit var manager: CourseManager
    lateinit var adapter: SpacetimeAdapter
    lateinit var course: Course
    private var flag: Boolean = true
    private var list = ArrayList<Spacetime>()
    private var courseId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_detail)

        manager = CourseManager(this)
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        recycler.layoutManager = llm

        val intent = intent
        flag = intent.getBooleanExtra(Constant.TOOLBAR_TITLE, true)
        adapter = SpacetimeAdapter(list, this, manager, flag)
        recycler.adapter = adapter
        courseId = intent.getIntExtra(Constant.COURSE_ID, 0)
        toolbar.setTitle(if (flag) R.string.course_new else R.string.course_edit)
        setSupportActionBar(toolbar)

        if (flag) {
            course = Course()
            manager.insertCourse(course)
        } else {
            course = manager.getCourse(courseId)
        }
        loadSpacetime()
        colorPanelView.setOnClickListener {
            val dialog = ColorPickerDialog.newBuilder()
                    .setColor(course.color)
                    .setColorShape(ColorShape.CIRCLE)
                    .setShowAlphaSlider(true)
                    .create()
            dialog.setColorPickerDialogListener(object : ColorPickerDialogListener {
                override fun onColorSelected(dialogId: Int, color: Int) {
                    course.color = color
                    colorPanelView.color = color
                }

                override fun onDialogDismissed(dialogId: Int) {}
            })
            dialog.show(fragmentManager, "")
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
        toolbar.setNavigationOnClickListener { onBackPressed() }
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_confirm -> {
                    course.name = course_name.text.toString()
                    course.teacher = course_teacher.text.toString()
                    manager.updateCourse(course)
                    adapter.items.forEach { manager.updateSpacetime(it) }
                    finish()
                }
                R.id.action_delete_forever -> showDeleteConfirm()
                R.id.action_add_spacetime -> {
                    val spacetime = Spacetime()
                    spacetime.course = course
                    manager.insertSpacetime(spacetime)
                    adapter.items.add(spacetime)
                    adapter.notifyDataSetChanged()
                }
            }
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_course_detail, menu)
        val deleteAction = menu.findItem(R.id.action_delete_forever)
        deleteAction.isVisible = !flag
        return true
    }

    override fun onBackPressed() {
        if (flag) {
            showDiscardConfirm()
        } else
            super.onBackPressed()
    }

    private fun showDeleteConfirm() {
        AlertDialog.Builder(this)
                .setTitle(R.string.alert_title)
                .setMessage(R.string.alert_message)
                .setPositiveButton(R.string.alert_pos) { _, _ ->
                    manager.deleteCourse(course)
                    finish()
                }
                .setNegativeButton(R.string.alert_neg) { _, _ -> }
                .show()
    }

    private fun showDiscardConfirm() {
        AlertDialog.Builder(this)
                .setTitle(R.string.alert_title)
                .setMessage(R.string.alert_discard)
                .setPositiveButton(R.string.alert_pos) { _, _ ->
                    if (flag) {
                        manager.deleteCourse(course)
                    }
                    finish()
                }
                .setNegativeButton(R.string.alert_neg) { _, _ -> }
                .show()
    }

    private fun loadSpacetime() {
        adapter.items.clear()
        if (!flag) {
            list.addAll(manager.getCourse(courseId).spacetimes!!)
            course_name.setText(course.name)
            course_teacher.setText(course.teacher)
            colorPanelView.color = course.color
        }
        adapter.notifyDataSetChanged()
    }
}
