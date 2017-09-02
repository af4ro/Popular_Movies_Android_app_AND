package com.example.anshul.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoritesContract {

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.example.anshul.popularmovies";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "tasks" directory
    public static final String PATH_FAVS = "favorites";

    /* TaskEntry is an inner class that defines the contents of the task table */
    public static final class FavoritesEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVS).build();

        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_MOVIE_TITLE = "movieTitle";
        public static final String COLUMN_MOVIE_ORIGINAL_TITLE = "movieOriginalTitle";
        public static final String COLUMN_COVER_IMAGE_LINK = "coverImage";
        public static final String COLUMN_POSTER_IMAGE_LINK = "posterImage";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_REVIEWS = "reviews";
    }
}