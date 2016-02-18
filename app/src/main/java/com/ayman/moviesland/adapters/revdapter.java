package com.ayman.moviesland.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.ayman.moviesland.R;
import com.ayman.moviesland.models.Reviewapp;

import java.util.ArrayList;




public class revdapter extends BaseAdapter{   //create by ayman
    private Context context;
    private ArrayList<Reviewapp> result;
    private Holder holder;
    private Reviewapp reviewapp;

    public revdapter(Context context, ArrayList<Reviewapp> result) {
        this.context = context;
        this.result = result;
    }

    public int getCount() {
        return result.size();
    }

    public Reviewapp getItem(int position) {
        return result.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        reviewapp = result.get(position);
        if (convertView == null){
            convertView = getView();
        }
        else {
            holder = (Holder) convertView.getTag();
        }

        return convertView;
    }

    @NonNull
    private View getView() {
        View convertView;LayoutInflater inflater = LayoutInflater.from(context);
        convertView =  inflater.inflate(R.layout.reviews_list_item, null);
        holder = new Holder();
        holder.author = (TextView) convertView.findViewById(R.id.author);
        holder.content = (TextView) convertView.findViewById(R.id.content);

        convertView.setTag(holder);

        holder.author.setText(reviewapp.getAuthor());
        holder.content.setText(Html.fromHtml(reviewapp.getContent()));
        return convertView;
    }

    private static class Holder {
        TextView author;
        TextView content;
    }
}
