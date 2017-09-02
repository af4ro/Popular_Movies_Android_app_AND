package com.example.anshul.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.anshul.popularmovies.data.FavoritesContract;
import com.example.anshul.popularmovies.data.FavoritesDbHelper;
import com.example.anshul.popularmovies.utilities.JsonUtils;
import com.example.anshul.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MovieAdapterOnClickHandler{

    // TODO add LifeCycle fixes
    // TODO add Db for favourites

    // Constants for logging and referring to a unique loader
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int TASK_LOADER_ID = 0;

    //Enter your API KEY here
    public static String API_KEY = "91a5d474b1e7b77bdc20b86894016717";

    //The necessary class variable go here
    private RecyclerView mRecyclerView;

    private MoviesAdapter mMoviesAdapter;

    private static String sortBy = "/movie/popular";

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;

    private static SQLiteDatabase mDb;

    private static ArrayList<String> favoriteContains = new ArrayList<String>();

    //The method executed on creation of this activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        //Getting the gridLayout for the layoutManager
        GridLayoutManager layoutManager
                = new GridLayoutManager(getApplicationContext(),2);

        //Setting up the Database for favorites
        FavoritesDbHelper dbHelper = new FavoritesDbHelper(this);

        mDb = dbHelper.getWritableDatabase();

        //Setting the adapter for the RecyclerView
        mRecyclerView.setLayoutManager(layoutManager);
        mMoviesAdapter = new MoviesAdapter(MainActivity.this, this);
        mRecyclerView.setAdapter(mMoviesAdapter);

        //Progress bar for before the movies are displayed
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        //Execute the popular movies call
        loadMoviesData();
    }

    //Executes the process of getting and setting the Movie posters
    private void loadMoviesData(){
        showMovieData();
        new FetchMovieData().execute(sortBy, API_KEY);
    }

    //Hides the error message and shows the Recycler View
    private void showMovieData(){
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
    }

    //Hides the RecyclerView and Displays the error message
    private void showErrorMessage(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    //Handles the click for the poster and starts another Activity
    @Override
    public void onCLick(Map<String, String> currentMovie) {
        Intent startMovieDetail = new Intent(this, MovieDetail.class);
        startMovieDetail.putExtra(Intent.EXTRA_TEXT,(HashMap)currentMovie);
        startActivity(startMovieDetail);
    }

    //The class for performing the asynchronous task of getting back information from the API
    private class FetchMovieData extends AsyncTask<String, Void, List<Map<String,String>>>{

        //Load indicator perExecute
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        //The async process of getting the information
        @Override
        protected List<Map<String,String>> doInBackground(String... strings) {

            if (strings.length == 0) {
                return null;
            }

            if (strings[0].equals("favorites") && strings[1] == null){

            }
            //Strings[0]: to sort by
            //Strings[1]: the api key
            URL movieRequestedUrl = NetworkUtils.buildUrl(strings[0],strings[1]);

            try {
                String jsonMoviesResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestedUrl);

                return JsonUtils
                        .stringFromJson(jsonMoviesResponse, API_KEY);
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        //Executed after information is retrieved
        @Override
        protected void onPostExecute(List<Map<String, String>> mapList) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if(mapList == null || mapList.size() == 0)
            {
                showErrorMessage();
            }
            else
            {
                showMovieData();
                mMoviesAdapter.setmMovieData(mapList);
            }
        }
    }

    //The menu options for the sorting preference
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_menu, menu);
        return true;
    }

    //To change sorting options
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_popular) {
            sortBy = "/movie/popular";
            loadMoviesData();
            return true;
        }

        if (id == R.id.action_most_rated) {
            sortBy = "/movie/top_rated";
            loadMoviesData();
            return true;
        }

        if(id == R.id.action_favorites) {

        }

        return super.onOptionsItemSelected(item);
    }

    //To disable the current sort option
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem popularItem = menu.findItem(R.id.action_popular);
        MenuItem ratedItem = menu.findItem(R.id.action_most_rated);

        if (sortBy.equals("/movie/top_rated")) {
            popularItem.setEnabled(true);
            ratedItem.setEnabled(false);
        }
        if(sortBy.equals("/movie/popular")) {
            popularItem.setEnabled(false);
            ratedItem.setEnabled(true);
        }
        return true;
    }


    // Make List of favorites
//    private List<Map<String, String>> favoritesToList(){
//
//    }

    // Database functions
    public static boolean containsFavorites(String movieTitle){
        return favoriteContains.contains(movieTitle);
    }

    public static boolean addToFavorites(String movieTitle){
        if (containsFavorites(movieTitle))
            return false;
        Log.v("Favorites", favoriteContains.toString());
        return favoriteContains.add(movieTitle);
    }

    public static boolean removeFromFavorites(String movieTitle){
        return favoriteContains.remove(movieTitle);
    }
}

