package com.ayman.moviesland.network;




import com.ayman.moviesland.BuildConfig;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class Rt2fSingle {
    private static Retrofit retrofit;
    private Rt2fSingle(){}

    public static Retrofit newInstance() {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
