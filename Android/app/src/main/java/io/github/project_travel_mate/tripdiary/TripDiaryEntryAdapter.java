package io.github.project_travel_mate.tripdiary;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import io.github.project_travel_mate.R;

public class TripDiaryEntryAdapter extends RecyclerView.Adapter<TripDiaryEntryAdapter.ViewHolder> {

    private final Context mContext;
    private final List<TripDiaryEntry> mEntries;

    public TripDiaryEntryAdapter(Context context, List<TripDiaryEntry> entries) {
        mContext = context;
        mEntries = entries;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_trip_diary_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TripDiaryEntry entry = mEntries.get(position);
        holder.text.setText(entry.getText());
        Picasso.with(mContext).load(entry.getImageUri()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mEntries.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView text;

        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.entry_image);
            text = itemView.findViewById(R.id.entry_text);
        }
    }
}
