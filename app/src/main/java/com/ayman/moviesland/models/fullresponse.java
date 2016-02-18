package com.ayman.moviesland.models;

import java.util.ArrayList;


public class fullresponse {
    private Long page;
    private ArrayList<Movieapp> results = new ArrayList<Movieapp>();
    private long allresults;
    private long allpages;

    public Long getPage() {
        return page;
    }

    public ArrayList<Movieapp> getResults() {
        return results;
    }

    public long getAllresults() {
        return allresults;
    }

    public long getAllpages() {
        return allpages;
    }
}
