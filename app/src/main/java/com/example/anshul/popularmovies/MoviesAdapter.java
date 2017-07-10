package com.example.anshul.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.anshul.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

//The Adapter for the RecyclerView
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder>{

    private List<Map<String, String>> mMovieData;
    private Context context;

    private final MovieAdapterOnClickHandler mClickHandler;

    //The clickhandler that gets the information about the current movie
    public interface MovieAdapterOnClickHandler{
        void onCLick(Map<String, String> currentMovie);
    }

    //The View Handler implementation
    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView movieImageView;

        public MoviesAdapterViewHolder(View view) {
            super(view);
            movieImageView = view.findViewById(R.id.iv_movie_data);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickHandler.onCLick(mMovieData.get(getAdapterPosition()));
        }
    }

    //Setting the adapter using a constructor
    public MoviesAdapter(Context context, MovieAdapterOnClickHandler clickHandler) {
        this.context = context;
        mClickHandler = clickHandler;
    }

    //Inflating the view every time
    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context con = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(con);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new MoviesAdapterViewHolder(view);
    }

    //Setting the content of the ImageView
    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder moviesAdapterViewHolder, int position) {
        String movieAtPosition = mMovieData.get(position).get("poster_path");

        moviesAdapterViewHolder.movieImageView.setVisibility(View.VISIBLE);

        Picasso.with(context).load(NetworkUtils.buildImageUrl(true, movieAtPosition).toString()).into(moviesAdapterViewHolder.movieImageView);
    }

    @Override
    public int getItemCount() {
        if (null == mMovieData) return 0;
        return mMovieData.size();
    }

    public void setmMovieData(List<Map<String, String>> movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }
}
