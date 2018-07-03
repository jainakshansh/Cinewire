package me.akshanshjain.popularmovies.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import me.akshanshjain.popularmovies.Object.MovieItem;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movies")
    LiveData<List<MovieItem>> loadAllMovies();

    @Insert
    void insertMovie(MovieItem movieItem);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(MovieItem movieItem);

    @Delete
    void deleteMovie(MovieItem movieItem);

    @Query("SELECT * FROM movies WHERE id = :movieID")
    MovieItem loadMovieById(String movieID);
}
