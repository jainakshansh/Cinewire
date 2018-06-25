package me.akshanshjain.popularmovies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.akshanshjain.popularmovies.Object.TrailerItem;
import me.akshanshjain.popularmovies.R;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private Context context;
    private List<TrailerItem> trailerItemList;
    private Typeface qReg;

    public TrailerAdapter(Context context, List<TrailerItem> trailerItemList) {
        this.context = context;
        this.trailerItemList = trailerItemList;
        qReg = Typeface.createFromAsset(context.getAssets(), "fonts/Quicksand-Medium.ttf");
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder {

        public TextView trailerName;

        public TrailerViewHolder(View view) {
            super(view);
            trailerName = view.findViewById(R.id.trailer_name);
        }
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trailers, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TrailerViewHolder holder, int position) {
        final TrailerItem trailerItem = trailerItemList.get(position);

        holder.trailerName.setTypeface(qReg);
        holder.trailerName.setText(trailerItem.getName());

        final String videoUrl = context.getResources().getString(R.string.trailerBase) + trailerItem.getKey();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent videoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
                videoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(videoIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trailerItemList.size();
    }
}
