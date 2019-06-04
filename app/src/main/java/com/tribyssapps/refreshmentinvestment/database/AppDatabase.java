package com.tribyssapps.refreshmentinvestment.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {InvestmentEntity.class},version =2)
public abstract class AppDatabase extends RoomDatabase {

    public abstract InvestmentDAO getInvestmentDAO();


    private static AppDatabase appDatabase;


    public static AppDatabase getLocalDataBase(Context context) {
        if (appDatabase == null) {
            synchronized (AppDatabase.class) {
                appDatabase = Room.databaseBuilder(context,
                        AppDatabase.class, "local_database").build();
            }
        }
        return appDatabase;
    }


}
