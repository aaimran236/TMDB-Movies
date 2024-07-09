package com.example.tmdbmovies.request;

import com.example.tmdbmovies.utils.Credentials;
import com.example.tmdbmovies.utils.MovieApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Servicey {
    private static Retrofit.Builder retrofitBuilder=
            new Retrofit.Builder()
                    .baseUrl(Credentials.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit=retrofitBuilder.build();

//    private static MovieApi movieApi=retrofit.create(MovieApi.class);
    private static MovieApi movieApi;

    public static MovieApi getMovieApi(){
        if (movieApi==null){
            movieApi=retrofit.create(MovieApi.class);
        }
        return movieApi;
    }

}
