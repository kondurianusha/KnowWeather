package com.tramsun.knowweather.data;

import android.content.Context;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tramsun.knowweather.data.model.FirebaseWeather;
import com.tramsun.knowweather.data.model.WeatherResponse;
import com.tramsun.knowweather.utils.KWUtils;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import timber.log.Timber;

public class MeraFirebaseManager {

  private static MeraFirebaseManager meraFirebaseManager;
  private final String androidId;
  private final FirebaseDatabase firebaseDatabase;

  public static MeraFirebaseManager getInstance(Context context) {
    if (meraFirebaseManager == null) {
      meraFirebaseManager = new MeraFirebaseManager(context);
    }
    return meraFirebaseManager;
  }

  private MeraFirebaseManager(Context context) {
    androidId = KWUtils.getAndroidID(context);
    firebaseDatabase = FirebaseDatabase.getInstance();
  }

  public Observable<WeatherResponse> addCityToFirebase(final WeatherResponse weatherResponse) {
    return Observable.create(subscriber -> {
      FirebaseWeather firebaseWeather = new FirebaseWeather(weatherResponse);
      firebaseDatabase.getReference()
          .child(androidId)
          .child(String.valueOf(firebaseWeather.getLocationId()))
          .setValue(firebaseWeather, (databaseError, databaseReference) -> {
            if (databaseError == null) {
              Timber.d("Data added to Firebase");
              subscriber.onNext(weatherResponse);
              subscriber.onCompleted();
            } else {
              Timber.e(databaseError.toException());
              subscriber.onError(databaseError.toException());
            }
          });
    });
  }

  public Observable<List<FirebaseWeather>> getCities() {
    return Observable.create(subscriber -> firebaseDatabase.getReference().child(androidId).addListenerForSingleValueEvent(new ValueEventListener() {
      @Override public void onDataChange(DataSnapshot dataSnapshot) {
        List<FirebaseWeather> data = new ArrayList<>();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
          data.add(snapshot.getValue(FirebaseWeather.class));
        }
        subscriber.onNext(data);
        subscriber.onCompleted();
      }

      @Override public void onCancelled(DatabaseError databaseError) {
        subscriber.onError(databaseError.toException());
      }
    }));
  }
}
