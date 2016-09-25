package com.example.tusharacharya.knowweather.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tusharacharya.knowweather.R;
import com.example.tusharacharya.knowweather.data.DataUtils;
import com.example.tusharacharya.knowweather.data.MeraFirebaseManager;
import com.example.tusharacharya.knowweather.data.WeatherApi;
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

import static com.example.tusharacharya.knowweather.Constants.OW_APPID;

public class HomeActivity extends AppCompatActivity {

    private static final int REQUEST_MY_CITIES = 4657;
    ActivityHomeBinding binding;
    MeraFirebaseManager firebaseManager;
    WeatherApi weatherApi;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        initModules();

        binding.toolbarHome.setTitle("K W App");
        setSupportActionBar(binding.toolbarHome);

        Timber.d("Android ID is %s", KWUtils.getAndroidID(this));
    }

    private void initModules() {
        firebaseManager = MeraFirebaseManager.getInstance(this);
        weatherApi = DataUtils.provideWeatherApi();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait..");
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
            Intent intent = new Intent(this, MyCitiesActivity.class);
            startActivityForResult(intent, REQUEST_MY_CITIES);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_MY_CITIES) {
            if (resultCode == RESULT_OK) {
                progressDialog.show();
                String cityName = data.getStringExtra("CityName");
                Observable<WeatherResponse> observable = weatherApi.getWeatherForCity(OW_APPID, cityName);
                fetchWeatherForCityAndSaveOnFirebase(observable);
            }
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        progressDialog.show();

        firebaseManager.getCities()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<FirebaseWeather>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showError(e);
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

    private void showError(Throwable e) {
        Timber.e(e);
        progressDialog.dismiss();
        Toast.makeText(HomeActivity.this, "An error occurred", Toast.LENGTH_LONG).show();
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
                        progressDialog.show();
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
            progressDialog.dismiss();
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
                        showError(e);
                    }

                    @Override
                    public void onNext(WeatherResponse weatherResponse) {
                        binding.toolbarHome.setTitle(weatherResponse.getName());
                        binding.tempText.setText(String.format("Temperature - %s", weatherResponse.getMain().getTemp()));
                        binding.pressureText.setText(String.format("Pressure - %s", weatherResponse.getMain().getPressure()));
                        binding.pressureText.setText(String.format("Pressure - %s", weatherResponse.getMain().getPressure()));
                        binding.humidityText.setText(String.format("Humidity - %s", weatherResponse.getMain().getHumidity()));

                        Glide.with(HomeActivity.this)
                                .load(KWUtils.getImageForWeatherCode(weatherResponse.getWeather().get(0).getIcon()))
                                .into(binding.weatherImage);

                        showNoData(false);
                        progressDialog.dismiss();
                    }
                });
    }
}
