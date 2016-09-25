package com.tramsun.knowweather.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class CityViewHolder extends RecyclerView.ViewHolder {
    public TextView textView;

    public CityViewHolder(View itemView) {
        super(itemView);

        textView = (TextView) itemView.findViewById(android.R.id.text1);
    }
}
