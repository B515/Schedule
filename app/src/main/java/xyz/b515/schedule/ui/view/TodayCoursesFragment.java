package xyz.b515.schedule.ui.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.stream.StreamSupport;
import xyz.b515.schedule.Constant;
import xyz.b515.schedule.R;
import xyz.b515.schedule.db.CourseManager;
import xyz.b515.schedule.entity.Course;
import xyz.b515.schedule.ui.adapter.CourseAdapter;

/**
 * Created by Yun on 2017.4.24.
 */

public class TodayCoursesFragment extends Fragment {

    @BindView(R.id.recycler) RecyclerView recycler;
    CourseAdapter adapter;
    CourseManager manager;
    SharedPreferences preferences;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, container, false);
        ButterKnife.bind(this, view);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(llm);
        adapter = new CourseAdapter(new ArrayList<>());
        recycler.setAdapter(adapter);
        recycler.setItemAnimator(new DefaultItemAnimator());

        int today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int currentWeek = preferences.getInt(Constant.CURRENT_WEEK, -1);
        adapter.items.clear();
        manager = new CourseManager(getContext());
        List<Course> list = manager.getAllCourse();
        if (list != null) {
            StreamSupport.stream(list)
                    .flatMap(course -> StreamSupport.stream(course.getSpacetimes()))
                    .filter(spacetime -> spacetime.getWeekday() == today)
                    .filter(spacetime -> spacetime.getStartWeek() <= currentWeek && spacetime.getEndWeek() >= currentWeek)
                    .forEach(spacetime -> adapter.items.add(spacetime.getCourse()));
        }
        adapter.notifyDataSetChanged();

        return view;
    }
}
