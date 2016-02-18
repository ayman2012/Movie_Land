package com.ayman.moviesland.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;


import com.ayman.moviesland.BuildConfig;
import com.ayman.moviesland.R;
import com.ayman.moviesland.models.Trailerapp;

import java.util.ArrayList;



public class trdapter extends BaseAdapter{   //create by ayman

    private Context context;
    private ArrayList<Trailerapp> result;
    private Holder holder;

    public trdapter(Context context, ArrayList<Trailerapp> result) {
        this.context = context;
        this.result = result;
    }

    public int getCount() {
        return result.size();
    }

    public Trailerapp getItem(int position) {
        return result.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = getView(position);
        }
        else {
            holder = (Holder) convertView.getTag();
        }

        holder.name.setText(result.get(position).getName());

        return convertView;
    }

    @NonNull
    private View getView(final int position) {
        View convertView;LayoutInflater inflater = LayoutInflater.from(context);
        convertView =  inflater.inflate(R.layout.trailers_list_item, null);
        holder = new Holder();
        holder.image = (ImageButton) convertView.findViewById(R.id.play_trailer);
        holder.name = (TextView) convertView.findViewById(R.id.trailer_name);

        convertView.setTag(holder);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + result.get(position).getKey()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                catch (ActivityNotFoundException ex) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.YOUTUBE_BASE_URL+ "?v=" + result.get(position).getKey()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
        return convertView;
    }

    private static class Holder {
        ImageButton image;
        TextView name;
    }
}
