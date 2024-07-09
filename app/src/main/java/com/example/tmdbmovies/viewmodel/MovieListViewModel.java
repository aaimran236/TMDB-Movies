package com.example.tmdbmovies.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tmdbmovies.models.MovieModel;
import com.example.tmdbmovies.repositories.MovieRepository;

import java.util.List;

public class MovieListViewModel extends ViewModel {

    ///This class is used for view model

    private MovieRepository movieRepository;
    public MovieListViewModel() {
        movieRepository=MovieRepository.getInstance();
    }

    public LiveData<List<MovieModel>> getMovies() {
        return movieRepository.getMovies();
    }

    public LiveData<List<MovieModel>> getPop() {
        return movieRepository.getPop();
    }

    ///3- Calling the method in view model
    public void searchMovieApi(String query,int pageNumber){
        movieRepository.searchMovieApi(query,pageNumber);
    }

    public void searchMoviePop(int pageNumber){
        movieRepository.searchMoviePop(pageNumber);
    }
    public void searchNextPage(){
        movieRepository.searchNextPage();
    }
}
