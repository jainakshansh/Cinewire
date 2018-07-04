package me.akshanshjain.popularmovies.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.akshanshjain.popularmovies.DetailActivity;
import me.akshanshjain.popularmovies.Object.MovieItem;
import me.akshanshjain.popularmovies.R;
import me.akshanshjain.popularmovies.Adapters.MovieAdapter;

public class Popular extends Fragment implements MovieAdapter.RecyclerClickListener {

    private static String POPULAR_URL;
    private String BASE_URL;

    private RecyclerView moviesRecycler;
    private List<MovieItem> movieItemList;
    private MovieAdapter movieAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectRequest;

    private static final String LIFECYCLE_CALLBACK_KEY = "callbacks";
    private Bundle state;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Setting the common layout for the fragment.
        View view = inflater.inflate(R.layout.fragment_common_layout, container, false);

        /*
            TODO Add your TheMovieDB generated API key in the strings file!
            Hover over the string id and Ctrl + Mouse click to directly navigate to string file.
        */
        //Initialising the URL parameter from the strings.xml file.
        POPULAR_URL = getResources().getString(R.string.popularUrl);
        BASE_URL = getResources().getString(R.string.baseUrl);

        //Setting up the recycler view for the fragment.
        movieItemList = new ArrayList<>();
        moviesRecycler = view.findViewById(R.id.movies_recycler);
        movieAdapter = new MovieAdapter(this.getContext().getApplicationContext(), movieItemList, this);

        layoutManager = new GridLayoutManager(this.getActivity(), numberOfColumns());
        moviesRecycler.setLayoutManager(layoutManager);
        moviesRecycler.setItemAnimator(new DefaultItemAnimator());
        moviesRecycler.setNestedScrollingEnabled(false);
        moviesRecycler.setHasFixedSize(true);
        moviesRecycler.setAdapter(movieAdapter);

        //Checking if there is network connection and making requests if connected.
        if (isConnected()) {
            networkCalls();
            state = savedInstanceState;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (state != null && state.containsKey(LIFECYCLE_CALLBACK_KEY)) {
                        int visiblePos = state.getInt(LIFECYCLE_CALLBACK_KEY);
                        if (visiblePos < 0) {
                            visiblePos = 0;
                        }
                        moviesRecycler.smoothScrollToPosition(visiblePos);
                    }
                }
            }, 500);
        } else {
            Toast.makeText(getContext(), getResources().getString(R.string.check_network_connection), Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    @Override
    public void onItemClicked(int clickedItemIndex) {
        //Getting the information about the item clicked.
        MovieItem movieItem;
        Intent detailedView = new Intent(getContext().getApplicationContext(), DetailActivity.class);
        movieItem = movieItemList.get(clickedItemIndex);

        Bundle bundle = new Bundle();
        //Passing in the data to the detailed activity.
        bundle.putString("ID", movieItem.getId());
        bundle.putString("NAME", movieItem.getName());
        bundle.putString("IMAGE", movieItem.getImage());
        bundle.putString("OVERVIEW", movieItem.getOverview());
        bundle.putString("RELEASE", movieItem.getRelease_date());
        bundle.putString("VOTE_AVG", movieItem.getVote_average());

        detailedView.putExtras(bundle);

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

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null;
    }

    private void extractFromJSON(JSONObject baseJSONResponse) {
        try {
            JSONArray results = baseJSONResponse.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject movieObject = results.getJSONObject(i);

                String id = movieObject.getString("id");
                String vote_average = movieObject.getString("vote_average");
                String poster_path = movieObject.getString("poster_path");
                String name = movieObject.getString("original_title");
                String overview = movieObject.getString("overview");
                String release_date = movieObject.getString("release_date");
                String image = BASE_URL + poster_path;

                movieItemList.add(new MovieItem(id, name, image, overview, vote_average, release_date));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        movieAdapter.notifyDataSetChanged();
    }

    private void networkCalls() {
        requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());
        if (isConnected()) {
            movieItemList.clear();
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, POPULAR_URL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            extractFromJSON(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext().getApplicationContext(), getResources().getString(R.string.check_network_connection), Toast.LENGTH_SHORT).show();
                }
            });
        }

        requestQueue.add(jsonObjectRequest);
        movieAdapter.notifyDataSetChanged();
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
    }
}
