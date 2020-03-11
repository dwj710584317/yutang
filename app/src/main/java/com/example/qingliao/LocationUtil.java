package com.example.qingliao;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public class LocationUtil {
    public static final int LOCATION_CODE = 301;
    private static LocationManager locationManager;
    private static String locationProvider;
    private Context context;

    public static List<String> getLocation(Context context){
        List<String> lal =new ArrayList<>();
        //1.获取位置管理器
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //2.获取位置提供器，GPS或是NetWork
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是网络定位
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS定位
            locationProvider = LocationManager.GPS_PROVIDER;
        } else {
            Log.d("onRequestPermissionsResultGps","没有可用的位置提供器");
            return lal;
        }
        //高版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //3.获取上次的位置，一般第一次运行，此值为null
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //请求权限
                ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE);
                return lal;
            }else {
                Location location = locationManager.getLastKnownLocation(locationProvider);
                if (location!=null){
                    lal=showLocation(location);
                }
//                else{
//                    // 监视地理位置变化，第二个和第三个参数分别为更新的最短时间minTime和最短距离minDistace
//                    locationManager.requestLocationUpdates(locationProvider, 0, 0,mListener);
//                }

            } return lal;
        }else{
            Location location = locationManager.getLastKnownLocation(locationProvider);
            if (location!=null){
                showLocation(location);
            }
//              else  {
//                // 监视地理位置变化，第二个和第三个参数分别为更新的最短时间minTime和最短距离minDistace
//                locationManager.requestLocationUpdates(locationProvider, 0, 0,mListener);
//            }
        }
        return lal;
    }


    private static List<String> showLocation(Location location){
        List<String> lal =new ArrayList<>();
        String address = "纬度："+location.getLatitude()+"经度："+location.getLongitude();
        lal.add(String.valueOf(location.getLatitude()));
        lal.add(String.valueOf(location.getLongitude()));
        Log.d("gps",address);
        return lal;
    }



}
