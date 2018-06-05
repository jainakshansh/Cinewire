package me.akshanshjain.popularmovies.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import me.akshanshjain.popularmovies.Object.MovieItem;
import me.akshanshjain.popularmovies.R;

/**
 * Created by Akshansh on 05-06-2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Context context;
    private List<MovieItem> movieItemList;
    private Typeface qMed;

    private static final int LIST_ITEM = 100;
    private static final int GRID_ITEM = 101;
    boolean isGrid = false;

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView moviePoster;
        TextView movieName, movieOverview;

        public MovieViewHolder(View view) {
            super(view);
            moviePoster = view.findViewById(R.id.movie_poster);
            movieName = view.findViewById(R.id.movie_name);
            movieOverview = view.findViewById(R.id.movie_overview);
        }
    }

    public MovieAdapter(Context context, List<MovieItem> movieItemList) {
        this.context = context;
        this.movieItemList = movieItemList;
        qMed = Typeface.createFromAsset(context.getAssets(), "fonts/Quicksand-Medium.ttf");
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (viewType == LIST_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_list, null);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_grid, null);
        }
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        MovieItem movieItem = movieItemList.get(position);
        Picasso.get()
                .load(movieItem.getImage())
                .into(holder.moviePoster);

        holder.movieName.setTypeface(qMed);
        holder.movieName.setText(movieItem.getName());

        holder.movieOverview.setTypeface(qMed);
        holder.movieOverview.setText(movieItem.getOverview());
    }

    @Override
    public int getItemViewType(int position) {
        if (isGrid) {
            return GRID_ITEM;
        } else {
            return LIST_ITEM;
        }
    }

    public boolean toggleViewType() {
        return !isGrid;
    }

    @Override
    public int getItemCount() {
        return movieItemList.size();
    }
}

