package me.akshanshjain.popularmovies.Utils;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import me.akshanshjain.popularmovies.Database.MovieDatabase;
import me.akshanshjain.popularmovies.Object.MovieItem;

public class MovieViewModel extends AndroidViewModel {

    private LiveData<List<MovieItem>> movieItemList;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        MovieDatabase movieDatabase = MovieDatabase.getInstance(this.getApplication());
        movieItemList = movieDatabase.movieDao().loadAllMovies();
    }

    public LiveData<List<MovieItem>> getMovieItemList() {
        return movieItemList;
    }
}
