package com.example.lbstest;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.LocationManagerBase;
import com.amap.api.maps.AMap;

import java.util.ArrayList;
import java.util.List;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.MapView;

import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity {

    public AMapLocationClient mLocationClient;

    private TextView positionText;

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationClient.setLocationListener(new MyLocationListener());
        MapView mapView = (MapView) findViewById(R.id.amapView);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        AMap aMap = mapView.getMap();
        aMap.setTrafficEnabled(false);
//        positionText = (TextView) findViewById(R.id.position_text_view);
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.
                permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.
                permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(android.Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.
                permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        } else {
            requestLocation();
        }
    }

//    @Override
//    protected void onResume(){
//        super.onResume();
//        mapView.onResume();
//    }
//
//    @Override
//    protected void onPause(){
//        super.onPause();
//        mapView.onPause();
//    }
//
//    @Override
//    protected void onDestroy(){
//        super.onDestroy();
//        mLocationClient.stopLocation();
//        mapView.onDestroy();
//    }


    private void requestLocation() {
        initLocation();
        mLocationClient.startLocation();
    }

    private void initLocation(){
        AMapLocationClientOption aMapLocationClientOption = new AMapLocationClientOption();
        aMapLocationClientOption.setInterval(5000);
        mLocationClient.setLocationOption(aMapLocationClientOption);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this,"必须同意所有权限才能使用该程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }
    public class MyLocationListener implements AMapLocationListener {

        @Override
        public void onLocationChanged(AMapLocation location) {
            StringBuilder currentPosition = new StringBuilder();
            currentPosition.append("纬度：").append(location.getLatitude()).append("\n");
            currentPosition.append("经度：").append(location.getLongitude()).append("\n");
            currentPosition.append("国家：").append(location.getCountry()).append("\n");
            currentPosition.append("省：").append(location.getProvince()).append("\n");
            currentPosition.append("市：").append(location.getCity()).append("\n");
            currentPosition.append("区：").append(location.getDistrict()).append("\n");
            currentPosition.append("街道：").append(location.getStreet()).append("\n");
            currentPosition.append("定位方式：");
            if (location.getLocationType() == AMapLocation.LOCATION_TYPE_GPS) {
                currentPosition.append("GPS");
            }else if (location.getLocationType() == AMapLocation.LOCATION_TYPE_WIFI) {
                currentPosition.append("WIFI");
            }
            positionText.setText(currentPosition);
        }
    }
}

