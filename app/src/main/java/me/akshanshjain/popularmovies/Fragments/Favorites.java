package me.akshanshjain.popularmovies.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.akshanshjain.popularmovies.Database.MovieDatabase;
import me.akshanshjain.popularmovies.DetailActivity;
import me.akshanshjain.popularmovies.Object.MovieItem;
import me.akshanshjain.popularmovies.R;
import me.akshanshjain.popularmovies.Adapters.MovieAdapter;
import me.akshanshjain.popularmovies.Utils.MovieViewModel;

public class Favorites extends Fragment implements MovieAdapter.RecyclerClickListener {

    private List<MovieItem> movieItemList;
    private RecyclerView moviesRecycler;
    private MovieAdapter movieAdapter;

    private MovieDatabase movieDatabase;

    private static final String LIFECYCLE_CALLBACK_KEY = "callbacks";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Setting the layout for the fragment
        View view = inflater.inflate(R.layout.fragment_common_layout, container, false);

        //Initializing the database object.
        movieDatabase = MovieDatabase.getInstance(getContext().getApplicationContext());

        moviesRecycler = view.findViewById(R.id.movies_recycler);
        movieItemList = new ArrayList<>();
        movieAdapter = new MovieAdapter(getContext().getApplicationContext(), movieItemList, this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this.getActivity(), numberOfColumns());
        moviesRecycler.setLayoutManager(layoutManager);
        moviesRecycler.setItemAnimator(new DefaultItemAnimator());
        moviesRecycler.setNestedScrollingEnabled(false);
        moviesRecycler.setHasFixedSize(true);
        moviesRecycler.setAdapter(movieAdapter);

        //Reading movies from the database.
        setupViewModel();

        return view;
    }

    @Override
    public void onItemClicked(int clickedItemIndex) {
        //Getting the information about the item clicked.
        MovieItem movieItem;
        Intent detailedView = new Intent(getContext().getApplicationContext(), DetailActivity.class);
        movieItem = movieItemList.get(clickedItemIndex);

        //Passing in the data to the detailed activity.
        detailedView.putExtra("ID", movieItem.getId());
        detailedView.putExtra("NAME", movieItem.getName());
        detailedView.putExtra("IMAGE", movieItem.getImage());
        detailedView.putExtra("OVERVIEW", movieItem.getOverview());
        detailedView.putExtra("RELEASE", movieItem.getRelease_date());
        detailedView.putExtra("VOTE_AVG", movieItem.getVote_average());

        //Starting the activity with all the data passed to the next one.
        startActivity(detailedView);
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }

    private void setupViewModel() {
        //Reading the list of favorite movies from the database.
        MovieViewModel viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        viewModel.getMovieItemList().observe(this.getActivity(), new Observer<List<MovieItem>>() {
            @Override
            public void onChanged(@Nullable List<MovieItem> movieItems) {
                if (movieItemList.size() > 0) {
                    movieItemList.clear();
                }
                movieItemList.addAll(movieItems);
                movieAdapter.notifyDataSetChanged();
                Log.d("ADebug", "LiveD Calls!");
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        int currentPos = ((GridLayoutManager) moviesRecycler.getLayoutManager()).findFirstVisibleItemPosition();
        outState.putInt(LIFECYCLE_CALLBACK_KEY, currentPos);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(LIFECYCLE_CALLBACK_KEY)) {
            int visiblePos = savedInstanceState.getInt(LIFECYCLE_CALLBACK_KEY);
            if (visiblePos < 0) {
                visiblePos = 0;
            }
            moviesRecycler.smoothScrollToPosition(visiblePos);
        }
    }
}
