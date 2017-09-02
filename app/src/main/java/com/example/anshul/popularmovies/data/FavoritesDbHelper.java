package com.example.anshul.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.anshul.popularmovies.data.FavoritesContract.FavoritesEntry;

public class FavoritesDbHelper extends SQLiteOpenHelper {

    // The database name
    private static final String DATABASE_NAME = "favorites.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;

    // Constructor
    public FavoritesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a table to hold favorites data
        final String SQL_CREATE_FAVORITES_TABLE = "CREATE TABLE " + FavoritesEntry.TABLE_NAME + " (" +
                FavoritesEntry._ID                + " INTEGER PRIMARY KEY, " +
                FavoritesEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                FavoritesEntry.COLUMN_MOVIE_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                FavoritesEntry.COLUMN_COVER_IMAGE_LINK + " TEXT, " +
                FavoritesEntry.COLUMN_POSTER_IMAGE_LINK + " TEXT, " +
                FavoritesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                FavoritesEntry.COLUMN_REVIEWS + " TEXT, " +
                FavoritesEntry.COLUMN_OVERVIEW + " TEXT, " +
                FavoritesEntry.COLUMN_RATING + " TEXT NOT NULL " +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoritesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}