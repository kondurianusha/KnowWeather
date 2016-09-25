package com.example.tusharacharya.knowweather.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by tusharacharya on 25/09/16.
 */
public class CityViewHolder extends RecyclerView.ViewHolder {
    public TextView textView;

    public CityViewHolder(View itemView) {
        super(itemView);

        textView = (TextView) itemView.findViewById(android.R.id.text1);
    }
}
