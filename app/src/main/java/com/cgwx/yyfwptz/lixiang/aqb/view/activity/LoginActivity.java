package com.cgwx.yyfwptz.lixiang.aqb.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import com.cgwx.yyfwptz.lixiang.aqb.di.components.DaggerLoginComponent;
import com.cgwx.yyfwptz.lixiang.aqb.presenter.LoginContract;
import com.cgwx.yyfwptz.lixiang.aqb.di.modules.LoginModule;
import com.cgwx.yyfwptz.lixiang.aqb.presenter.LoginPresenter;
import com.cgwx.yyfwptz.lixiang.aqb.R;
import com.githang.statusbar.StatusBarCompat;

import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    @BindView(R.id.getVcode) Button getVcode;
    @BindView(R.id.tel) EditText tel;


    @Inject
    LoginPresenter presenter;

    String userTel;
    public static LoginActivity la;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#FFFFFF"));
        ButterKnife.bind(this);
        DaggerLoginComponent.builder().loginModule(new LoginModule(this))
                .build()
                .inject(this);


        la = this;
        SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
        if(sp.getString("userTel", null) != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("userTel", sp.getString("userTel", null));
            intent.putExtra("userId", sp.getString("userId", null));
            startActivity(intent);
            finish();
        }

        tel.setInputType(EditorInfo.TYPE_CLASS_NUMBER);

        getVcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userTel = tel.getText().toString();
                presenter.sendMessage(userTel);
            }
        });

        tel.addTextChangedListener(mTextWatcher);


    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            getVcode.setBackgroundResource(R.drawable.orabtn);
            if (tel.getText().length() == 0) {
                getVcode.setBackgroundResource(R.drawable.gray);
            }
        }
    };

    @Override
    public void getVCodeSuccess(String tel) {
        Intent intent = new Intent(LoginActivity.this, VCodeActivity.class);
        intent.putExtra("userTel", tel);
        startActivity(intent);
    }
}
