package com.example.cam_scanner.database;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class HistoryDatabaseTest {
    private HistoryDao historyDao;
    private HistoryDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, HistoryDatabase.class).build();
        historyDao = db.getHistoryDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void writeHistoryAndReadHistory() {
        History history = new History("path", "today");
        historyDao.addHistory(history);
        List<History> histories = historyDao.getAllHistory();
        assertEquals(histories.get(0).getFile_path(), history.getFile_path());
        assertEquals(histories.get(0).getDate(), history.getDate());
    }
}