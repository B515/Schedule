package xyz.b515.schedule.ui.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import xyz.b515.schedule.R;

/**
 * Created by Yun on 2017.4.24.
 */

public class WeekCoursesFragment extends Fragment {

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_week, container, false);
        ButterKnife.bind(this, view);

        addViews(view);
        return view;
    }

    private void addViews(View view) {
        GridLayout gridLayout = (GridLayout) view.findViewById(R.id.grid);
        int column = 8;
        int row = 13;
        for (int i = 1; i < column; i++) {
            TextView textView = new TextView(getContext());
            textView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
            textView.setText(getResources().getStringArray(R.array.weekdays)[i - 1]);

            GridLayout.Spec rowSpan = GridLayout.spec(0, 1);
            GridLayout.Spec colSpan = GridLayout.spec(i, 1);
            GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(rowSpan, colSpan);
            gridLayout.addView(textView, gridParam);
        }
        for (int i = 1; i < row; i++) {
            TextView textView = new TextView(getContext());
            textView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
            textView.setText(getResources().getStringArray(R.array.row)[i - 1]);

            GridLayout.Spec rowSpan = GridLayout.spec(i, 1);
            GridLayout.Spec colSpan = GridLayout.spec(0, 1);
            GridLayout.LayoutParams gridParam = new GridLayout.LayoutParams(rowSpan, colSpan);
            gridLayout.addView(textView, gridParam);
        }
    }

}
