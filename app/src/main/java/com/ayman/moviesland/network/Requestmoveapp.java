package com.ayman.moviesland.network;



import com.ayman.moviesland.BuildConfig;
import com.ayman.moviesland.models.fullresponse;
import com.ayman.moviesland.models.Movieapp;
import com.ayman.moviesland.models.reviewResponseapp;
import com.ayman.moviesland.models.TrailerResapp;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;


public interface Requestmoveapp {
    @GET("discover/movie?api_key=" + BuildConfig.API_KEY)
    public Call<fullresponse> getFullResponse(@Query("sort_by") String sort_by, @Query("page") long page);

    @GET("movie/{id}?api_key=" + BuildConfig.API_KEY)
    Call<Movieapp> getMovie(@Path("id") long id);

    @GET("movie/{id}/videos?api_key=" + BuildConfig.API_KEY)
    public Call<TrailerResapp> getTrailer(@Path("id") long id);

    @GET("movie/{id}/reviews?api_key=" + BuildConfig.API_KEY)
    public Call<reviewResponseapp> getReview(@Path("id") long id);

}
