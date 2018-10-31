package com.example.boobasedriver2.boobase;

import android.content.Context;

import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;

import com.example.boobasedriver2.boobase.entity.LocationEntity;
import com.example.boobasedriver2.utils.FileUtil;
import com.example.boobasedriver2.utils.PathManager;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


/**
 * create by zzh on 2018/10/23
 */
public class LocationsManager {

    private String TAG = "TAG";
    private static LocationsManager mInstance;
    private List<LocationEntity> locationLists;
    private ArrayMap<String, LocationEntity.CoordinateBean> coordinateMap;  //名字对应坐标的Map


    public static synchronized LocationsManager CreateInstance() {
        if (mInstance == null) {
            mInstance = new LocationsManager();
        }
        return mInstance;
    }

    public static LocationsManager getInstance() {
        return mInstance;
    }


    public LocationsManager() {
        initLocation();
    }

    //读取配置文件的位置信息
    private void initLocation() {
       // String location=null;
        String location = FileUtil.readFileFromSDCard(PathManager.CONFIGURATION_PATH, "locations.cfg");
        if (TextUtils.isEmpty(location)) {
            // TODO: 2018/10/29 提示未能初始化配置文件
            Log.e(TAG, "locations.cfg 配置文件不存在");
            return;
        }
        try {
            List<LocationEntity> locationLists = new Gson().fromJson(location, new TypeToken<List<LocationEntity>>() {
            }.getType());
            this.setLocationLists(locationLists);
            ArrayMap<String, LocationEntity.CoordinateBean> coordinateMap = new ArrayMap<>();
            for (LocationEntity entity : locationLists) {
                coordinateMap.put(entity.getName(), entity.getCoordinate());
            }
            setCoordinateMap(coordinateMap);
            Log.d(TAG, "init Location: " + location.toString());
            Log.d(TAG, "initM: " + coordinateMap.toString());
        } catch (JsonSyntaxException e) {
            // TODO: 2018/10/29 提示配置文件出错
            Log.e(TAG, "locations.cfg 配置出错");
        }


    }


    //判断地点是否存在
    public boolean isLocationExits(String name) {
        if (coordinateMap == null) {
            return false;
        }
        return coordinateMap.containsKey(name);
    }

    //获得配置里的所有地点以及其他信息
    public List<LocationEntity> getLocationLists() {
        if (locationLists == null) {
            locationLists = new ArrayList<>();
        }
        return locationLists;
    }

    public void setLocationLists(List<LocationEntity> locationLists) {
        this.locationLists = locationLists;
    }

    //获得地点名和坐标的映射
    public ArrayMap<String, LocationEntity.CoordinateBean> getCoordinateMap() {
        if (coordinateMap == null) {
            coordinateMap = new ArrayMap<>();
        }
        return coordinateMap;
    }

    public void setCoordinateMap(ArrayMap<String, LocationEntity.CoordinateBean> coordinateMap) {
        this.coordinateMap = coordinateMap;
    }


}
