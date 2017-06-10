package xyz.b515.schedule.ui.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.b515.schedule.Constant;
import xyz.b515.schedule.R;
import xyz.b515.schedule.entity.Course;
import xyz.b515.schedule.entity.Spacetime;
import xyz.b515.schedule.ui.view.CourseDetailActivity;

/**
 * Created by Yun on 2017.4.24.
 */

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    public List<Course> items;

    public CourseAdapter(List<Course> items) {
        this.items = items;
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.item_course_summary, viewGroup, false);
        return new CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, int position) {
        holder.tvName.setText(items.get(position).getName());
        String locations = items.get(position).getSpacetimes().stream()
                .map(Spacetime::getLocation)
                .distinct()
                .collect(Collectors.joining(", "));
        holder.tvLocation.setText(locations);
        holder.tvTeacher.setText(items.get(position).getTeacher());

        String[] weeks = DateFormatSymbols.getInstance().getShortWeekdays();
        String times = items.get(position).getSpacetimes().stream()
                .map(st -> weeks[st.getWeekday()] + " " + st.getStartTime() + "-" + st.getEndTime())
                .collect(Collectors.joining(", "));
        holder.tvTime.setText(times);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), CourseDetailActivity.class);
            intent.putExtra(Constant.TOOLBAR_TITLE, false);
            intent.putExtra(Constant.COURSE_ID, items.get(position).getId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }


    class CourseViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name) TextView tvName;
        @BindView(R.id.tv_teacher) TextView tvTeacher;
        @BindView(R.id.tv_location) TextView tvLocation;
        @BindView(R.id.tv_time) TextView tvTime;

        CourseViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
