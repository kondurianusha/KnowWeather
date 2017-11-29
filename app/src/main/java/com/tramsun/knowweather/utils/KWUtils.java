package com.tramsun.knowweather.utils;

import android.content.Context;
import android.provider.Settings;
import com.tramsun.knowweather.R;

public class KWUtils {

  public static String getAndroidID(Context context) {
    return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
  }

  public static int getImageForWeatherCode(String weatherCode) {
    String code = weatherCode.substring(0, weatherCode.length() - 1);
    switch (code) {
      case "01":
        return R.drawable.default_weather;
      case "02":
      case "03":
      case "04":
        return R.drawable.cloudy;
      case "09":
      case "10":
        return R.drawable.rainy;
      case "11":
        return R.drawable.stormy;
      case "13":
        return R.drawable.snowy;
      case "50":
        return R.drawable.misty;
      default:
        return R.drawable.default_weather;
    }
  }
}
