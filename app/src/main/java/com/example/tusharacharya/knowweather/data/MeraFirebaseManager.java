package com.example.tusharacharya.knowweather.data;

import android.content.Context;

import com.example.tusharacharya.knowweather.data.model.FirebaseWeather;
import com.example.tusharacharya.knowweather.data.model.WeatherResponse;
import com.example.tusharacharya.knowweather.utils.KWUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by tusharacharya on 24/09/16.
 */
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

    public Observable<Boolean> addCityToFirebase(final WeatherResponse weatherResponse) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(final Subscriber<? super Boolean> subscriber) {
                FirebaseWeather firebaseWeather = new FirebaseWeather(weatherResponse);
                firebaseDatabase.getReference().child(androidId).child(String.valueOf(firebaseWeather.getLocationId())).setValue(firebaseWeather, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            Timber.d("Data Add to Firebase");
                            subscriber.onNext(true);
                            subscriber.onCompleted();

                        } else {
                            Timber.e(databaseError.toException());
                            subscriber.onNext(false);
                            subscriber.onCompleted();
                        }

                    }
                });

            }
        });

    }

    public Observable<List<FirebaseWeather>> getCities() {
        return Observable.create(new Observable.OnSubscribe<List<FirebaseWeather>>() {
            @Override
            public void call(final Subscriber<? super List<FirebaseWeather>> subscriber) {
                firebaseDatabase.getReference().child(androidId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<FirebaseWeather> data = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            data.add(snapshot.getValue(FirebaseWeather.class));

                        }
                        subscriber.onNext(data);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        subscriber.onError(databaseError.toException());
                    }
                });
            }
        });
    }
}
