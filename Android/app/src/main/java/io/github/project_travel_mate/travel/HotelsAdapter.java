package io.github.project_travel_mate.travel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.project_travel_mate.R;

class HotelsAdapter extends RecyclerView.Adapter<HotelsAdapter.HotelsViewHolder> {

    private final Context mContext;
    private List<HotelsModel> mHotelsModelList;

    HotelsAdapter(Context context, List<HotelsModel> hotelsModelList) {
        this.mContext = context;
        this.mHotelsModelList = hotelsModelList;
    }

    @NonNull
    @Override
    public HotelsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hotel_listitem, parent, false);
        return new HotelsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelsViewHolder holder, int position) {
        try {
            holder.title.setText(mHotelsModelList.get(position).getTitle());
            holder.description.setText(android.text.Html.fromHtml(
                    mHotelsModelList.get(position).getAddress()));
            holder.distance.setText(new DecimalFormat("##.##").format(
                    (float) mHotelsModelList.get(position).getDistance() / 1000));
            holder.call.setOnClickListener(view -> {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                try {
                    intent.setData(Uri.parse("tel:" + mHotelsModelList.get(position).getPhone()));
                    mContext.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    ((HotelsActivity) mContext).networkError();
                }
            });

            holder.map.setOnClickListener(view -> {
                Intent browserIntent;
                try {
                    Double latitude = mHotelsModelList.get(position).getLatitude();
                    Double longitude = mHotelsModelList.get(position).getLongitude();
                    browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.google.com/maps?q="
                                    + mHotelsModelList.get(position).getTitle()
                                    + "+(name)+@" + latitude + "," + longitude));
                    mContext.startActivity(browserIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                    ((HotelsActivity) mContext).networkError();
                }
            });
            holder.book.setOnClickListener(view -> {
                Intent browserIntent;
                try {
                    browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(mHotelsModelList.get(position).getHref()));
                    mContext.startActivity(browserIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                    ((HotelsActivity) mContext).networkError();
                }
            });

            holder.expand_more_details.setOnClickListener(v -> {
                if (holder.detailsLayout.getVisibility() == View.GONE) {
                    holder.detailsLayout.setVisibility(View.VISIBLE);
                    holder.expandCollapse.setImageResource(
                            R.drawable.ic_keyboard_arrow_up_black_24dp);
                } else {
                    holder.detailsLayout.setVisibility(View.GONE);
                    holder.expandCollapse.setImageResource(
                            R.drawable.ic_keyboard_arrow_down_black_24dp);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERROR : ", "Message : " + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return mHotelsModelList.size();
    }

    class HotelsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.hotel_name)
        TextView title;
        @BindView(R.id.hotel_address)
        TextView description;
        @BindView(R.id.call)
        RelativeLayout call;
        @BindView(R.id.map)
        RelativeLayout map;
        @BindView(R.id.book)
        RelativeLayout book;
        @BindView(R.id.more_details)
        LinearLayout detailsLayout;
        @BindView(R.id.expand_collapse)
        ImageView expandCollapse;
        @BindView(R.id.distance)
        TextView distance;
        @BindView(R.id.expand_more_details)
        RelativeLayout expand_more_details;

        private HotelsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
