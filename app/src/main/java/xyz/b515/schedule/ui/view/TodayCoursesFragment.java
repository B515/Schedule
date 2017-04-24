package xyz.b515.schedule.ui.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.b515.schedule.R;
import xyz.b515.schedule.db.CourseManager;
import xyz.b515.schedule.ui.adapter.CourseAdapter;

/**
 * Created by Yun on 2017.4.24.
 */

public class TodayCoursesFragment extends Fragment {

    @BindView(R.id.recycler) RecyclerView recycler;
    CourseAdapter adapter;
    CourseManager manager;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(llm);
        adapter = new CourseAdapter(new ArrayList<>());
        recycler.setAdapter(adapter);
        recycler.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), llm.getOrientation());
        recycler.addItemDecoration(dividerItemDecoration);

        manager = new CourseManager(getContext());
        adapter.items.addAll(manager.getAllCourse());
        adapter.notifyDataSetChanged();

        return view;
    }
}
