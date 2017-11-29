package com.tramsun.knowweather.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.EditText;
import com.tramsun.knowweather.R;
import com.tramsun.knowweather.adapter.MyCitiesListAdapter;
import com.tramsun.knowweather.data.MeraFirebaseManager;
import com.tramsun.knowweather.data.model.FirebaseWeather;
import com.tramsun.knowweather.databinding.ActivityMyCitiesBinding;
import java.util.List;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class MyCitiesActivity extends AppCompatActivity {
    private ActivityMyCitiesBinding binding;
    private MeraFirebaseManager firebaseManager;
    private MyCitiesListAdapter adapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_cities);
        initModules();

        binding.myCitiesToolbar.setTitle("My Cities");
        setSupportActionBar(binding.myCitiesToolbar);

      adapter = new MyCitiesListAdapter(this, firebaseWeather -> {
        Intent intent = new Intent();
        intent.putExtra("CityName", firebaseWeather.getName());
        setResult(RESULT_OK, intent);
        finish();
        });

        binding.listContainer.setLayoutManager(new LinearLayoutManager(this));
        binding.listContainer.setHasFixedSize(true);
        binding.listContainer.setAdapter(adapter);

      binding.addButton.setOnClickListener(v -> onAddCityClicked());
    }

    private void initModules() {
        firebaseManager = MeraFirebaseManager.getInstance(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait..");
    }

    private void onAddCityClicked() {
        final EditText editText = new EditText(this);
        Timber.d("Add button Clicked !!");
        new AlertDialog.Builder(this)
                .setTitle("Add city name").setView(editText).setPositiveButton("Add", (dialog, which) -> {
          String cityName = editText.getText().toString();
          Intent intent = new Intent();
          intent.putExtra("CityName", cityName);
          setResult(RESULT_OK, intent);
          finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
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
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onNext(List<FirebaseWeather> firebaseWeathers) {
                        adapter.setData(firebaseWeathers);
                        progressDialog.dismiss();
                    }
                });
    }
}
