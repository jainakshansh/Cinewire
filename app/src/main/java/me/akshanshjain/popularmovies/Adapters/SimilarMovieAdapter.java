package me.akshanshjain.popularmovies.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.akshanshjain.popularmovies.Object.SimilarItem;
import me.akshanshjain.popularmovies.R;

public class SimilarMovieAdapter extends RecyclerView.Adapter<SimilarMovieAdapter.SimilarViewHolder> {

    private Context context;
    private List<SimilarItem> similarItemList;
    private Typeface qReg;

    public SimilarMovieAdapter(Context context, List<SimilarItem> similarItemList) {
        this.context = context;
        this.similarItemList = similarItemList;
        qReg = Typeface.createFromAsset(context.getAssets(), "fonts/Quicksand-Medium.ttf");
    }

    public class SimilarViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView posterImageView;
        public TextView nameTextView, overviewTextView;

        public SimilarViewHolder(View view) {
            super(view);
        }
    }

    @NonNull
    @Override
    public SimilarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_similar_movies, parent, false);
        return new SimilarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimilarViewHolder holder, int position) {
        SimilarItem similarItem = similarItemList.get(position);

        Picasso.get()
                .load(similarItem.getImage())
                .into(holder.posterImageView);

        holder.nameTextView.setTypeface(qReg);
        holder.nameTextView.setText(similarItem.getName());

        holder.overviewTextView.setTypeface(qReg);
        holder.overviewTextView.setText(similarItem.getOverview());
    }

    @Override
    public int getItemCount() {
        return similarItemList.size();
    }
}
