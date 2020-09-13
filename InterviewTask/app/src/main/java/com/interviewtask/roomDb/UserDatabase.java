package com.interviewtask.roomDb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.interviewtask.models.GetUserRe;
import com.interviewtask.utils.Constants;

@Database(entities = {GetUserRe.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {

    public abstract UserDao getUserDao();

    private static UserDatabase db;

    public static UserDatabase getInstance(Context context) {
        if (null == db) {
            db = buildDatabaseInstance(context);
        }
        return db;
    }

    private static UserDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                UserDatabase.class,
                Constants.DB_NAME)
                .allowMainThreadQueries().build();
    }

    public void cleanUp() {
        db = null;
    }
}