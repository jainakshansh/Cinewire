package me.akshanshjain.popularmovies;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import me.akshanshjain.popularmovies.Utils.SectionPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private SectionPagerAdapter sectionPagerAdapter;
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

        //Defining colors for the bottom navigation menu.
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

        //Handling the click listeners for the bottom navigation.
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.now_playing_menu_item:
                        viewPager.setCurrentItem(0);
                        bottomNavigationView.setItemIconTintList(nowColorList);
                        bottomNavigationView.setItemTextColor(nowColorList);
                        break;
                    case R.id.popular_menu_item:
                        viewPager.setCurrentItem(1);
                        bottomNavigationView.setItemIconTintList(popularColorList);
                        bottomNavigationView.setItemTextColor(popularColorList);
                        break;
                    case R.id.top_menu_item:
                        viewPager.setCurrentItem(2);
                        bottomNavigationView.setItemIconTintList(topColorList);
                        bottomNavigationView.setItemTextColor(topColorList);
                        break;
                    case R.id.favorites_menu_item:
                        viewPager.setCurrentItem(3);
                        bottomNavigationView.setItemIconTintList(favoriteColorList);
                        bottomNavigationView.setItemTextColor(favoriteColorList);
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
                        bottomNavigationView.setItemTextColor(nowColorList);
                        break;
                    case 1:
                        bottomNavigationView.setItemIconTintList(popularColorList);
                        bottomNavigationView.setItemTextColor(popularColorList);
                        break;
                    case 2:
                        bottomNavigationView.setItemIconTintList(topColorList);
                        bottomNavigationView.setItemTextColor(topColorList);
                        break;
                    case 3:
                        bottomNavigationView.setItemIconTintList(favoriteColorList);
                        bottomNavigationView.setItemTextColor(favoriteColorList);
                        break;
                    default:
                        bottomNavigationView.setItemIconTintList(nowColorList);
                        bottomNavigationView.setItemTextColor(nowColorList);
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
}
