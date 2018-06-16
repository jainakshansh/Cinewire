package me.akshanshjain.popularmovies.Database;

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
    List<MovieItem> loadAllMovies();

    @Insert
    void insertTask(MovieItem movieItem);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(MovieItem movieItem);

    @Delete
    void deleteTask(MovieItem movieItem);
}
