package com.cgwx.yyfwptz.lixiang.aqb.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.cgwx.yyfwptz.lixiang.aqb.R;
import com.cgwx.yyfwptz.lixiang.aqb.di.components.DaggerMainComponent;
import com.cgwx.yyfwptz.lixiang.aqb.di.modules.MainModule;
import com.cgwx.yyfwptz.lixiang.aqb.presenter.MainContract;
import com.cgwx.yyfwptz.lixiang.aqb.presenter.MainPresenter;
import com.cgwx.yyfwptz.lixiang.aqb.util.Constants;
import com.cgwx.yyfwptz.lixiang.aqb.model.entity.WrongMessage;
import com.cgwx.yyfwptz.lixiang.aqb.model.entity.addAlarm;
import com.cgwx.yyfwptz.lixiang.aqb.util.TimerUtils;
import com.githang.statusbar.StatusBarCompat;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    @BindView(R.id.bmapView)
    MapView mMapView;
    @BindView(R.id.iconInfo)
    TextView iconinfo;
    @BindView(R.id.call110)
    Button call_110;
    @BindView(R.id.btnRight)
    TextView done;
    @BindView(R.id.cpimage)
    ImageView cpimage;
    @BindView(R.id.arrange)
    View arrangeview;
    @BindView(R.id.complete)
    View completeview;
    @BindView(R.id.cpview)
    View callpoliceview;
    @BindView(R.id.failed)
    View failureview;

    public MyLocationListenner myListener = new MyLocationListenner();
    private LocationClient mLocClient;
    private BaiduMap mBaiduMap;
    private Toolbar toolbar;
    @SuppressLint("StaticFieldLeak")
    public static MainActivity ma;
    private String userTel;
    private String userId;
    private String alarmID;
    private long exitTime = 0;
    private Boolean isSucceed;
    private Timer timer;
    private int count = 0;
    private boolean isFirstLoc = true;


    @Inject
    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ma = this;
        DaggerMainComponent.builder().mainModule(new MainModule(this))
                .build()
                .inject(this);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#FFFFFF"));


        call_110.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:10086"));
                startActivity(intent);
            }
        });

        final Intent intent = getIntent();

        if (intent != null) {
            userTel = intent.getStringExtra("userTel");
            userId = intent.getStringExtra("userId");
        }

        initToolbar(R.id.toolbar, R.id.toolbar_title, "安全宝");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MineActivity.class);
                intent.putExtra("userTel", userTel);
                startActivity(intent);

            }
        });


        /**
         * call police;
         */
        cpimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.addAlarm(userId, userTel, myListener.longi, myListener.lati, myListener.poi.get(0).getName(), myListener.address);
                count = 0;
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        }

        MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.location);
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker));
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }

        mMapView.showScaleControl(false);
        mMapView.showZoomControls(false);
        UiSettings mUiSettings = mBaiduMap.getUiSettings();
        mUiSettings.setScrollGesturesEnabled(false);
        mUiSettings.setOverlookingGesturesEnabled(false);
        mUiSettings.setZoomGesturesEnabled(false);
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);
        option.setAddrType("all");
        option.setIsNeedLocationPoiList(true);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            roll();
            count++;
            if (count > 300) {
                if (timer != null)
                    timer.cancel();
                timer = new Timer();

                callpoliceview.setVisibility(View.INVISIBLE);
                arrangeview.setVisibility(View.INVISIBLE);
                failureview.setVisibility(View.VISIBLE);
                iconinfo.setText("报警失败");
                done.setVisibility(View.VISIBLE);
                isSucceed = false;
            }
        }
    };


    public void roll() {
        presenter.isAlarmAccepted();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

    }

    @Override
    public void addAlarmDone(String aid) {
        alarmID = aid;
        callpoliceview.setVisibility(View.INVISIBLE);
        arrangeview.setVisibility(View.VISIBLE);
        iconinfo.setText("正在安排警力");

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        }, 1000, 1000);
    }

    @Override
    public void alarmAccepted() {
        arrangeview.setVisibility(View.INVISIBLE);
        completeview.setVisibility(View.VISIBLE);
        iconinfo.setText("报警成功");
        if (timer != null)
            timer.cancel();
        done.setVisibility(View.VISIBLE);
        isSucceed = true;
    }

    @Override
    public void noPolice() {
        if (timer != null)
            timer.cancel();
        callpoliceview.setVisibility(View.INVISIBLE);
        arrangeview.setVisibility(View.INVISIBLE);
        failureview.setVisibility(View.VISIBLE);
        iconinfo.setText("报警失败");
        done.setVisibility(View.VISIBLE);
        isSucceed = false;
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {
        public double lati;
        public double longi;
        public String address;
        List<Poi> poi;

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mMapView == null) {
                return;
            }

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(0)
                    .direction(0).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            lati = location.getLatitude();
            longi = location.getLongitude();
            address = location.getAddrStr();
            poi = location.getPoiList();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }

    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mLocClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    @SuppressLint("ResourceAsColor")
    public Toolbar initToolbar(int id, int titleId, String titleString) {
        toolbar = (Toolbar) findViewById(id);
        TextView textView = (TextView) findViewById(titleId);
        textView.setText(titleString);
        textView.setTextSize(17);
        textView.setTextColor(R.color.color5F);
        toolbar.setBackgroundColor(Color.WHITE);
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        setToolbarRight("完成", null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("sdfs", "提交操作");
                iconinfo.setText("我当前的位置");
                if (!isSucceed) {
                    failureview.setVisibility(View.INVISIBLE);
                    callpoliceview.setVisibility(View.VISIBLE);
                    done.setVisibility(View.INVISIBLE);

                } else {
                    completeview.setVisibility(View.INVISIBLE);
                    callpoliceview.setVisibility(View.VISIBLE);
                    done.setVisibility(View.INVISIBLE);

                }
            }
        });
        return toolbar;
    }

    protected void setToolbarRight(String text, @Nullable Integer icon, View.OnClickListener btnClick) {
        if (text != null) {
            done.setText(text);
        }
        done.setOnClickListener(btnClick);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序\n您的案情将报警失败!", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                if (timer != null)
                    timer.cancel();
                isSucceed = false;
                finish();
                if (LoginActivity.la != null)
                    LoginActivity.la.finish();
                if (VCodeActivity.va != null)
                    VCodeActivity.va.finish();
                if (MineActivity.ma != null)
                    MineActivity.ma.finish();
                System.exit(0);
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
