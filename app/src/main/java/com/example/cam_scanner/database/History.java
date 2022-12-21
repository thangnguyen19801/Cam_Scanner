package com.example.cam_scanner.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "History")
public class History {
    @PrimaryKey(autoGenerate = true)
    private int id = 0;

    @ColumnInfo(name = "file_path")
    private String file_path;

    @ColumnInfo(name = "date")
    private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public History(String file_path, String date) {
        this.file_path = file_path;
        this.date = date;
    }
}
