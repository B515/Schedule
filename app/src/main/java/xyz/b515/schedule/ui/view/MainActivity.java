package xyz.b515.schedule.ui.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.stream.Collectors;
import java8.util.stream.RefStreams;
import xyz.b515.schedule.Constant;
import xyz.b515.schedule.R;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.spinner) Spinner spinner;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        title.setText(R.string.app_name);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        checkCurrentWeek();

        setSupportActionBar(toolbar);

        viewPager.setAdapter(new CourseFragmentAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        List<String> weeks = RefStreams.iterate(1, i -> i + 1)
                .limit(20)
                .map(i -> String.format(getResources().getString(R.string.week_num), i))
                .collect(Collectors.toList());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, weeks);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(preferences.getInt(Constant.CURRENT_WEEK, 0));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                preferences.edit().putInt(Constant.CURRENT_WEEK, position).apply();
                reload();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        reload();
    }

    private void reload() {
        int page = viewPager.getCurrentItem();
        viewPager.setAdapter(new CourseFragmentAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(page);
    }

    private void checkCurrentWeek() {
        int newWeekOfYear = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        int oldWeek = preferences.getInt(Constant.CURRENT_WEEK, -1);
        int oldWeekOfYear = preferences.getInt(Constant.CURRENT_WEEK_OF_YEAR, newWeekOfYear - 1);
        if (newWeekOfYear > oldWeekOfYear) {
            int newWeek = oldWeek + newWeekOfYear - oldWeekOfYear;
            preferences.edit().putInt(Constant.CURRENT_WEEK, newWeek < 20 ? newWeek : 0)
                    .putInt(Constant.CURRENT_WEEK_OF_YEAR, newWeekOfYear)
                    .apply();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_manage: {
                Intent intent = new Intent(this, CourseManageActivity.class);
                startActivity(intent);
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
        return super.onOptionsItemSelected(item);
    }

    private class CourseFragmentAdapter extends FragmentPagerAdapter {

        CourseFragmentAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new TodayCoursesFragment();
                default:
                    return new WeekCoursesFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.main_today_courses);
                default:
                    return getResources().getString(R.string.main_week_courses);
            }
        }
    }
}
