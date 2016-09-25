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

import com.example.tusharacharya.knowweather.R;
import com.example.tusharacharya.knowweather.data.DataUtils;
import com.example.tusharacharya.knowweather.data.MeraFirebaseManager;
import com.example.tusharacharya.knowweather.data.WeatherApi;
import com.example.tusharacharya.knowweather.data.model.FirebaseWeather;
import com.example.tusharacharya.knowweather.data.model.Weather;
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

import static com.example.tusharacharya.knowweather.Constants.OW_APPID;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    MeraFirebaseManager firebaseManager;
    WeatherApi weatherApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        firebaseManager = MeraFirebaseManager.getInstance(this);
        weatherApi = DataUtils.provideWeatherApi();

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
                onAddCityClicked();
            }
        });

    }

    private void onAddCityClicked() {
        final EditText editText = new EditText(HomeActivity.this);
        Timber.d("Add button Clicked !!");
        new AlertDialog.Builder(HomeActivity.this)
                .setTitle("Add city name")
                .setView(editText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String cityName = editText.getText().toString();
                        Observable<WeatherResponse> observable = weatherApi.getWeatherForCity(OW_APPID, cityName);
                        fetchWeatherForCityAndSaveOnFirebase(observable);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void handleUiWithData(List<FirebaseWeather> firebaseWeathers) {
        if (!firebaseWeathers.isEmpty()) {
            Observable<WeatherResponse> observable = weatherApi.getWeatherForCityWithId(OW_APPID, firebaseWeathers.get(0).getLocationId());
            fetchWeatherForCityAndSaveOnFirebase(observable);
        } else {
            showNoData(true);
        }
    }


    private void showNoData(boolean showNoDataText) {
        if (showNoDataText) {
            binding.dataContainer.setVisibility(View.GONE);
            binding.noDataText.setVisibility(View.VISIBLE);
        } else {
            binding.dataContainer.setVisibility(View.VISIBLE);
            binding.noDataText.setVisibility(View.GONE);
        }
    }

    private void fetchWeatherForCityAndSaveOnFirebase(Observable<WeatherResponse> observable) {
        observable
                .flatMap(new Func1<WeatherResponse, Observable<WeatherResponse>>() {
                    @Override
                    public Observable<WeatherResponse> call(WeatherResponse weatherResponse) {
                        return firebaseManager.addCityToFirebase(weatherResponse);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<WeatherResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(WeatherResponse weatherResponse) {
                        binding.toolbarHome.setTitle(weatherResponse.getName());
                        binding.tempText.setText(String.format("Temperature - %s", weatherResponse.getMain().getTemp()));
                        binding.pressureText.setText(String.format("Pressure - %s", weatherResponse.getMain().getPressure()));
                        binding.humidityText.setText(String.format("Humidity - %s", weatherResponse.getMain().getHumidity()));
                        binding.weatherImage.setImageResource(KWUtils.getImageForWeatherCode(weatherResponse.getWeather().get(0).getIcon()));

                        showNoData(false);
                    }
                });
    }
}
