package com.example.tmdbmovies.networkcheck;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.tmdbmovies.MovieListActivity;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private boolean isConnected = false;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (CheckNetwork.isNetWorkAvailable(context)) {
            if (!isConnected) {
                isConnected = true;

                // Call your refresh method here
                ((MovieListActivity) context).refreshData();
            }
        } else {
            isConnected = false;
            MovieListActivity.called=true;
            // Inform the user about the disconnection
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }
}
