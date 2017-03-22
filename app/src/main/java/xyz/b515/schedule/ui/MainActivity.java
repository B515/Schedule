package xyz.b515.schedule.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import xyz.b515.schedule.util.CourseParser;

public class MainActivity extends AppCompatActivity {
    private CoordinatorLayout coordinatorLayout;
    private SharedPreferences prefs;
    private ProgressDialog progressDialog;
    Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        coordinatorLayout = ButterKnife.findById(this, R.id.coordinator);
        Toolbar toolbar = ButterKnife.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        FloatingActionButton fab = ButterKnife.findById(this, R.id.fab);
        fab.setOnClickListener(view -> {
            getCourses(prefs.getString("user", null), prefs.getString("password", null));
        });

        addViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add: {
                Intent intent = new Intent(this, CourseActivity.class);
                intent.putExtra("toolbar_title", true);
                startActivity(intent);
            }
            break;
            case R.id.action_refresh: {

            }
            break;
            case R.id.action_settings: {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.action_capture: {

            }
            break;
        }
        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    private void addViews() {
        GridLayout gridLayout = (GridLayout) findViewById(R.id.main_grid);
        int column = 8;
        int row = 13;
        for (int i = 1; i < column; i++) {
            TextView textView = new TextView(this);
            textView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
            textView.setText(getResources().getStringArray(R.array.weekdays)[i - 1]);

            GridLayout.Spec rowSpan = GridLayout.spec(0, 1);
            GridLayout.Spec colSpan = GridLayout.spec(i, 1);
            GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(rowSpan, colSpan);
            gridLayout.addView(textView, gridParam);
        }
        for (int i = 1; i < row; i++) {
            TextView textView = new TextView(this);
            textView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
            textView.setText(getResources().getStringArray(R.array.row)[i - 1]);

            GridLayout.Spec rowSpan = GridLayout.spec(i, 1);
            GridLayout.Spec colSpan = GridLayout.spec(0, 1);
            GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(rowSpan, colSpan);
            gridLayout.addView(textView, gridParam);
        }
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
                            CourseManager manager = new CourseManager(MainActivity.this);
                            manager.clearCourse();
                            manager.insertCourse(courses);

                            String allCourses = "";
                            for (Course course : courses) {
                                allCourses = allCourses.concat(course.toString());
                            }
                            new AlertDialog.Builder(MainActivity.this).setMessage(allCourses).show();
                            //TODO Refresh layouts
                        },
                        throwable -> {
                            dismissProgressDialog();
                            Snackbar.make(coordinatorLayout, "Error!!!" + throwable.getMessage(), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            throwable.printStackTrace();
                        },
                        () -> dismissProgressDialog(),
                        dis -> progressDialog = ProgressDialog.show(MainActivity.this, "Schedule", "Now loading...", true, true, dialogInterface -> dis.dispose()));
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
