package me.akshanshjain.popularmovies;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

import me.akshanshjain.popularmovies.Object.MovieItem;
import me.akshanshjain.popularmovies.Utils.MovieAdapter;
import me.akshanshjain.popularmovies.Utils.SectionPagerAdapter;

public class MainActivity extends AppCompatActivity implements MovieAdapter.RecyclerClickListener {

    private Toolbar toolbar;

    private List<MovieItem> movieItemList;
    private RecyclerView moviesRecycler;
    private MovieAdapter movieAdapter;

    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;
    private ConstraintLayout constraintLayout;

    private RequestQueue requestQueue;
    private JsonObjectRequest popularRequest, topRequest;

    private String BASE_URL;

    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private SectionPagerAdapter sectionPagerAdapter;
    private int choice;
    private MenuItem previousMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //States for the bottom navigation menu
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_checked},
                new int[]{-android.R.attr.state_enabled},
                new int[]{-android.R.attr.state_checked},
                new int[]{android.R.attr.state_pressed},
                new int[]{android.R.attr.state_window_focused}
        };

        int[] nowColors = new int[]{
                ContextCompat.getColor(this, R.color.primary_now_playing),
                ContextCompat.getColor(this, R.color.materialBlack),
                ContextCompat.getColor(this, R.color.materialBlack),
                ContextCompat.getColor(this, R.color.materialBlack),
                ContextCompat.getColor(this, R.color.primary_now_playing)
        };

        int[] popularColors = new int[]{
                ContextCompat.getColor(this, R.color.primary_popular),
                ContextCompat.getColor(this, R.color.materialBlack),
                ContextCompat.getColor(this, R.color.materialBlack),
                ContextCompat.getColor(this, R.color.materialBlack),
                ContextCompat.getColor(this, R.color.primary_popular)
        };

        int[] topColors = new int[]{
                ContextCompat.getColor(this, R.color.primary_top),
                ContextCompat.getColor(this, R.color.materialBlack),
                ContextCompat.getColor(this, R.color.materialBlack),
                ContextCompat.getColor(this, R.color.materialBlack),
                ContextCompat.getColor(this, R.color.primary_top)
        };

        int[] favoriteColors = new int[]{
                ContextCompat.getColor(this, R.color.primary_favorites),
                ContextCompat.getColor(this, R.color.materialBlack),
                ContextCompat.getColor(this, R.color.materialBlack),
                ContextCompat.getColor(this, R.color.materialBlack),
                ContextCompat.getColor(this, R.color.primary_favorites)
        };

        final ColorStateList nowColorList = new ColorStateList(states, nowColors);
        final ColorStateList popularColorList = new ColorStateList(states, popularColors);
        final ColorStateList topColorList = new ColorStateList(states, topColors);
        final ColorStateList favoriteColorList = new ColorStateList(states, favoriteColors);

        //Initialising the major containers
        bottomNavigationView = findViewById(R.id.bottom_navigation_main);
        bottomNavigationView.setItemIconTintList(nowColorList);

        viewPager = findViewById(R.id.view_pager_main);
        sectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(sectionPagerAdapter);
        choice = 0;

        switch (choice) {
            case 0:
                viewPager.setCurrentItem(0);
                bottomNavigationView.setItemIconTintList(nowColorList);
                bottomNavigationView.getMenu().getItem(0).setChecked(false);
                bottomNavigationView.getMenu().getItem(0).setChecked(true);
                break;
            case 1:
                viewPager.setCurrentItem(1);
                bottomNavigationView.setItemIconTintList(nowColorList);
                bottomNavigationView.getMenu().getItem(0).setChecked(false);
                bottomNavigationView.getMenu().getItem(1).setChecked(true);
                break;
            case 2:
                viewPager.setCurrentItem(2);
                bottomNavigationView.setItemIconTintList(nowColorList);
                bottomNavigationView.getMenu().getItem(0).setChecked(false);
                bottomNavigationView.getMenu().getItem(2).setChecked(true);
                break;
            case 3:
                viewPager.setCurrentItem(3);
                bottomNavigationView.setItemIconTintList(nowColorList);
                bottomNavigationView.getMenu().getItem(0).setChecked(false);
                bottomNavigationView.getMenu().getItem(3).setChecked(true);
                break;
        }

        //Handling the click listeners for the bottom navigation.
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.now_playing_menu_item:
                        viewPager.setCurrentItem(0);
                        bottomNavigationView.setItemIconTintList(nowColorList);
                        break;
                    case R.id.popular_menu_item:
                        viewPager.setCurrentItem(1);
                        bottomNavigationView.setItemIconTintList(popularColorList);
                        break;
                    case R.id.top_menu_item:
                        viewPager.setCurrentItem(2);
                        bottomNavigationView.setItemIconTintList(topColorList);
                        break;
                    case R.id.favorites_menu_item:
                        viewPager.setCurrentItem(3);
                        bottomNavigationView.setItemIconTintList(favoriteColorList);
                        break;
                }
                return false;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.setItemIconTintList(nowColorList);
                        break;
                    case 1:
                        bottomNavigationView.setItemIconTintList(popularColorList);
                        break;
                    case 2:
                        bottomNavigationView.setItemIconTintList(topColorList);
                        break;
                    case 3:
                        bottomNavigationView.setItemIconTintList(favoriteColorList);
                        break;
                    default:
                        bottomNavigationView.setItemIconTintList(nowColorList);
                        break;
                }

                if (previousMenuItem != null) {
                    previousMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }

                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                previousMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        //Setting up the toolbar for the activity.
        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
        }

        movieItemList = new ArrayList<>();
        moviesRecycler = findViewById(R.id.movies_recycler);
        movieAdapter = new MovieAdapter(this, movieItemList, this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns());
        moviesRecycler.setLayoutManager(layoutManager);
        moviesRecycler.setItemAnimator(new DefaultItemAnimator());
        moviesRecycler.setNestedScrollingEnabled(false);
        moviesRecycler.setHasFixedSize(true);
        moviesRecycler.setAdapter(movieAdapter);

        //Initializing and referencing the Parent View for the activity.
        constraintLayout = findViewById(R.id.parent_main);

        requestQueue = Volley.newRequestQueue(this);

        if (isConnected()) {
            //popularNetworkReq();
        } else {
            //popularNetworkReq();
            callSnackbar(requestQueue, popularRequest);
        }
        movieAdapter.notifyDataSetChanged();
    }

    /*
    private void topNetworkReq() {
        movieItemList.clear();
        topRequest = new JsonObjectRequest(Request.Method.GET, TOP_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        extractFromJSON(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.check_network_connection), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(topRequest);
        movieAdapter.notifyDataSetChanged();
    }
    */

    /*
    private void popularNetworkReq() {
        movieItemList.clear();
        popularRequest = new JsonObjectRequest(Request.Method.GET, POPULAR_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        extractFromJSON(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.check_network_connection), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(popularRequest);
        movieAdapter.notifyDataSetChanged();
    }
    */

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
                moviesRecycler.setLayoutManager(isGrid ? new GridLayoutManager(this, numberOfColumns()) : new LinearLayoutManager(this));
                break;
            case R.id.popular_menu_item:
                if (isConnected()) {
                    //popularNetworkReq();
                } else {
                    //popularNetworkReq();
                    callSnackbar(requestQueue, popularRequest);
                }
                break;
            case R.id.top_menu_item:
                if (isConnected()) {
                    //topNetworkReq();
                } else {
                    //topNetworkReq();
                    callSnackbar(requestQueue, topRequest);
                }
                break;
        }
        movieAdapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
    }

    private void extractFromJSON(JSONObject baseJSONResponse) {
        movieItemList.clear();
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

    @Override
    public void onItemClicked(int clickedItemIndex) {
        //Getting the information about the item clicked.
        MovieItem movieItem;
        Intent detailedView = new Intent(getApplicationContext(), DetailActivity.class);
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
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private boolean isConnected() {
        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null;
    }

    private void callSnackbar(final RequestQueue requestQueue, final JsonObjectRequest jsonObjectRequest) {
        Snackbar snackbar = Snackbar.make(constraintLayout, getResources().getString(R.string.connection_timed_out), Snackbar.LENGTH_INDEFINITE)
                .setAction("RELOAD", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isConnected()) {
                            requestQueue.add(jsonObjectRequest);
                            movieAdapter.notifyDataSetChanged();
                        } else {
                            callSnackbar(requestQueue, jsonObjectRequest);
                        }
                    }
                });
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        View snackbarView = snackbar.getView();
        TextView snackText = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        snackText.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        snackbar.show();
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }
}
