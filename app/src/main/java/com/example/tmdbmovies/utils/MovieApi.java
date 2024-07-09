package com.example.tmdbmovies.utils;

import com.example.tmdbmovies.models.MovieModel;
import com.example.tmdbmovies.response.MovieSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {

    ///Search for movie
    ///https://api.themoviedb.org/3/search/movie?query=Jack+Reacher&api_key=insert your api key
    @GET("/3/search/movie")
    Call<MovieSearchResponse> searchMovie(
            @Query("api_key") String key,
            @Query("query") String query,
            @Query("page") int page
    );

    ///Get popular movies
    ///https://api.themoviedb.org/3/movie/popular?api_key=insert your api key
    @GET("/3/movie/popular")
    Call<MovieSearchResponse> getPopular(
            @Query("api_key") String key,
            @Query("page") int page
    );

    ///Searching single movie by movie id
    ///https://api.themoviedb.org/3/movie/550?api_key=insert your api key
    @GET("/3/movie/{movie_id}?")
    Call<MovieModel> getMovie(
            @Path("movie_id") int movie_id,
            @Query("api_key") String key
    );
}
