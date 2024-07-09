package com.example.tmdbmovies.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tmdbmovies.R;

public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ///Widgets

     ImageView imageView;
     RatingBar ratingBar;

    ///Click Listener
    OnMovieListener onMovieListener;

    public MovieViewHolder(@NonNull View itemView,OnMovieListener onMovieListener) {
        super(itemView);

        this.onMovieListener=onMovieListener;

        imageView=itemView.findViewById(R.id.movie_img);
        ratingBar=itemView.findViewById(R.id.rating_bar);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onMovieListener.onMovieClick(getAdapterPosition());
    }
}
