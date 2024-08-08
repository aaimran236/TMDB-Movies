package com.example.tmdbmovies;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.tmdbmovies.models.MovieModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MovieDetails extends AppCompatActivity {

    private ImageView imageViewDetails;
    private TextView titleDetails,descDetails;
    private RatingBar ratingBarDetails;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        imageViewDetails=findViewById(R.id.imageView_details);
        titleDetails=findViewById(R.id.textView_title_details);
        descDetails=findViewById(R.id.textView_desc_details);
        ratingBarDetails=findViewById(R.id.ratingBar_details);
        fab=findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getDataFromIntent();
    }

    private void getDataFromIntent() {
        if (getIntent().hasExtra("movie")){
            MovieModel movieModel=getIntent().getParcelableExtra("movie");
            ///Toast.makeText(this, ""+movieModel.getMovie_overview(), Toast.LENGTH_SHORT).show();
            titleDetails.setText(movieModel.getTitle());
            descDetails.setText(movieModel.getMovie_overview());
            ratingBarDetails.setRating(movieModel.getVote_average()/2f);


            Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w500/"
                            +movieModel.getPoster_path())
                    .into(imageViewDetails);
            ratingBarDetails.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    ratingBarDetails.setRating((movieModel.getVote_average())/2);
                }
            });
        }
    }
}