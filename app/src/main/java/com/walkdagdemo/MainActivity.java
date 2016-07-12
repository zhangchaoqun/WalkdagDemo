package com.walkdagdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.wilddog.client.DataSnapshot;
import com.wilddog.client.ValueEventListener;
import com.wilddog.client.Wilddog;
import com.wilddog.client.WilddogError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 实时上传坐标经纬度
 */
public class MainActivity extends Activity implements AMapLocationListener {
    //定位先关
    public AMapLocationClientOption mLocationOption = null;
    private AMapLocationClient mLocationClient = null;
    //野狗相关
    private Wilddog ref = null;
    //经度和纬度
    private double dLatitude, dLongitude;

    //把数据加到集合中
    List<CarPosition> carPositions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationAbout();
        ref = new Wilddog("https://zcq-123.wilddogio.com/test1");
//        carPositions.add(new CarPosition("1", "117.124715", "31.830059"));
//        carPositions.add(new CarPosition("1", "117.125874", "31.82995"));
//        carPositions.add(new CarPosition("1", "117.128234","31.82995"));
//        carPositions.add(new CarPosition("1", "117.12935","31.830059"));
//        carPositions.add(new CarPosition("1", "117.130852","31.82995"));
//        carPositions.add(new CarPosition("1", "117.133985", "31.830059"));
//        carPositions.add(new CarPosition("1", "117.134414", "31.830351"));
//        carPositions.add(new CarPosition("1", "117.134414", "31.830934"));
//        carPositions.add(new CarPosition("1", "117.134328", "31.831736"));

//        carPositions.add(new CarPosition("1", "117.134371", "31.833231"));
//        carPositions.add(new CarPosition("1", "117.134307", "31.835309"));
//        carPositions.add(new CarPosition("1", "117.133899", "31.835127"));
//        uploadDate();
//        getDateFromWilddog();

    }

    public void uploadDate() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (final CarPosition cp : carPositions) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    ref.push().setValue(new CarPosition("1", cp.getcLong(), cp.getcLat()));
                    Log.e("data", "上传成功！！！");

                }
            }
        }).start();
    }


    //region  获得添加的数据
    public void getDateFromWilddog() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot d = null;
                try {
                    //获取子节点下的迭代数据
                    Iterator i = dataSnapshot.getChildren().iterator();
                    while (i.hasNext()) {
                        //通过循环获取子节点下的数据
                        d = (DataSnapshot) i.next();
                        //把数据转换成Json格式
                        JSONObject json = new JSONObject();
                        json.put(d.getKey(), new JSONObject(d.getValue().toString()));
                        //解析数据
                        JSONObject jObj = json.getJSONObject(d.getKey());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(WilddogError wilddogError) {
                if (wilddogError != null) {
                    Log.e("WilddogError", wilddogError.getCode() + "");
                }
            }
        });
    }
    //endregion

    //region 定位监听相关
    public void locationAbout() {
        mLocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mLocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mLocationClient.startLocation();
    }
    //endregion


    /**
     * 定位回调结果
     *
     * @param amapLocation 回调结果
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //执行判断若经度或纬度有一个发生变化则上传数据，否则不上传
                if (dLatitude != amapLocation.getLatitude() && dLongitude != amapLocation.getLongitude()) {
//                    //定位成功回调信息，设置相关消息
                    String mLatitude = String.valueOf(amapLocation.getLatitude());//获取纬度
                    String mLongitude = String.valueOf(amapLocation.getLongitude());//获取经度
                    CarPosition cp = new CarPosition("1", mLongitude, mLatitude);
//                    CarPosition cp = new CarPosition("1","117.24403381","31.7766288");
//                    CarPosition cp = new CarPosition("1", "117.26137161", "31.74700014");
                    ref.push().setValue(cp);
                }
                dLatitude = amapLocation.getLatitude();
                dLongitude = amapLocation.getLongitude();


            }
        } else {
            //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
            Log.e("AmapError", "location Error, ErrCode:"
                    + amapLocation.getErrorCode() + ", errInfo:"
                    + amapLocation.getErrorInfo());
        }
    }
}


