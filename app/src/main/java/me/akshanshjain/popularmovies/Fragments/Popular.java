package me.akshanshjain.popularmovies.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import me.akshanshjain.popularmovies.MainActivity;
import me.akshanshjain.popularmovies.Object.MovieItem;
import me.akshanshjain.popularmovies.R;
import me.akshanshjain.popularmovies.Utils.MovieAdapter;

public class Popular extends Fragment implements MovieAdapter.RecyclerClickListener {

    private static String POPULAR_URL;
    private String BASE_URL;

    private RecyclerView moviesRecycler;
    private List<MovieItem> movieItemList;
    private MovieAdapter movieAdapter;

    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectRequest;

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
        movieAdapter = new MovieAdapter(getContext().getApplicationContext(), movieItemList, this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext().getApplicationContext(), numberOfColumns());
        moviesRecycler.setLayoutManager(layoutManager);
        moviesRecycler.setItemAnimator(new DefaultItemAnimator());
        moviesRecycler.setNestedScrollingEnabled(false);
        moviesRecycler.setHasFixedSize(true);
        moviesRecycler.setAdapter(movieAdapter);

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
                }
            });
        }

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
        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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
}
