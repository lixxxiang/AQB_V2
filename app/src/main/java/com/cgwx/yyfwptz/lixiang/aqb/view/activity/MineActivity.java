package com.cgwx.yyfwptz.lixiang.aqb.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import com.cgwx.yyfwptz.lixiang.aqb.R;
import com.githang.statusbar.StatusBarCompat;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MineActivity extends AppCompatActivity {

    @BindView(R.id.tel) TextView tel;
    @BindView(R.id.logout) Button logout;
    private Toolbar toolbar;
    private String userTel;
    private String string;
    public static MineActivity ma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mine);
        ButterKnife.bind(this);
        ma = this;
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#FFFFFF"));


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(MineActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                MainActivity.ma.finish();
                VCodeActivity.va.finish();
                LoginActivity.la.finish();
            }
        });
        Intent intent = getIntent();
        if (intent != null) {
            userTel = intent.getStringExtra("userTel");
            Log.e("userTel", userTel);
            String pre = userTel.substring(0, 3);
            String post = userTel.substring(7, 11);
            string = pre + "****" + post;
            Log.e("ore", pre + post);
        }
        tel.setText(string);
        initToolbar(R.id.toolbar, R.id.toolbar_title, "安全宝");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MineActivity.this.finish();
            }
        });
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
        return toolbar;
    }
}
