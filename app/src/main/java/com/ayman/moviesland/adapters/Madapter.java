package com.ayman.moviesland.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.ayman.moviesland.BuildConfig;
import com.ayman.moviesland.R;
import com.ayman.moviesland.models.Movieapp;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;




public class Madapter extends BaseAdapter{    //create by ayman
    private Context context;
    private ArrayList<Movieapp> result;
    private Holder holder;

    public Madapter(Context context, ArrayList<Movieapp> result) {
        this.context = context;
        this.result = result;
    }

    public int getCount() {

        return result.size();
    }

    public Movieapp getItem(int position) {

        return result.get(position);
    }

    public long getItemId(int position)
    {
        return result.get(position).getId();
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {

        if (convertView == null){
            convertView = getView();
        }
        else {
            holder = (Holder) convertView.getTag();
        }

        loadImageByPicasso(position);


        return convertView;
    }

    @NonNull
    private View getView() {
        View convertView;LayoutInflater inflater = LayoutInflater.from(context);
        convertView =  inflater.inflate(R.layout.grid_item, null);
        holder = new Holder();
        holder.imageView = (ImageView) convertView.findViewById(R.id.grid_row);
        convertView.setTag(holder);
        return convertView;
    }

    private void loadImageByPicasso(int position) {
        Picasso.with(context).load(BuildConfig.IMAGE_BASE_URL + result.get(position).
                getPoster_path()).fit().placeholder(R.drawable.placeholder).into(holder.imageView);
    }

    private static class Holder{
        ImageView imageView;
    }
}
