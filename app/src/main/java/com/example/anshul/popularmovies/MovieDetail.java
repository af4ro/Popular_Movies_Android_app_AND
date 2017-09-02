package com.example.anshul.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anshul.popularmovies.data.FavoritesContract;
import com.example.anshul.popularmovies.utilities.JsonUtils;
import com.example.anshul.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//The movie detail activity
public class MovieDetail extends AppCompatActivity{

    private HashMap<String, String> moviesData;
    private TextView title;
    private ImageView cover_image;
    private ImageView poster_image;
    private TextView release_date;
    private TextView overview;
    private RatingBar rating_bar;
    private Button favorite_button;
    private boolean in_favorite;
    private FrameLayout fl_videos;
    private FrameLayout fl_reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        //Getting the intent from the previous screen
        final Intent receivedIntent = getIntent();

        if(receivedIntent != null){
            if(receivedIntent.hasExtra(Intent.EXTRA_TEXT)){
                try {
                    //Getting the movie data
                    moviesData = (HashMap<String, String>) receivedIntent.getSerializableExtra(Intent.EXTRA_TEXT);

                    final String movieTitle = moviesData.get("title");
                    setTitle(moviesData.get("original_title"));

                    //Setting the Title
                    title = (TextView) findViewById(R.id.movie_title);
                    title.setText(movieTitle);

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

                    //Set favorite button TODO check the boolean state and change accordingly
                    favorite_button = (Button) findViewById(R.id.favorite_button);

                    in_favorite = MainActivity.containsFavorites(movieTitle);
                    if (in_favorite)
                        favorite_button.setText(R.string.remove_from_favorite);
                    else
                        favorite_button.setText(R.string.add_to_favorite);

                    favorite_button.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                            if(in_favorite) {
                                removeFromFavorite();
                                favorite_button.setText(R.string.add_to_favorite);
                                in_favorite = false;
                            }
                            else {
                                addToFavorite();
                                favorite_button.setText(R.string.remove_from_favorite);
                                in_favorite = true;
                            }
                        }
                    });

                    overview = (TextView) findViewById(R.id.movie_overview);
                    String toSet = "OVERVIEW\n\n" + moviesData.get("overview");
                    overview.setText(toSet);

                    rating_bar = (RatingBar) findViewById(R.id.movie_rating_bar);
                    rating_bar.setRating(Float.parseFloat(moviesData.get("vote_average")) / 2);

                    // TODO add reviews to details
//                    fillReviews(moviesData.get("reviews"), R.id.sv_reviews);

                    // TODO add videos to details

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void fillReviews(String reviewJSONString, int parentLayoutId) throws JSONException{
        ScrollView svReviews = (ScrollView) findViewById(parentLayoutId);
        List<Map<String, String>> reviews = JsonUtils.returnReviews(reviewJSONString);

        LayoutInflater inflater = LayoutInflater.from(svReviews.getContext());
        for(int i = 0; i< reviews.size(); i++){

            View viewReview = inflater.inflate(R.layout.review_list_item, svReviews, false);
            TextView name = viewReview.findViewById(R.id.tv_author_name);
            TextView content = viewReview.findViewById(R.id.tv_review_content);

//            name.setText(reviews.get(i).get("author"));
//            content.setText(reviews.get(i).get("content"));

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            Log.d("ReviewName", reviews.get(i).get("author"));
            svReviews.addView(viewReview, params);
        }
    }

    public void addToFavorite() {

        Log.v("Favorites", "add check");

        if(!MainActivity.containsFavorites(moviesData.get("title"))) {
            ContentValues cv = new ContentValues();
            cv.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_TITLE, String.format("'%s'",moviesData.get("title")));
            cv.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ORIGINAL_TITLE, String.format("'%s'",moviesData.get("original_title")));
            cv.put(FavoritesContract.FavoritesEntry.COLUMN_COVER_IMAGE_LINK, moviesData.get("backdrop_path"));
            cv.put(FavoritesContract.FavoritesEntry.COLUMN_POSTER_IMAGE_LINK, moviesData.get("poster_path"));
            cv.put(FavoritesContract.FavoritesEntry.COLUMN_RELEASE_DATE, moviesData.get("release_date"));
            cv.put(FavoritesContract.FavoritesEntry.COLUMN_REVIEWS, String.format("'%s'",moviesData.get("reviews")));
            cv.put(FavoritesContract.FavoritesEntry.COLUMN_OVERVIEW, String.format("'%s'",moviesData.get("overview")));
            cv.put(FavoritesContract.FavoritesEntry.COLUMN_RATING, String.format("'%s'",moviesData.get("vote_average")));
            Log.v("Checkin", FavoritesContract.FavoritesEntry.CONTENT_URI.toString());
            try {
                Uri uri = getContentResolver().insert(FavoritesContract.FavoritesEntry.CONTENT_URI, cv);
                if (uri != null) {
                    Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.v("favorites", "error adding");
            }

            MainActivity.addToFavorites(moviesData.get("title"));
        }
    }

    public void removeFromFavorite(){
        MainActivity.removeFromFavorites(moviesData.get("title"));

        Uri uri = FavoritesContract.FavoritesEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(moviesData.get("title")).build();

        getContentResolver().delete(uri, null, null);

//        try {
//            mDb.delete(FavoritesContract.FavoritesEntry.TABLE_NAME,
//                    FavoritesContract.FavoritesEntry.COLUMN_MOVIE_TITLE + "='" + movieTitle+"'", null);
//        }
//        catch (Exception e){
//            e.printStackTrace();
//            Log.e("Error Removing Movie", movieTitle);
//            return false;
//        }
    }

}
