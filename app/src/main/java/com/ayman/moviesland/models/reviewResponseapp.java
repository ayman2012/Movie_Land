package com.ayman.moviesland.models;

import java.util.ArrayList;


public class reviewResponseapp {
    private long id;
    private long page;
    private ArrayList<Reviewapp> results = new ArrayList<Reviewapp>();
    private long totalPages;
    private long totalResults;

    public long getId() {
        return id;
    }

    public long getPage() {
        return page;
    }

    public ArrayList<Reviewapp> getResults() {
        return results;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public long getTotalResults() {
        return totalResults;
    }
}
