package com.ayman.moviesland.models;

import java.util.ArrayList;

/**
 * Created by Kareem on 1/12/2016.
 */
public class TrailerResapp {
    private long id;

    private ArrayList<Trailerapp> results = new ArrayList<Trailerapp>();

    public ArrayList<Trailerapp> getResults() {
        return results;
    }

    public long getId() {
        return id;
    }

}
