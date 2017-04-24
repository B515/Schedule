package xyz.b515.schedule.ui.view;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import xyz.b515.schedule.R;
import xyz.b515.schedule.api.ZfRetrofit;
import xyz.b515.schedule.api.ZfService;
import xyz.b515.schedule.db.CourseManager;
import xyz.b515.schedule.entity.Course;
import xyz.b515.schedule.ui.adapter.CourseAdapter;
import xyz.b515.schedule.util.CourseParser;

public class AllCoursesActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recycler) RecyclerView recycler;
    @BindView(R.id.fab) FloatingActionButton fab;
    CourseAdapter adapter;
    CourseManager manager;
    private ProgressDialog progressDialog;
    Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_cources);
        ButterKnife.bind(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        fab.setOnClickListener(v->getCourses(prefs.getString("user", null), prefs.getString("password", null)));

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(llm);
        adapter = new CourseAdapter(new ArrayList<>());
        recycler.setAdapter(adapter);
        recycler.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, llm.getOrientation());
        recycler.addItemDecoration(dividerItemDecoration);

        manager = new CourseManager(this);
        adapter.items.addAll(manager.getAllCourse());
        adapter.notifyDataSetChanged();
    }

    private void getCourses(String user, String password) {
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();

        Map<String, String> loginMap = new HashMap<>();
        loginMap.put("TextBox1", user);
        loginMap.put("TextBox2", password);
        loginMap.put("RadioButtonList1_2", "%D1%A7%C9%FA");
        loginMap.put("Button1", "");

        Map<String, String> scheduleMap = new HashMap<>();
        scheduleMap.put("xh", user);
        scheduleMap.put("gnmkdm", "N121603");

        final ZfService zfService = ZfRetrofit.getZfService();
        disposable = zfService.login(loginMap)
                .flatMap(s -> {
                    //TODO check login state
                    return zfService.getSchedule(scheduleMap);
                })
                .map((Function<String, List<Course>>) CourseParser::parse)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(courses -> {
                            CourseManager manager = new CourseManager(AllCoursesActivity.this);
                            manager.clearCourse();
                            manager.insertCourse(courses);
                        },
                        throwable -> {
                            dismissProgressDialog();
                            Snackbar.make(recycler, "Error!!!" + throwable.getMessage(), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            throwable.printStackTrace();
                        },
                        () -> dismissProgressDialog(),
                        dis -> progressDialog = ProgressDialog.show(AllCoursesActivity.this, "Schedule", "Now loading...", true, true, dialogInterface -> dis.dispose()));
    }

    @Override
    protected void onStop() {
        super.onStop();
        dismissProgressDialog();
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
