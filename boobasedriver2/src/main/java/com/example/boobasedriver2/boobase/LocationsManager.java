package com.example.boobasedriver2.boobase;

import android.content.Context;

import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;

import com.example.boobasedriver2.boobase.entity.LocationEntity;
import com.example.boobasedriver2.utils.FileUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;


/**
 * create by zzh on 2018/10/23
 */
public class LocationsManager {

    private String TAG = "TAG";
    private static LocationsManager mInstance;
    private List<LocationEntity> locationLists;
    private ArrayMap<String, LocationEntity.CoordinateBean> coordinateMap;  //名字对应坐标的Map
    private Context mContext;

    public static LocationsManager CreateInstance(Context context) {
        if (mInstance == null) {
            mInstance = new LocationsManager(context);
        }
        return mInstance;
    }

    public static LocationsManager getInstance() {
        return mInstance;
    }


    public LocationsManager(Context mContext) {
        this.mContext = mContext;
        initLocation(mContext);
    }

    //读取配置文件的位置信息
    private void initLocation(Context mContext) {
        String location = FileUtil.readAssetsFile(mContext, "cfg/locations.cfg");
        if (!TextUtils.isEmpty(location)) {
            List<LocationEntity> locationLists = new Gson().fromJson(location, new TypeToken<List<LocationEntity>>() {
            }.getType());
            this.setLocationLists(locationLists);
            ArrayMap<String, LocationEntity.CoordinateBean> coordinateMap = new ArrayMap<>();
            for (LocationEntity entity : locationLists) {
                coordinateMap.put(entity.getName(), entity.getCoordinate());
            }
            this.setCoordinateMap(coordinateMap);
            Log.d(TAG, "initLocation: " + location.toString());
            Log.d(TAG, "initM: " + coordinateMap.toString());
        }
    }


    //判断地点是否存在
    public boolean isLocationExits(String name) {
        return coordinateMap.containsKey(name);
    }

    //获得配置里的所有地点以及其他信息
    public List<LocationEntity> getLocationLists() {
        return locationLists;
    }

    public void setLocationLists(List<LocationEntity> locationLists) {
        this.locationLists = locationLists;
    }

    //获得地点名和坐标的映射
    public ArrayMap<String, LocationEntity.CoordinateBean> getCoordinateMap() {
        return coordinateMap;
    }

    public void setCoordinateMap(ArrayMap<String, LocationEntity.CoordinateBean> coordinateMap) {
        this.coordinateMap = coordinateMap;
    }


}
