package xyz.b515.schedule.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.jrummyapps.android.colorpicker.ColorPanelView;
import com.jrummyapps.android.colorpicker.ColorPickerDialog;
import com.jrummyapps.android.colorpicker.ColorPickerDialogListener;
import com.jrummyapps.android.colorpicker.ColorShape;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.b515.schedule.Constant;
import xyz.b515.schedule.R;
import xyz.b515.schedule.db.CourseManager;
import xyz.b515.schedule.entity.Course;
import xyz.b515.schedule.entity.Spacetime;
import xyz.b515.schedule.ui.adapter.SpacetimeAdapter;

public class CourseDetailActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recycler) RecyclerView recycler;
    @BindView(R.id.course_name) EditText nameView;
    @BindView(R.id.course_teacher) EditText teacherView;
    @BindView(R.id.cpv_color_panel_view) ColorPanelView colorPanelView;
    CourseManager manager;
    SpacetimeAdapter adapter;
    Course course;
    Boolean flag;
    ArrayList<Spacetime> list = new ArrayList<>();
    int courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        ButterKnife.bind(this);

        manager = new CourseManager(this);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(llm);

        Intent intent = getIntent();
        flag = intent.getBooleanExtra(Constant.TOOLBAR_TITLE, true);
        adapter = new SpacetimeAdapter(list, this, manager, flag);
        recycler.setAdapter(adapter);
        courseId = intent.getIntExtra(Constant.COURSE_ID, 0);
        toolbar.setTitle(flag ? R.string.course_new : R.string.course_edit);
        setSupportActionBar(toolbar);

        if (flag) {
            course = new Course();
            manager.insertCourse(course);
        } else {
            course = manager.getCourse(courseId);
        }
        loadSpacetime();
        colorPanelView.setOnClickListener(v -> {
            ColorPickerDialog dialog = ColorPickerDialog.newBuilder()
                    .setColor(course.getColor())
                    .setColorShape(ColorShape.CIRCLE)
                    .setShowAlphaSlider(true)
                    .create();
            dialog.setColorPickerDialogListener(new ColorPickerDialogListener() {
                @Override
                public void onColorSelected(int dialogId, int color) {
                    course.setColor(color);
                    colorPanelView.setColor(color);
                }

                @Override
                public void onDialogDismissed(int dialogId) {
                }
            });
            dialog.show(getFragmentManager(), "");
        });
        toolbar.setNavigationOnClickListener(v -> {
            if (flag) {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.alert_title)
                        .setMessage(R.string.alert_discard)
                        .setPositiveButton(R.string.alert_pos, (dialog, which) -> {
                            manager.deleteCourse(course);
                            onBackPressed();
                        })
                        .setNegativeButton(R.string.alert_neg, (dialog, which) -> {
                        })
                        .show();
            } else onBackPressed();
        });
        toolbar.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.action_confirm) {
                //Confirm
                course.setName(nameView.getText().toString());
                course.setTeacher(teacherView.getText().toString());
                manager.updateCourse(course);
                for (Spacetime st : adapter.items) manager.updateSpacetime(st);
                onBackPressed();
            } else if (menuItem.getItemId() == R.id.action_delete_forever) {
                //Delete
                new AlertDialog.Builder(this)
                        .setTitle(R.string.alert_title)
                        .setMessage(R.string.alert_message)
                        .setPositiveButton(R.string.alert_pos, (dialog, which) -> {
                            manager.deleteCourse(course);
                            onBackPressed();
                        })
                        .setNegativeButton(R.string.alert_neg, (dialog, which) -> {
                            onBackPressed();
                        })
                        .show();
            } else if (menuItem.getItemId() == R.id.action_add_spacetime) {
                Spacetime spacetime = new Spacetime();
                spacetime.setCourse(course);
                manager.insertSpacetime(spacetime);
                adapter.items.add(spacetime);
                adapter.notifyDataSetChanged();
            }
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course_detail, menu);
        MenuItem deleteAction = menu.findItem(R.id.action_delete_forever);
        deleteAction.setVisible(!flag);
        return true;
    }

    private void loadSpacetime() {
        adapter.items.clear();
        if (!flag) {
            list.addAll(manager.getCourse(courseId).getSpacetimes());
            nameView.setText(course.getName());
            teacherView.setText(course.getTeacher());
            colorPanelView.setColor(course.getColor());
        }
        adapter.notifyDataSetChanged();
    }
}
