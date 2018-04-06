package com.example.harry.mynews.Util;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by harry on 23/03/2018.
 */

public class CountryLocUtil {
    private Application application;
    private String country;

    public CountryLocUtil(Application application) {
        this.application = application;
    }

    /**
     * Get ISO 3166-1 alpha-2 country code for this device (or null if not available)
     * @return country code or null
     */
    @Nullable
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    public String getUserCountryOrDefault(Activity activity) {
        // Request permision
        if (ActivityCompat.checkSelfPermission(application.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    2);
        }
        String countryName = null;
        LocationManager lm = (LocationManager) application.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        Geocoder geocoder = new Geocoder(application.getApplicationContext());
        for (String provider : lm.getAllProviders()) {
            @SuppressWarnings("ResourceType") Location location = lm.getLastKnownLocation(provider);
            if (location != null) {
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (addresses != null && addresses.size() > 0) {
                        countryName = addresses.get(0).getCountryCode();
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return countryName.toLowerCase();
    }
    @Nullable
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    public String getUserCountryOrDefault() {
        // Request permision
        String countryName = null;
        LocationManager lm = (LocationManager) application.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        Geocoder geocoder = new Geocoder(application.getApplicationContext());
        for (String provider : lm.getAllProviders()) {
            @SuppressWarnings("ResourceType") Location location = lm.getLastKnownLocation(provider);
            if (location != null) {
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (addresses != null && addresses.size() > 0) {
                        countryName = addresses.get(0).getCountryCode();
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return countryName.toLowerCase();
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
