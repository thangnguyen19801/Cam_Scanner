package com.example.cam_scanner.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {History.class}, version = 1, exportSchema = false)
public abstract class HistoryDatabase extends RoomDatabase {

    public abstract HistoryDao getHistoryDao();

    private static volatile HistoryDatabase INSTANCE = null;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(2);

    public static HistoryDatabase getDatabase(final Context context) {
        if(INSTANCE != null) return INSTANCE;
        synchronized (HistoryDatabase.class) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        HistoryDatabase.class,
                        "history.db").build();
            }
            return INSTANCE;
        }
    }
}
