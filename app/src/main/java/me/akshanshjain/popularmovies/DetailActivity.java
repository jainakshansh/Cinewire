package me.akshanshjain.popularmovies;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import me.akshanshjain.popularmovies.Object.TrailerItem;

public class DetailActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private ImageView moviePoster;
    private TextView movieName, movieOverview, movieRating, movieRelease, movieFavorite;
    private Typeface qBold, qMedium;

    private RecyclerView trailersRecycler;
    private List<TrailerItem> trailerItemList;

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
        String id = intent.getExtras().getString("ID");
        String name = intent.getExtras().getString("NAME");
        String image = intent.getExtras().getString("IMAGE");
        String overview = intent.getExtras().getString("OVERVIEW");
        String release = intent.getExtras().getString("RELEASE");
        String rating = intent.getExtras().getString("VOTE_AVG");

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
    }

    private void initViews() {
        moviePoster = findViewById(R.id.movie_poster_detail);
        movieName = findViewById(R.id.movie_name_detail);
        movieOverview = findViewById(R.id.movie_overview_detail);
        movieRating = findViewById(R.id.movie_rating_detail);
        movieRelease = findViewById(R.id.movie_release_detail);

        qBold = Typeface.createFromAsset(getAssets(), "fonts/Quicksand-Bold.ttf");
        qMedium = Typeface.createFromAsset(getAssets(), "fonts/Quicksand-Medium.ttf");

        trailerItemList = new ArrayList<>();
        trailersRecycler = findViewById(R.id.trailers_recycler);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
