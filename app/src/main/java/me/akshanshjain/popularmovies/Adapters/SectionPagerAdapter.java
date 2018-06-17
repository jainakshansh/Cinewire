package me.akshanshjain.popularmovies.Adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import me.akshanshjain.popularmovies.Fragments.Favorites;
import me.akshanshjain.popularmovies.Fragments.NowPlaying;
import me.akshanshjain.popularmovies.Fragments.Popular;
import me.akshanshjain.popularmovies.Fragments.TopRated;

public class SectionPagerAdapter extends FragmentPagerAdapter {

    public SectionPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new NowPlaying();
            case 1:
                return new Popular();
            case 2:
                return new TopRated();
            case 3:
                return new Favorites();
            default:
                return new NowPlaying();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Now Playing";
            case 1:
                return "Popular";
            case 2:
                return "Top Rated";
            case 3:
                return "Favourites";
        }
        return null;
    }
}
