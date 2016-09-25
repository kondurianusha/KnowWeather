package com.tramsun.knowweather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tramsun.knowweather.data.model.FirebaseWeather;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tusharacharya on 25/09/16.
 */
public class MyCitiesListAdapter extends RecyclerView.Adapter<CityViewHolder> {

    private final LayoutInflater inflater;
    private final ArrayList<FirebaseWeather> data;
    private final OnCityClicked listener;

    public interface OnCityClicked {
        void onCityClicked(FirebaseWeather firebaseWeather);
    }

    public MyCitiesListAdapter(Context context, OnCityClicked listener) {
        this.listener = listener;
        inflater = LayoutInflater.from(context);
        data = new ArrayList<>();
    }

    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CityViewHolder holder, int position) {
        final FirebaseWeather city = data.get(position);
        holder.textView.setText(city.getName());

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCityClicked(city);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<FirebaseWeather> firebaseWeathers) {
        data.clear();
        data.addAll(firebaseWeathers);
        notifyDataSetChanged();
    }
}
