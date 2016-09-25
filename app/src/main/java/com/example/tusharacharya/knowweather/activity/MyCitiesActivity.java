package com.example.tusharacharya.knowweather.activity;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.tusharacharya.knowweather.R;
import com.example.tusharacharya.knowweather.databinding.ActivityHomeBinding;
import com.example.tusharacharya.knowweather.databinding.ActivityMyCitiesBinding;

public class MyCitiesActivity extends AppCompatActivity {
    private ActivityMyCitiesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  DataBindingUtil.setContentView(this, R.layout.activity_my_cities);

        binding.myCitiesToolbar.setTitle("My Cities");


    }
}
