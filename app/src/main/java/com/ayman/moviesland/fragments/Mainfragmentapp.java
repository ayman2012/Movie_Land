package com.ayman.moviesland.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;


import com.ayman.moviesland.R;
import com.ayman.moviesland.adapters.Madapter;
import com.ayman.moviesland.models.fullresponse;
import com.ayman.moviesland.models.Movieapp;
import com.ayman.moviesland.network.Requestmoveapp;
import com.ayman.moviesland.network.Rt2fSingle;
import com.ayman.moviesland.utilities.FavoriteMoviesProvider;
import com.google.gson.Gson;
import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class Mainfragmentapp extends Fragment implements GridView.OnScrollListener, GridView.OnItemClickListener{

    private View rootView;
    private GridView gridview;
    private Madapter myAdapter;

    private long pageNumber;
    private String sortCriteria;

    private SharedPreferences sharedPreferences;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private Detailfragmentapp detailfragmentapp;
    private Setfragmentapp setfragmentapp;

    private Context context;
    private static final String LOG_TAG = Mainfragmentapp.class.getSimpleName();

    FavoriteMoviesProvider favoriteMoviesProvider = new FavoriteMoviesProvider();
    public ArrayList<Movieapp> results = new ArrayList<>();

    boolean isFavorite = false;

    public Mainfragmentapp() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        if (rootView != null)
            return rootView;

        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridview = (GridView) rootView.findViewById(R.id.gridView);

        myAdapter = new Madapter(getActivity(), results);

        gridview.setAdapter(myAdapter);

        gridview.setOnScrollListener(this);
        gridview.setOnItemClickListener(this);

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pageNumber = 1; //To begin loading from the first page
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            fragmentManager = this.getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            setfragmentapp = new Setfragmentapp();

            fragmentTransaction.replace(R.id.main_fragment, setfragmentapp);

            if (isTablet()) {

                if (detailfragmentapp != null && detailfragmentapp.isVisible())
                    fragmentTransaction.hide(detailfragmentapp);
            }
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        sortCriteria = getSortCriteria();
        if (sortCriteria.equals("favorites")) {
            isFavorite = true;
            results.clear();
            ArrayList<Movieapp> tempResult = favoriteMoviesProvider.loadFavorites(context);
            if (tempResult != null)
                results.addAll(favoriteMoviesProvider.loadFavorites(context));
        }
        else {
            isFavorite = false;
            results.clear();
            fetchMoviesByCriteria(sortCriteria, pageNumber);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this.getActivity();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        pageNumber = 1; //To begin loading from the first page
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        fragmentManager = this.getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        Bundle bundle = new Bundle();
        long movie_id = ((Movieapp) parent.getAdapter().getItem(position)).getId();

        bundle.putLong("movie_id", movie_id);
        bundle.putBoolean("isFavorite", isFavorite);

        if (isFavorite) {
            Gson gson = new Gson();
            String movieStr = gson.toJson(((Movieapp) parent.getAdapter().getItem(position)));
            bundle.putString("movie", movieStr);
        }

        detailfragmentapp = new Detailfragmentapp();
        detailfragmentapp.setArguments(bundle);

        //Handle tablet view
        if (isTablet()) {
            fragmentTransaction.replace(R.id.detail_fragment, detailfragmentapp);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            fragmentTransaction.replace(R.id.main_fragment, detailfragmentapp);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        Log.d(LOG_TAG, "onItemClick() Selected movie id: " + String.valueOf(movie_id));
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (totalItemCount > 0 && firstVisibleItem + visibleItemCount >= totalItemCount && !isFavorite)
            fetchMoviesByCriteria(sortCriteria, ++pageNumber);
    }

    private void fetchMoviesByCriteria(String sortBy, long page) {

        Requestmoveapp requestmoveapp = Rt2fSingle.newInstance().create(Requestmoveapp.class);
        Call<fullresponse> call = requestmoveapp.getFullResponse(sortBy, page);

        call.enqueue(new Callback<fullresponse>() {
            @Override
            public void onResponse(Response<fullresponse> response, Retrofit retrofit) {
                if (response.body() != null) {
                    results.addAll(response.body().getResults());
                    myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(context, "Check your internet connection!", Toast.LENGTH_LONG).show();
                Log.e(LOG_TAG, "onFailure() Failure has occurred in requesting movies from Retrofit", t);
            }
        });
    }

    private boolean isTablet() {
        return (this.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    private String getSortCriteria() {
        String currentSortCriteria = sharedPreferences.getString(getString(R.string.sort_key), getString(R.string.sort_default));
        return currentSortCriteria;
    }
}
