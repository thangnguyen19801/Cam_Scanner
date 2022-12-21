package com.example.cam_scanner.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {History.class}, version = 1, exportSchema = false)
public abstract class HistoryDatabase extends RoomDatabase {
    public abstract HistoryDao getHistoryDao();
    private static HistoryDatabase INSTANCE = null;

    public static HistoryDatabase getDatabase(final Context context) {
        if(INSTANCE != null) return INSTANCE;
        synchronized (HistoryDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            HistoryDatabase.class,
                            "history.db").build();
            }
        }
        return INSTANCE;
    }
}
