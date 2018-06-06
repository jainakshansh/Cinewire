package me.akshanshjain.popularmovies;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import me.akshanshjain.popularmovies.Object.MovieItem;
import me.akshanshjain.popularmovies.Utils.MovieAdapter;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private List<MovieItem> movieItemList;
    private RecyclerView moviesRecycler;
    private MovieAdapter movieAdapter;

    /*
    TODO Add your TheMovieDB generated API Key here!
     */
    private static String POPULAR_URL = "https://api.themoviedb.org/3/movie/popular?api_key=[YOUR_API_KEY]&language=en-US&page=1";
    private static String TOP_URL = "https://api.themoviedb.org/3/movie/top_rated?api_key=[YOUR_API_KEY]&language=en-US&page=1";

    private String BASE_URL = "http://image.tmdb.org/t/p/w185";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting up the toolbar for the activity.
        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
        }

        movieItemList = new ArrayList<>();
        moviesRecycler = findViewById(R.id.movies_recycler);
        movieAdapter = new MovieAdapter(this, movieItemList);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        moviesRecycler.setLayoutManager(layoutManager);
        moviesRecycler.setItemAnimator(new DefaultItemAnimator());
        moviesRecycler.setNestedScrollingEnabled(false);
        moviesRecycler.setHasFixedSize(true);
        moviesRecycler.setAdapter(movieAdapter);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest popularRequest = new JsonObjectRequest(Request.Method.GET, POPULAR_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        extractFromJSON(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
            }
        });

        JsonObjectRequest topRequest = new JsonObjectRequest(Request.Method.GET, TOP_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        extractFromJSON(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo() != null) {
            requestQueue.add(popularRequest);
            requestQueue.add(topRequest);
        } else {
            Toast.makeText(this, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toggle_view:
                boolean isGrid = movieAdapter.toggleViewType();
                item.setIcon(isGrid ? ContextCompat.getDrawable(this, R.drawable.list) : ContextCompat.getDrawable(this, R.drawable.grid));
                moviesRecycler.setLayoutManager(isGrid ? new GridLayoutManager(this, 2) : new LinearLayoutManager(this));
                break;
        }
        return super.onOptionsItemSelected(item);
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
    }

}
