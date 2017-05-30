package xyz.b515.schedule.ui.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import xyz.b515.schedule.Constant;
import xyz.b515.schedule.R;
import xyz.b515.schedule.api.ZfRetrofit;
import xyz.b515.schedule.api.ZfService;
import xyz.b515.schedule.db.CourseManager;
import xyz.b515.schedule.entity.Course;
import xyz.b515.schedule.ui.adapter.CourseAdapter;
import xyz.b515.schedule.util.CourseParser;
import xyz.b515.schedule.util.FileHelper;

@RuntimePermissions
public class CourseManageActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_course_manage);
        ButterKnife.bind(this);
        toolbar.setTitle(R.string.course_manage);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, CourseDetailActivity.class);
            intent.putExtra(Constant.TOOLBAR_TITLE, true);
            startActivity(intent);
        });

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(llm);
        adapter = new CourseAdapter(new ArrayList<>());
        recycler.setAdapter(adapter);
        recycler.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCourses();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course_manage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_clear: {
                manager.clearCourse();
                loadCourses();
            }
            break;
            case R.id.action_import: {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                getCourses(prefs.getString("user", null), prefs.getString("password", null));
            }
            break;
            case R.id.action_import_file: {
                CourseManageActivityPermissionsDispatcher.showFileChooserWithCheck(CourseManageActivity.this);
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadCourses() {
        adapter.items.clear();
        manager = new CourseManager(this);
        List<Course> list = manager.getAllCourse();
        if (list != null)
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(text -> {
                            manager.clearCourse();
                            CourseParser.parse(text, manager);
                            loadCourses();
                        },
                        throwable -> {
                            dismissProgressDialog();
                            Snackbar.make(recycler, "Error!!!" + throwable.getMessage(), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            throwable.printStackTrace();
                        },
                        () -> dismissProgressDialog(),
                        dis -> progressDialog = ProgressDialog.show(CourseManageActivity.this, "Schedule", "Now loading...", true, true, dialogInterface -> dis.dispose()));
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

    private static final int FILE_SELECT_CODE = 0;

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, ""), FILE_SELECT_CODE);
        } catch (Exception e) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    try {
                        Uri uri = data.getData();
                        String path = FileHelper.getPath(this, uri);
                        File file = new File(path);
                        FileInputStream in = new FileInputStream(file);
                        int length = (int) file.length();
                        byte[] temp = new byte[length];
                        in.read(temp, 0, length);
                        String text = new String(temp, "gb2312");
                        in.close();

                        CourseManager manager = new CourseManager(CourseManageActivity.this);
                        manager.clearCourse();
                        CourseParser.parse(text, manager);
                        loadCourses();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
