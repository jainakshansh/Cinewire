package me.akshanshjain.popularmovies.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.akshanshjain.popularmovies.R;

public class SimilarMovies extends Fragment {

    private RecyclerView similarRecycler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_similar_layout, container, false);

        similarRecycler = view.findViewById(R.id.similar_recycler);

        return view;
    }
}
