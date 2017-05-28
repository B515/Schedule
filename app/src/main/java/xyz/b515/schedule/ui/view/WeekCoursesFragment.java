package xyz.b515.schedule.ui.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.stream.StreamSupport;
import xyz.b515.schedule.Constant;
import xyz.b515.schedule.R;
import xyz.b515.schedule.db.CourseManager;
import xyz.b515.schedule.entity.Course;
import xyz.b515.schedule.entity.Spacetime;

/**
 * Created by Yun on 2017.4.24.
 */

public class WeekCoursesFragment extends Fragment {
    View view;
    @BindView(R.id.header_weeks) LinearLayout headerWeeks;
    @BindView(R.id.header_time) LinearLayout headerTime;
    @BindView(R.id.courses) RelativeLayout coursesLayout;
    CourseManager manager;
    SharedPreferences preferences;
    private int baseHeight;
    private int baseWidth;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_week, container, false);
        ButterKnife.bind(this, view);
        manager = new CourseManager(getContext());
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        showHeaders();
        showCourses();
        return view;
    }

    private void showHeaders() {
        final int column = 7;
        final int row = 12;

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        baseHeight = (int) Math.round(metrics.heightPixels * (1.0 / 15.0));
        baseWidth = (int) Math.round(metrics.widthPixels * (2.0 / 15.0));

        for (int i = 0; i < column; i++) {
            TextView tv = new TextView(getContext());
            tv.setText(getResources().getStringArray(R.array.weekdays)[i]);
            tv.setGravity(Gravity.CENTER);
            tv.setWidth(baseWidth);
            headerWeeks.addView(tv);
        }
        for (int i = 1; i <= row; i++) {
            TextView tv = new TextView(getContext());
            tv.setText(String.valueOf(i));
            tv.setGravity(Gravity.CENTER);
            tv.setHeight(baseHeight);
            headerTime.addView(tv);
        }
    }

    private void showCourses() {
        int currentWeek = preferences.getInt(Constant.CURRENT_WEEK, -1);
        List<Course> list = manager.getAllCourse();
        if (list != null) {
            StreamSupport.stream(list)
                    .flatMap(course -> StreamSupport.stream(course.getSpacetimes()))
                    .filter(spacetime -> spacetime.getStartWeek() <= currentWeek && spacetime.getEndWeek() >= currentWeek)
                    .forEach(this::addCourseToView);
        }
    }

    private void addCourseToView(Spacetime spacetime) {
        Course course = spacetime.getCourse();

        LinearLayout v = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_course_view, coursesLayout, false);
        TextView textView = ButterKnife.findById(v, R.id.course_name);
        textView.setText(course.getName() + "\n" + spacetime.getLocation());
        v.setBackgroundColor(course.getColor());
        v.setX((spacetime.getWeekday() - 1) * baseWidth);
        v.setY((spacetime.getStartTime() - 1) * baseHeight);
        coursesLayout.addView(v);
        v.getLayoutParams().height = (spacetime.getEndTime() - spacetime.getStartTime() + 1) * baseHeight;
        v.getLayoutParams().width = baseWidth;
        v.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), CourseDetailActivity.class);
            intent.putExtra(Constant.TOOLBAR_TITLE, false);
            intent.putExtra(Constant.COURSE_ID, course.getId());
            getContext().startActivity(intent);
        });
    }

}
