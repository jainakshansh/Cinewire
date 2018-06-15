package me.akshanshjain.popularmovies.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.akshanshjain.popularmovies.Object.MovieItem;
import me.akshanshjain.popularmovies.R;
import me.akshanshjain.popularmovies.Utils.MovieAdapter;

public class TopRated extends Fragment implements MovieAdapter.RecyclerClickListener {

    private static String TOP_URL;
    private String BASE_URL;

    private RecyclerView moviesRecycler;
    private List<MovieItem> movieItemList;
    private MovieAdapter movieAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Setting the layout for the fragment
        View view = inflater.inflate(R.layout.fragment_common_layout, container, false);

        /*
            TODO Add your TheMovieDB generated API key in the strings file!
            Hover over the string id and Ctrl + Mouse click to directly navigate to string file.
        */
        //Initialising the URL parameters from the strings.xml file.
        TOP_URL = getResources().getString(R.string.topUrl);
        BASE_URL = getResources().getString(R.string.baseUrl);

        //Setting up the recycler view for the fragment.
        movieItemList = new ArrayList<>();
        moviesRecycler = view.findViewById(R.id.movies_recycler);
        movieAdapter = new MovieAdapter(getContext().getApplicationContext(), movieItemList, this);

        return view;
    }

    @Override
    public void onItemClicked(int clickedItemIndex) {
    }
}
