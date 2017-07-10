package com.example.anshul.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.anshul.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

//The movie detail activity
public class MovieDetail extends AppCompatActivity {

    private HashMap<String, String> moviesData;
    private TextView title;
    private ImageView cover_image;
    private ImageView poster_image;
    private TextView release_date;
    private TextView overview;
    private RatingBar rating_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        //Getting the intent from the previous screen
        Intent receivedIntent = getIntent();

        if(receivedIntent != null){
            if(receivedIntent.hasExtra(Intent.EXTRA_TEXT)) {
                //Getting the movie data
                moviesData = (HashMap<String, String>) receivedIntent.getSerializableExtra(Intent.EXTRA_TEXT);

                setTitle(moviesData.get("title"));

                //Setting the Title
                title = (TextView) findViewById(R.id.movie_title);
                title.setText(moviesData.get("original_title"));

                //Setting the cover image
                cover_image = (ImageView) findViewById(R.id.cover_image);
                Picasso.with(this).load(NetworkUtils.buildImageUrl(false, moviesData.get("backdrop_path"))
                        .toString()).into(cover_image);

                //Setting the background image
                poster_image = (ImageView) findViewById(R.id.poster_image);
                Picasso.with(this).load(NetworkUtils.buildImageUrl(true, moviesData.get("poster_path"))
                        .toString()).into(poster_image);

                //Set other attributes
                release_date = (TextView) findViewById(R.id.movie_release_date);
                release_date.setText(moviesData.get("release_date"));

                overview = (TextView) findViewById(R.id.movie_overview);
                String toSet = "OVERVIEW\n\n" +  moviesData.get("overview");
                overview.setText(toSet);

                rating_bar = (RatingBar) findViewById(R.id.movie_rating_bar);
                rating_bar.setRating(Float.parseFloat(moviesData.get("vote_average"))/2);
            }
        }
    }

}
