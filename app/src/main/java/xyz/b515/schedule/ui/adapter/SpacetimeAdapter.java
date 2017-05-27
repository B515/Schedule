package xyz.b515.schedule.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.text.DateFormatSymbols;
import java.util.List;

import xyz.b515.schedule.R;
import xyz.b515.schedule.databinding.ItemSpacetimeBinding;
import xyz.b515.schedule.db.CourseManager;
import xyz.b515.schedule.entity.Spacetime;

/**
 * Created by ZeroGo on 2017/5/25.
 */

public class SpacetimeAdapter extends RecyclerView.Adapter<SpacetimeAdapter.SpacetimeViewHolder> {
    public List<Spacetime> items;
    private Context context;
    private CourseManager manager;
    private Boolean flag;

    public SpacetimeAdapter(List<Spacetime> items, Context context, CourseManager manager, Boolean flag) {
        this.items = items;
        this.context = context;
        this.manager = manager;
        this.flag = flag;
    }

    @Override
    public SpacetimeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ItemSpacetimeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.item_spacetime, viewGroup, false);

        return new SpacetimeViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(SpacetimeViewHolder holder, int position) {
        holder.binding.setSpacetime(items.get(position));
        String[] weeks = DateFormatSymbols.getInstance().getShortWeekdays();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, weeks);
        holder.binding.spinner.setAdapter(adapter);
        if(!flag) {
            holder.binding.spinner.setSelection(items.get(position).getWeekday());
        }
        holder.binding.spacetimeDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.alert_title)
                    .setMessage(R.string.alert_message)
                    .setPositiveButton(R.string.alert_pos, (dialog, which) -> {
                        manager.deleteSpacetime(items.get(position));
                        items.remove(position);
                        notifyDataSetChanged();
                    })
                    .setNegativeButton(R.string.alert_neg, (dialog, which) -> {
                    })
                    .show();
        });
        holder.binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                items.get(position).setWeekday(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();

    }

    class SpacetimeViewHolder extends RecyclerView.ViewHolder {
        ItemSpacetimeBinding binding;

        SpacetimeViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.getBinding(view);
        }
    }
}
