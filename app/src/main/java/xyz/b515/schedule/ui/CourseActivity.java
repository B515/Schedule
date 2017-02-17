package xyz.b515.schedule.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.b515.schedule.R;

public class CourseActivity extends AppCompatActivity {
    @BindView(R.id.course_toolbar) Toolbar toolbar;
    Boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        flag = intent.getBooleanExtra("toolbar_title", true);
        toolbar.setTitle(flag ? R.string.course_new : R.string.course_edit);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        toolbar.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.action_confirm) {
                //TODO Actions after confirm

                onBackPressed();
                return true;
            }
            if (menuItem.getItemId() == R.id.action_delete_forever) {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.alert_title)
                        .setMessage(R.string.alert_title)
                        .setPositiveButton(R.string.alert_pos, (dialog, which) -> {
                            //TODO Actions after confirm

                            onBackPressed();
                        })
                        .setNegativeButton(R.string.alert_neg, (dialog, which) -> {
                            onBackPressed();
                        })
                        .show();
                return true;
            }
            return false;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_course, menu);
        MenuItem deleteAction = menu.findItem(R.id.action_delete_forever);
        deleteAction.setVisible(!flag);
        return true;
    }
}
