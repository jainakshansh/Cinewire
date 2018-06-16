package me.akshanshjain.popularmovies;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import me.akshanshjain.popularmovies.Utils.SectionPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private SectionPagerAdapter sectionPagerAdapter;
    private int choice;
    private MenuItem previousMenuItem;

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

        //Adding the page scroll listener.
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        switch (item.getItemId()) {
            case R.id.toggle_view:
                boolean isGrid = movieAdapter.toggleViewType();
                item.setIcon(isGrid ? ContextCompat.getDrawable(this, R.drawable.list) : ContextCompat.getDrawable(this, R.drawable.grid));
                moviesRecycler.setLayoutManager(isGrid ? new GridLayoutManager(this, numberOfColumns()) : new LinearLayoutManager(this));
                break;
        }
        movieAdapter.notifyDataSetChanged();
        return super.onOptionsItemSelected(item);
        */
        return false;
    }
}
