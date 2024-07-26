package com.example.tmdbmovies.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tmdbmovies.R;
import com.example.tmdbmovies.models.MovieModel;
import com.example.tmdbmovies.utils.Credentials;

import java.util.List;

public class MovieRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MovieModel> mMovies;
    private OnMovieListener onMovieListener;
    private static final int DISPLAY_POP = 1;
    private static final int DISPLAY_SEARCH = 2;

    public MovieRecyclerView(OnMovieListener onMovieListener) {
        this.onMovieListener = onMovieListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;

        if (viewType == DISPLAY_SEARCH) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item,
                    parent, false);
            return new MovieViewHolder(view, onMovieListener);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_layout,
                    parent, false);
            return new Popular_view_holder(view, onMovieListener);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        ((MovieViewHolder)holder).title.setText(mMovies.get(position).getTitle());
//        ((MovieViewHolder)holder).release_date.setText(mMovies.get(position).getRelease_date());
//
//        ///There is an error in getting the duration
//        ((MovieViewHolder)holder).duration.setText(mMovies.get(position).getOriginal_language());

        ///vote average is in between 10 but out top rating is 5 stars 
        int itemViewType = getItemViewType(position);
        if (itemViewType == DISPLAY_SEARCH) {

            // vote average is over 10, and our rating bar is over 5 stars: dividing by 2
            ((MovieViewHolder) holder).ratingBar.setRating((mMovies.get(position).getVote_average())/2);

            ((MovieViewHolder) holder).ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    ((MovieViewHolder) holder).ratingBar.setRating ((mMovies.get(position).getVote_average())/2);
                }
            });

            // ImageView: Using Glide Library
            Glide.with(holder.itemView.getContext())
                    .load("https://image.tmdb.org/t/p/w500/"
                            + mMovies.get(position).getPoster_path())
                    .into(((MovieViewHolder) holder).imageView);

        } else {
            ((Popular_view_holder) holder).ratingBar_pop.setRating((mMovies.get(position).getVote_average())/2);
            ((Popular_view_holder) holder).ratingBar_pop.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    ((Popular_view_holder) holder).ratingBar_pop.setRating((mMovies.get(position).getVote_average())/2);
                }
            });

            // ImageView: Using Glide Library
            Glide.with(holder.itemView.getContext())
                    .load("https://image.tmdb.org/t/p/w500/"
                            + mMovies.get(position).getPoster_path())
                    .into(((Popular_view_holder) holder).imageView_pop);

        }

    }

    @Override
    public int getItemCount() {
        if (mMovies != null) {
            return mMovies.size();
        }
        return 0;
    }

    public void setmMovies(List<MovieModel> mMovies) {
        this.mMovies = mMovies;
        notifyDataSetChanged();
    }

    ///Getting the id of the movie clicked
    public MovieModel getSelectedMovie(int position) {
        if (mMovies != null) {
            if (mMovies.size() > 0) {
                return mMovies.get(position);
            }
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {

        if (Credentials.POPULAR){
            return DISPLAY_POP;
        }
        else
            return DISPLAY_SEARCH;
    }
}
