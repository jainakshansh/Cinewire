package me.akshanshjain.popularmovies.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.akshanshjain.popularmovies.R;

public class Popular extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Setting the layout for the fragment
        View view = inflater.inflate(R.layout.fragment_common_layout, container, false);

        return view;
    }
}
