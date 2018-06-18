package me.akshanshjain.popularmovies;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.akshanshjain.popularmovies.Adapters.ReviewAdapter;
import me.akshanshjain.popularmovies.Adapters.TrailerAdapter;
import me.akshanshjain.popularmovies.Object.ReviewItem;
import me.akshanshjain.popularmovies.Object.TrailerItem;

public class DetailActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private String movieID;

    private ImageView moviePoster;
    private TextView movieName, movieOverview, movieRating, movieRelease, movieFavorite, trailerLabel, reviewLabel;
    private Typeface qBold, qMedium;

    private RecyclerView trailersRecycler;
    private List<TrailerItem> trailerItemList;
    private TrailerAdapter trailerAdapter;
    private String TRAILER_URL;

    private RecyclerView reviewsRecycler;
    private List<ReviewItem> reviewItemList;
    private ReviewAdapter reviewAdapter;
    private String REVIEW_URL;

    private RequestQueue requestQueue;
    private JsonObjectRequest trailerRequest, reviewRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Setting up the toolbar for the activity.
        toolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (intent != null) {
                getSupportActionBar().setTitle(intent.getExtras().getString("NAME"));
            }
        }

        //Getting the passed data from the intent.
        movieID = intent.getStringExtra("ID");
        String name = intent.getStringExtra("NAME");
        String image = intent.getStringExtra("IMAGE");
        String overview = intent.getStringExtra("OVERVIEW");
        String release = intent.getStringExtra("RELEASE");
        String rating = intent.getStringExtra("VOTE_AVG");

        //Initializing the views from the XML.
        initViews();

        //Binding the received data to the views.
        image = image.replace("w185", "w500");
        Picasso.get()
                .load(image)
                .into(moviePoster);

        movieName.setTypeface(qBold);
        movieName.setText(name);

        movieOverview.setTypeface(qMedium);
        movieOverview.setText(overview);

        movieRating.setTypeface(qBold);
        movieRating.setText(rating);

        movieRelease.setTypeface(qMedium);
        movieRelease.setText(release);

        movieFavorite.setTypeface(qMedium);

        //Setting up the trailers recycler view.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        trailersRecycler.setLayoutManager(layoutManager);
        trailersRecycler.setItemAnimator(new DefaultItemAnimator());
        trailersRecycler.setHasFixedSize(true);
        trailersRecycler.setNestedScrollingEnabled(false);
        trailersRecycler.setAdapter(trailerAdapter);

        //Setting up the reviews recycler view.
        RecyclerView.LayoutManager reviewLm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        reviewsRecycler.setLayoutManager(reviewLm);
        reviewsRecycler.setItemAnimator(new DefaultItemAnimator());
        reviewsRecycler.setHasFixedSize(true);
        reviewsRecycler.setNestedScrollingEnabled(false);
        reviewsRecycler.setAdapter(reviewAdapter);

        //Centering the layout by using the inbuilt snap helper class.
        SnapHelper reviewSnapper = new LinearSnapHelper();
        reviewSnapper.attachToRecyclerView(reviewsRecycler);

        SnapHelper trailerSnapper = new LinearSnapHelper();
        trailerSnapper.attachToRecyclerView(trailersRecycler);

        connectedState();
    }

    private void connectedState() {
        if (isConnected()) {
            networkCalls();
        } else {
            connectedState();
        }
    }

    private void initViews() {
        moviePoster = findViewById(R.id.movie_poster_detail);
        movieName = findViewById(R.id.movie_name_detail);
        movieOverview = findViewById(R.id.movie_overview_detail);
        movieRating = findViewById(R.id.movie_rating_detail);
        movieRelease = findViewById(R.id.movie_release_detail);
        movieFavorite = findViewById(R.id.movie_favorite_detail);

        qBold = Typeface.createFromAsset(getAssets(), "fonts/Quicksand-Bold.ttf");
        qMedium = Typeface.createFromAsset(getAssets(), "fonts/Quicksand-Medium.ttf");

        //Initialising all the variables with respect to trailer components.
        trailerLabel = findViewById(R.id.trailer_label_detail);
        trailerLabel.setTypeface(qBold);

        trailerItemList = new ArrayList<>();
        trailersRecycler = findViewById(R.id.trailers_recycler);
        trailerAdapter = new TrailerAdapter(getApplicationContext(), trailerItemList);
        TRAILER_URL = getApplicationContext().getResources().getString(R.string.trailerUrl);
        TRAILER_URL = TRAILER_URL.replace("{movieID}", movieID);

        //Initialising all the variables with respect to review components.
        reviewLabel = findViewById(R.id.review_label_detail);
        reviewLabel.setTypeface(qBold);

        reviewItemList = new ArrayList<>();
        reviewsRecycler = findViewById(R.id.reviews_recycler);
        reviewAdapter = new ReviewAdapter(this, reviewItemList);

        REVIEW_URL = getApplicationContext().getResources().getString(R.string.reviewUrl);
        REVIEW_URL = REVIEW_URL.replace("{movieID}", movieID);
    }

    private void extractFromJSON(JSONObject baseJsonResponse) {
        try {
            JSONArray results = baseJsonResponse.getJSONArray("results");
            for (int c = 0; c < results.length(); c++) {
                JSONObject trailer = results.getJSONObject(c);

                String name = trailer.getString("name");
                String key = trailer.getString("key");

                trailerItemList.add(new TrailerItem(name, key));
            }
            trailerAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void extractFromJsonReviews(JSONObject baseJsonResponse) {
        try {
            JSONArray results = baseJsonResponse.getJSONArray("results");
            for (int c = 0; c < results.length(); c++) {
                JSONObject review = results.getJSONObject(c);

                String author = review.getString("author");
                String content = review.getString("content");

                reviewItemList.add(new ReviewItem(author, content));
            }
            reviewAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void networkCalls() {
        //Setting up the network request for trailers.
        requestQueue = Volley.newRequestQueue(this);
        trailerRequest = new JsonObjectRequest(Request.Method.GET, TRAILER_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        extractFromJSON(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailActivity.this, getResources().getString(R.string.check_network_connection), Toast.LENGTH_SHORT).show();
            }
        });

        //Setting up the network request for reviews.
        reviewRequest = new JsonObjectRequest(Request.Method.GET, REVIEW_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        extractFromJsonReviews(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailActivity.this, getResources().getString(R.string.check_network_connection), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(trailerRequest);
        //trailerAdapter.notifyDataSetChanged();

        requestQueue.add(reviewRequest);
        //reviewAdapter.notifyDataSetChanged();
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
