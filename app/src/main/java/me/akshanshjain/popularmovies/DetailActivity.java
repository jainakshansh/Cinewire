package me.akshanshjain.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {

    private Toolbar toolbar;

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

        String id = intent.getStringExtra("ID");
        String name = intent.getStringExtra("NAME");
        String rating = intent.getStringExtra("VOTE_AVG");
        String image = intent.getStringExtra("IMAGE");

        Toast.makeText(this, id + "\t" + name + "\t" + rating + "\t" + image, Toast.LENGTH_LONG).show();
    }
}
