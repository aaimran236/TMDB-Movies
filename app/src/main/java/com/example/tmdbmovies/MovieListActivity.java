package com.example.tmdbmovies;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tmdbmovies.adapter.MovieRecyclerView;
import com.example.tmdbmovies.adapter.OnMovieListener;
import com.example.tmdbmovies.models.MovieModel;
import com.example.tmdbmovies.networkcheck.CheckNetwork;
import com.example.tmdbmovies.networkcheck.NetworkChangeReceiver;
import com.example.tmdbmovies.viewmodel.MovieListViewModel;
import java.util.List;

public class MovieListActivity extends AppCompatActivity implements OnMovieListener {

    private RecyclerView recyclerView;
    private MovieRecyclerView movieRecyclerViewAdapter;

    /// ViewModel
    private MovieListViewModel movieListViewModel;

    boolean isPopular = true;
    private AlertDialog.Builder alertdialogBuilder;

    private NetworkChangeReceiver networkChangeReceiver;
    public static boolean called = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (CheckNetwork.isNetWorkAvailable(this)) {
            called = false;
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
                    movieRecyclerViewAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /// Observe any data change
    private void observeAnyChange() {
        movieListViewModel.getMovies().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                ///observing for any data change
                if (movieModels != null) {
//                    for (MovieModel movieModel: movieModels){
//                        Log.v("tagy","movies: "+movieModel.getTitle());

                    movieRecyclerViewAdapter.setmMovies(movieModels);
                    movieRecyclerViewAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /// 4- Calling the method in main activity
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
        Intent intent = new Intent(this, MovieDetails.class);
        intent.putExtra("movie", movieRecyclerViewAdapter.getSelectedMovie(position));
        startActivity(intent);
    }

    /// 5 Initializing recyclerView and data to it
    private void configureRecyclerView() {
        movieRecyclerViewAdapter = new MovieRecyclerView(this);
        recyclerView.setAdapter(movieRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this
                , LinearLayoutManager.HORIZONTAL, false));


        //RecyclerView pagination
        //Looking next page of api response
        /*recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1)){
                    ///here we need to display the next search result on the next page of api
                    movieListViewModel.searchNextPage();
                }
            }
        });*/
    }

    public void onScroll() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1)) {
                    ///here we need to display the next search result on the next page of api
                    movieListViewModel.searchNextPage();
                }
            }
        });
    }

    /// Getting data form searchView and query the api to get the result (movies)
    private void setUpSearchView() {
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
                onScroll();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    @SuppressLint("MissingSuperCall")
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

    public void refreshData() {
        if (called) {
            called = false;
            // Network is back, refresh the app
            Toast.makeText(this, "Internet connected, refreshing...", Toast.LENGTH_SHORT).show();
            recreate();
        }
    }
}