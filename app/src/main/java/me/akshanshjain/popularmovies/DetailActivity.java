package me.akshanshjain.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private ImageView moviePoster;
    private TextView movieName, movieOverview, movieRating, movieRelease;

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
                getSupportActionBar().setTitle(intent.getStringExtra("NAME"));
            }
        }

        //Getting the passed data from the intent.
        String id = intent.getStringExtra("ID");
        String name = intent.getStringExtra("NAME");
        String rating = intent.getStringExtra("VOTE_AVG");
        String image = intent.getStringExtra("IMAGE");
        String overview = intent.getStringExtra("OVERVIEW");

        //Initializing the views from the XML.
        initViews();
    }

    private void initViews() {
        moviePoster = findViewById(R.id.movie_poster_detail);
        movieName = findViewById(R.id.movie_name_detail);
        movieOverview = findViewById(R.id.movie_overview_detail);
        movieRating = findViewById(R.id.movie_rating_detail);
        movieRelease = findViewById(R.id.movie_release_detail);
    }
}
