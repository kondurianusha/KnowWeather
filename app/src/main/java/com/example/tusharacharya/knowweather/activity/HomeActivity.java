package com.example.tusharacharya.knowweather.activity;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tusharacharya.knowweather.Constants;
import com.example.tusharacharya.knowweather.R;
import com.example.tusharacharya.knowweather.data.DataUtils;
import com.example.tusharacharya.knowweather.data.MeraFirebaseManager;
import com.example.tusharacharya.knowweather.data.model.FirebaseWeather;
import com.example.tusharacharya.knowweather.data.model.WeatherResponse;
import com.example.tusharacharya.knowweather.databinding.ActivityHomeBinding;
import com.example.tusharacharya.knowweather.utils.KWUtils;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    MeraFirebaseManager firebaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        firebaseManager = MeraFirebaseManager.getInstance(this);

        binding.toolbarHome.setTitle("K W App");
        setSupportActionBar(binding.toolbarHome);

        Timber.d("Android ID is %s", KWUtils.getAndroidID(this));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_home, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_my_cities) {
            // TODO: 24/09/16 implement click
        }
        return super.onOptionsItemSelected(item);
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
                        handleUiWithData(firebaseWeathers);
                    }
                });
        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(HomeActivity.this);
                Timber.d("Add button Clicked !!");
                new AlertDialog.Builder(HomeActivity.this)
                        .setTitle("Add city name")
                        .setView(editText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                addCity(editText.getText().toString());
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

    }

    private void handleUiWithData(List<FirebaseWeather> firebaseWeathers) {
        if(firebaseWeathers.isEmpty()){
            
        }
    }

    private void addCity(String cityName) {
        DataUtils.provideWeatherApi().getWeatherForCity(Constants.OPEN_WEATHER_APPID, cityName)
                .flatMap(new Func1<WeatherResponse, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(WeatherResponse weatherResponse) {
                        return firebaseManager.addCityToFirebase(weatherResponse);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean success) {
                        if (success) {
                            Toast.makeText(HomeActivity.this, "Added to Firebase Successfully", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(HomeActivity.this, "Error in adding to Firebase", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
