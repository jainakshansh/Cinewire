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

import me.akshanshjain.popularmovies.Object.ReviewItem;
import me.akshanshjain.popularmovies.R;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private Context context;
    private List<ReviewItem> reviewItemList;
    private Typeface qReg;

    public ReviewAdapter(Context context, List<ReviewItem> reviewItemList) {
        this.context = context;
        this.reviewItemList = reviewItemList;
        qReg = Typeface.createFromAsset(context.getAssets(), "fonts/Quicksand-Medium.ttf");
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {

        public TextView authorTv, contentTv;

        public ReviewViewHolder(View view) {
            super(view);
            authorTv = view.findViewById(R.id.review_author);
            contentTv = view.findViewById(R.id.review_content);
        }
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reviews, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        final ReviewItem reviewItem = reviewItemList.get(position);

        holder.authorTv.setTypeface(qReg);
        holder.contentTv.setTypeface(qReg);

        holder.authorTv.setText(reviewItem.getAuthor());
        holder.contentTv.setText(reviewItem.getContent());
    }

    @Override
    public int getItemCount() {
        return reviewItemList.size();
    }
}
