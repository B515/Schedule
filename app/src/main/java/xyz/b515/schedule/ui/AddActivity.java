package xyz.b515.schedule.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import xyz.b515.schedule.R;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.add_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        toolbar.setOnMenuItemClickListener(menuItem -> {
            if(menuItem.getItemId()==R.id.action_confirm){
                //TODO: Actions after confirm

                onBackPressed();
                return true;
            }
            return false;
        });
    }
}
