package com.aidrive.aidriveconcept.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.aidrive.aidriveconcept.MainActivity;
import com.aidrive.aidriveconcept.R;
import com.aidrive.aidriveconcept.app.AiDriveApp;
import com.softradix.core.AppPreferences;
import com.softradix.core.Constants;

public class LauncherActivity extends AppCompatActivity {
    static {
        System.loadLibrary("gstreamer_android");
        System.loadLibrary("flyover");
//        nativeClassInit();
    }
    public static int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launcher);
        getSupportActionBar().hide();
        if(!AiDriveApp.getAppPreferences().getString(Constants.USER_DATA).equals("")){
            startActivity(new Intent(LauncherActivity.this, MainActivity.class));
            finishAffinity();
        }

    }
}