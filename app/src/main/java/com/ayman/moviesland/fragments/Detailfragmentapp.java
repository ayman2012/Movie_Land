package com.ayman.moviesland.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.ayman.moviesland.BuildConfig;
import com.ayman.moviesland.R;
import com.ayman.moviesland.adapters.revdapter;
import com.ayman.moviesland.adapters.trdapter;
import com.ayman.moviesland.models.Movieapp;
import com.ayman.moviesland.models.Reviewapp;
import com.ayman.moviesland.models.reviewResponseapp;
import com.ayman.moviesland.models.Trailerapp;
import com.ayman.moviesland.models.TrailerResapp;
import com.ayman.moviesland.network.Requestmoveapp;
import com.ayman.moviesland.network.Rt2fSingle;
import com.ayman.moviesland.utilities.FavoriteMoviesProvider;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;



public class Detailfragmentapp extends Fragment implements ListView.OnScrollListener, ListView.OnTouchListener, ImageButton.OnClickListener{

    private View rootview;
    private ImageButton imagebutton;
    private ImageView imageview;
    private ListView trailerslistview;
    private ListView reviewslistview;
    private ScrollView scrollview;


    private trdapter traileradapter;
    private revdapter reviewadapter;
    private Context context;
    private static final String LOG_TAG = Detailfragmentapp.class.getSimpleName();
    private Movieapp movieModel;
    private long movie_id;
    private ArrayList<Trailerapp> trailerresult = new ArrayList<Trailerapp>();
    private ArrayList<Reviewapp> reviewresult = new ArrayList<Reviewapp>();
    private FavoriteMoviesProvider favoritemovies;
    String movieStr = null;
    boolean isFavorite = false;

    public Detailfragmentapp() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this.getActivity();
        favoritemovies = new FavoriteMoviesProvider();
        Bundle bundle = getArguments();
        movie_id = bundle.getLong("movie_id");
        isFavorite = bundle.getBoolean("isFavorite");
        movieStr = bundle.getString("movie");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return getView(inflater, container);


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings && !istablet()) {
            FragmentManager fragmentManager = this.getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_fragment, new Setfragmentapp());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        return super.onOptionsItemSelected(item);
    }
    private View getView(LayoutInflater inflater, ViewGroup container) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_detail, container, false);
        trailerslistview = (ListView) rootview.findViewById(R.id.trailers_list);
        reviewslistview = (ListView) rootview.findViewById(R.id.reviews_list);
        imageview = (ImageView) rootview.findViewById(R.id.movie_poster);
        imagebutton = (ImageButton) rootview.findViewById(R.id.favourite_btn);
        scrollview = (ScrollView) rootview.findViewById(R.id.main_scrollview);

        imagebutton.setOnClickListener(this);
        reviewslistview.setOnScrollListener(this);
        reviewslistview.setOnTouchListener(this);

        setHasOptionsMenu(true);


        setHasOptionsMenu(true);
        return rootview;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Get favorite movies from sharedPreferences in offline mode
        if (isFavorite) {
            Gson gson = new Gson();
            Movieapp movieapp = gson.fromJson(movieStr, Movieapp.class);
            movieModel = movieapp;
            fill_layout(movieapp);

        }
        else {
            getdetails();
            get_trailers();
            getreviews();
        }
    }



    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (toplist(reviewslistview) && scrollState == SCROLL_STATE_IDLE)
            ((ScrollView) view.getParent().getParent()).pageScroll(View.FOCUS_UP);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {}

    @Override
    public void onClick(View v) {
        ImageButton image_btn = (ImageButton) v;
        image_btn.setSelected(!image_btn.isSelected());

        //Marked as favorite
        if (image_btn.isSelected()) {
            favoritemovies.addToFavorite(getActivity().getApplicationContext(), movieModel);
            Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT)
                    .show();

        }
        //Marked as non-favorite
        else {
            favoritemovies.removeFromFavorite(getActivity().getApplicationContext(), movieModel);
            Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT)
                    .show();
        }
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // Disallow the touch request for parent scroll on touch of child view
        v.getParent().requestDisallowInterceptTouchEvent(true);
        return false;
    }


    private void getdetails() {
        Requestmoveapp requestmoveapp = Rt2fSingle.newInstance().create(Requestmoveapp.class);
        Call<Movieapp> getMovieCall = requestmoveapp.getMovie(movie_id);
        getMovieCall.enqueue(new Callback<Movieapp>() {
            @Override
            public void onResponse(Response<Movieapp> response, Retrofit retrofit) {
                movieModel = response.body();
                fill_layout(movieModel);
            }

            @Override
            public void onFailure(Throwable t) {

                Toast.makeText(context, "Check your internet connection!", Toast.LENGTH_LONG).show();
                Log.e(LOG_TAG, "onFailure() Failure has occurred in requesting a movie with id: " + movie_id + " from Retrofit", t);
            }
        });

    }


    private void getreviews() {
        Requestmoveapp requestmoveapp = Rt2fSingle.newInstance().create(Requestmoveapp.class);
        Call<reviewResponseapp> getMovieCall = requestmoveapp.getReview(movie_id);
        getMovieCall.enqueue(new Callback<reviewResponseapp>() {
            @Override
            public void onResponse(Response<reviewResponseapp> response, Retrofit retrofit) {
                reviewresult = response.body().getResults();
                reviewadapter = new revdapter(getActivity(), reviewresult);
                reviewslistview.setAdapter(reviewadapter);
                setheight(reviewslistview);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(context, "Check your internet connection!", Toast.LENGTH_LONG).show();
                Log.e(LOG_TAG, "onFailure() Failure has occurred in requesting reviews of a movie with id: " + movie_id + " from Retrofit", t);
            }
        });
    }
    private boolean toplist(ListView listView) {
        if (listView.getChildCount() == 0) return true;
        return listView.getChildAt(0).getTop() == 0;
    }

    private void fill_layout(Movieapp movieModel) {
        TextView movieTitle = (TextView) rootview.findViewById(R.id.movie_title);
        movieTitle.setText(movieModel.getTitle());

        boolean favorite = favoritemovies.isFavorite(getActivity(), movieModel);
        imagebutton.setSelected(favorite);

        Picasso.with(getActivity().getApplicationContext())
                .load(BuildConfig.IMAGE_BASE_URL + movieModel.getPoster_path())
                .placeholder(R.drawable.placeholder)
                .into(imageview);

        DateFormat formatter = new SimpleDateFormat("yy-mm-dd");
        TextView movieYear = (TextView) rootview.findViewById(R.id.movie_year);
        try {
            Date date = formatter.parse(movieModel.getRelease_date());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            movieYear.setText(Integer.toString(calendar.get(Calendar.YEAR)));
        } catch (ParseException ex) {
        }


        TextView movieDuration = (TextView) rootview.findViewById(R.id.movie_duration);
        movieDuration.setText(String.format("%1$dmin", movieModel.getRuntime()));

        TextView movieRating = (TextView) rootview.findViewById(R.id.movie_rating);
        movieRating.setText(String.format("%1$.2f/10", movieModel.getVote_average()));

        TextView movieOverview = (TextView) rootview.findViewById(R.id.movie_overview);
        movieOverview.setText(movieModel.getOverview());
        scrollview.setVisibility(View.VISIBLE);
    }

    private void get_trailers() {
        Requestmoveapp requestmoveapp = Rt2fSingle.newInstance().create(Requestmoveapp.class);
        Call<TrailerResapp> getMovieCall = requestmoveapp.getTrailer(movie_id);
        getMovieCall.enqueue(new Callback<TrailerResapp>() {
            @Override
            public void onResponse(Response<TrailerResapp> response, Retrofit retrofit) {
                trailerresult = response.body().getResults();
                traileradapter = new trdapter(getActivity(), trailerresult);
                trailerslistview.setAdapter(traileradapter);
                setheight(trailerslistview);
            }
            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(context, "Check your internet connection!", Toast.LENGTH_LONG).show();
                Log.e(LOG_TAG, "onFailure() Failure has occurred in requesting trailers of a movie with id: " + movie_id + " from Retrofit", t);
            }
        });
    }

    //Set list view height based on its children
    private boolean setheight(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }
    }

    private boolean istablet() {
        return (this.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
