package com.example.tmdbmovies.request;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tmdbmovies.AppExecutors;
import com.example.tmdbmovies.models.MovieModel;
import com.example.tmdbmovies.response.MovieSearchResponse;
import com.example.tmdbmovies.utils.Credentials;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class MovieApiClient {

    // LiveData for search
    private MutableLiveData<List<MovieModel>> mMovies;
    private static MovieApiClient instance;

    // making Global RUNNABLE
    private RetrieveMoviesRunnable retrieveMoviesRunnable;

    ///Live data for popular movies
    private MutableLiveData<List<MovieModel>> mMoviesPop;

    // making Global RUNNABLE for popular movies
    private RetrieveMoviesRunnablePop retrieveMoviesRunnablePop;

    public static MovieApiClient getInstance() {
        if (instance == null) {
            instance = new MovieApiClient();
        }
        return instance;

    }

    private MovieApiClient() {
        mMovies = new MutableLiveData<>();
        mMoviesPop = new MutableLiveData<>();
    }


    public LiveData<List<MovieModel>> getMovies() {
        return mMovies;
    }

    public LiveData<List<MovieModel>> getMoviesPop() {
        return mMoviesPop;
    }

    //1- This method that we are going to call through the classes
    public void searchMoviesApi(String query, int pageNumber) {

        if (retrieveMoviesRunnable != null) {
            retrieveMoviesRunnable = null;
        }

        retrieveMoviesRunnable = new RetrieveMoviesRunnable(query, pageNumber);

        final Future myHandler = AppExecutors.getInstance().netWorkIO().submit(retrieveMoviesRunnable);

        AppExecutors.getInstance().netWorkIO().schedule(new Runnable() {
            @Override
            public void run() {
                // Cancelling the retrofit call
                myHandler.cancel(true);

            }
        }, 3000, TimeUnit.MILLISECONDS);


    }

    public void searchMoviesPop(int pageNumber) {

        if (retrieveMoviesRunnablePop != null) {
            retrieveMoviesRunnablePop = null;
        }

        retrieveMoviesRunnablePop = new RetrieveMoviesRunnablePop(pageNumber);

        final Future myHandler2 = AppExecutors.getInstance().netWorkIO().submit(retrieveMoviesRunnablePop);

        AppExecutors.getInstance().netWorkIO().schedule(new Runnable() {
            @Override
            public void run() {
                // Cancelling the retrofit call
                myHandler2.cancel(true);

            }
        }, 1000, TimeUnit.MILLISECONDS);


    }

    // Retrieving data from REstAPI by runnable class
    // WE have 2 types of Queries : the ID & search Queries
    private class RetrieveMoviesRunnable implements Runnable {

        private String query;
        private int pageNumber;
        boolean cancelRequest;


        public RetrieveMoviesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            // Getting the response objects

            try {

                Response<MovieSearchResponse> response = getMovies(query, pageNumber).execute();

                if (cancelRequest) {
                    return;
                }
                if (response.code() == 200) {
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse) response.body()).getMovies());
                    if (pageNumber == 1) {
                        // Sending data to live data
                        // PostValue: used for background thread
                        // setValue: not for background thread
                        mMovies.postValue(list);

                    } else {
                        List<MovieModel> currentMovies = mMovies.getValue();
                        currentMovies.addAll(list);
                        mMovies.postValue(currentMovies);
                    }
                } else {
                    String error = response.errorBody().string();
                    Log.v("Tag", "Error " + error);
                    mMovies.postValue(null);

                }
            } catch (IOException e) {
                e.printStackTrace();
                mMovies.postValue(null);

            }
        }

        // Search Method/ query
        private Call<MovieSearchResponse> getMovies(String query, int pageNumber) {
            return Servicey.getMovieApi().searchMovie(
                    Credentials.API_KEY,
                    query,
                    pageNumber
            );

        }

        private void cancelRequest() {
            Log.v("Tag", "Cancelling Search Request");
            cancelRequest = true;
        }
    }

    private class RetrieveMoviesRunnablePop implements Runnable {
        private int pageNumber;
        boolean cancelRequest;

        public RetrieveMoviesRunnablePop(int pageNumber) {

            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            // Getting the response objects

            try {

                Response<MovieSearchResponse> response2 = getPop(pageNumber).execute();

                if (cancelRequest) {
                    return;
                }
                if (response2.code() == 200) {
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse) response2.body()).getMovies());
                    if (pageNumber == 1) {
                        // Sending data to live data
                        // PostValue: used for background thread
                        // setValue: not for background thread
                        mMoviesPop.postValue(list);

                    } else {
                        List<MovieModel> currentMovies = mMoviesPop.getValue();
                        currentMovies.addAll(list);
                        mMoviesPop.postValue(currentMovies);
                    }
                } else {
                    String error = response2.errorBody().string();
                    Log.v("Tag", "Error " + error);
                    mMoviesPop.postValue(null);

                }
            } catch (IOException e) {
                e.printStackTrace();
                mMoviesPop.postValue(null);

            }
        }

        // Search Method/ query
        private Call<MovieSearchResponse> getPop(int pageNumber) {
            return Servicey.getMovieApi().getPopular(
                    Credentials.API_KEY,
                    pageNumber
            );

        }

        private void cancelRequest() {
            Log.v("Tag", "Cancelling Search Request");
            cancelRequest = true;
        }
    }
}

