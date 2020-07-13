package com.ctfww.module.main.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.ctfww.commonlib.base.BaseActivity;
import com.ctfww.module.main.R;

@Route(path = "/main/splash")
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_splash);

        Button button = findViewById(R.id.btn_test_router);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build("/user/login").navigation();
            }
        });


    }
}
