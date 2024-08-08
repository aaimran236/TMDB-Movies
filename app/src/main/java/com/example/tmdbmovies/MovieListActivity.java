package com.example.tmdbmovies;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tmdbmovies.adapter.MovieRecyclerView;
import com.example.tmdbmovies.adapter.OnMovieListener;
import com.example.tmdbmovies.models.MovieModel;
import com.example.tmdbmovies.networkcheck.CheckNetwork;
import com.example.tmdbmovies.networkcheck.NetworkChangeReceiver;
import com.example.tmdbmovies.utils.Credentials;
import com.example.tmdbmovies.viewmodel.MovieListViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;



public class MovieListActivity extends AppCompatActivity implements OnMovieListener {

    private RecyclerView recyclerView;
    private MovieRecyclerView movieRecyclerViewAdapter;

    ///ViewModel
    private MovieListViewModel movieListViewModel;

    boolean isPopular=true;

    private  AlertDialog.Builder alertdialogBuilder;

    private NetworkChangeReceiver networkChangeReceiver;
    public static boolean called=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (CheckNetwork.isNetWorkAvailable(this)){
            called=false;
        }

        ///Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        recyclerView = findViewById(R.id.recyclerView);
        ///checking whether network connection is active or not

        ///SearchView
        setUpSearchView();

        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        configureRecyclerView();

        observeAnyChange();
        observePopularMovies();

        ///Getting popular movies
        movieListViewModel.searchMoviePop(1);

    }

    private void observePopularMovies() {
        movieListViewModel.getPop().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                ///observing for any data change
                if (movieModels != null) {
//                    for (MovieModel movieModel: movieModels){
//                        Log.v("tagy","movies: "+movieModel.getTitle());

                    movieRecyclerViewAdapter.setmMovies(movieModels);

                }
            }
        });
    }

    ///Observe any data change
    private void observeAnyChange() {
        movieListViewModel.getMovies().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                ///observing for any data change
                if (movieModels != null) {
//                    for (MovieModel movieModel: movieModels){
//                        Log.v("tagy","movies: "+movieModel.getTitle());

                    movieRecyclerViewAdapter.setmMovies(movieModels);

                }
            }
        });
    }

    ///4- Calling the method in main activity
//    public void searchMovieApi(String query, int pageNumber) {
//        movieListViewModel.searchMovieApi(query, pageNumber);
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }

    @Override
    public void onMovieClick(int position) {
        ///Toast.makeText(this, "Position = " + position, Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(this, MovieDetails.class);
        intent.putExtra("movie",movieRecyclerViewAdapter.getSelectedMovie(position));
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {

    }

    ///5 Initializing recyclerView and data to it
    private void configureRecyclerView() {
        movieRecyclerViewAdapter = new MovieRecyclerView(this);
        recyclerView.setAdapter(movieRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this
                ,LinearLayoutManager.HORIZONTAL,false));


        ///RecyclerView pagination
        ///Looking next page of api response
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                if (!recyclerView.canScrollVertically(1)){
//                    ///here we need to display the next search result on the next page of api
//                    movieListViewModel.searchNextPage();
//                }
//            }
//        });
    }

    public void onScroll(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1)){
                    ///here we need to display the next search result on the next page of api
                    movieListViewModel.searchNextPage();
                }
            }
        });
    }

    ///Getting data form searchView and query the api to get the result (movies)
    private void setUpSearchView(){
        final SearchView searchView = findViewById(R.id.search_view);


        // Detect Search
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do what you want when search view expended
                isPopular = false;
                ///Log.v("Tagy", "ispop: " +isPopular);

            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                //do what you want  searchView is not expanded
                movieListViewModel.getMovies().removeObserver(new Observer<List<MovieModel>>() {
                    @Override
                    public void onChanged(List<MovieModel> movieModels) {


                    }
                });
                
                recyclerView.clearOnScrollListeners();
                ///Getting popular movies
                movieListViewModel.searchMoviePop(1);
                configureRecyclerView();
                ///observePopularMovies();
                return false;
            }
        });




        // Make search query
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                movieListViewModel.searchMovieApi(
                        // The search string getted from searchview
                        query,
                        1
                );
                configureRecyclerView();
                onScroll();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {

        alertdialogBuilder = new AlertDialog.Builder(this);
        alertdialogBuilder.setTitle(R.string.title_string1);
        alertdialogBuilder.setMessage(R.string.title_message1);
        alertdialogBuilder.setIcon(R.drawable.alert1);
        alertdialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        alertdialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog = alertdialogBuilder.create();
        alertDialog.show();
    }

    public void refreshData(){
        if (called){
            called=false;
            // Network is back, refresh the app
            Toast.makeText(this, "Internet connected, refreshing...", Toast.LENGTH_SHORT).show();
            recreate();
        }
    }

}
//    private void getRetrofitResponse() {
//        MovieApi movieApi = Servicey.getMovieApi();
//
//        Call<MovieSearchResponse> responseCall = movieApi
//        .searchMovie(
//                Credentials.API_KEY,
//                "Jack Reacher",
//                1);
//
//
//        responseCall.enqueue(new Callback<MovieSearchResponse>() {
//            @Override
//            public void onResponse(Call<MovieSearchResponse> call, Response<MovieSearchResponse> response) {
//                if (response.code() == 200){
//
//                    Log.v("Tag", "the response" +response.body().toString());
//
//                    List<MovieModel> movies = new ArrayList<>(response.body().getMovies());
//
//                    for (MovieModel movie: movies){
//                        Log.v("Tag" , "Name: " + movie.getRelease_date());
//                    }
//                }
//                else
//                {
//
//                    try {
//                        Log.v("Tag", "Error" + response.errorBody().string());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MovieSearchResponse> call, Throwable t) {
//                t.printStackTrace();
//
//            }
//        });
//    }

//    private void getRetrofitResponseAccordingToID(){
//        MovieApi movieApi=Servicey.getMovieApi();
//        Call<MovieModel> responseCall=movieApi.getMovie(
//                343611,
//                Credentials.API_KEY
//        );
//
//        responseCall.enqueue(new Callback<MovieModel>() {
//            @Override
//            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
//                if (response.code()==200){
//                    MovieModel movie=response.body();
//                    Log.v("Tag","Response: "+movie.getTitle());
//                }else {
//                    try {
//                        Log.v("Tag","Error: "+response.errorBody().string());
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MovieModel> call, Throwable throwable) {
//
//            }
//        });
//    }
//}