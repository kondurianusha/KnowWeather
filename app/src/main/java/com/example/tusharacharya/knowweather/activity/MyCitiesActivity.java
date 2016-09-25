package com.example.tusharacharya.knowweather.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;

import com.example.tusharacharya.knowweather.R;
import com.example.tusharacharya.knowweather.adapter.MyCitiesListAdapter;
import com.example.tusharacharya.knowweather.data.MeraFirebaseManager;
import com.example.tusharacharya.knowweather.data.model.FirebaseWeather;
import com.example.tusharacharya.knowweather.databinding.ActivityMyCitiesBinding;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class MyCitiesActivity extends AppCompatActivity {
    private ActivityMyCitiesBinding binding;
    private MeraFirebaseManager firebaseManager;
    private MyCitiesListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_cities);
        firebaseManager = MeraFirebaseManager.getInstance(this);

        binding.myCitiesToolbar.setTitle("My Cities");
        setSupportActionBar(binding.myCitiesToolbar);

        adapter = new MyCitiesListAdapter(this, new MyCitiesListAdapter.OnCityClicked() {
            @Override
            public void onCityClicked(FirebaseWeather firebaseWeather) {
                Intent intent = new Intent();
                intent.putExtra("CityName", firebaseWeather.getName());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        binding.listContainer.setLayoutManager(new LinearLayoutManager(this));
        binding.listContainer.setHasFixedSize(true);
        binding.listContainer.setAdapter(adapter);

        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddCityClicked();
            }
        });
    }

    private void onAddCityClicked() {
        final EditText editText = new EditText(this);
        Timber.d("Add button Clicked !!");
        new AlertDialog.Builder(this)
                .setTitle("Add city name")
                .setView(editText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String cityName = editText.getText().toString();
                        Intent intent = new Intent();
                        intent.putExtra("CityName", cityName);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        firebaseManager.getCities()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<FirebaseWeather>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<FirebaseWeather> firebaseWeathers) {
                        adapter.setData(firebaseWeathers);
                    }
                });
    }
}
