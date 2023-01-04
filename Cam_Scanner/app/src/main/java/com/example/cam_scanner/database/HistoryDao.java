package com.example.cam_scanner.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addHistory(History... histories);

    @Query("SELECT * FROM History ORDER BY id DESC ")
    List<History> getAllHistory();

    @Query("DELETE FROM History")
    void deleteHistory();

    @Query("DELETE FROM History WHERE id = :id")
    void deleteHistoryById(int id);

    @Query("SELECT * FROM History WHERE id = :id")
    List<History> getHistoryById(int id);


}
