package demo.doctor.com.doctor.login;

import android.annotation.SuppressLint;
import android.app.Application;

import cn.smssdk.SMSSDK;


public class MobApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SMSSDK.initSDK(this, "2df31c73508b4", "9672e023046e3bed079faeecbc294a49");
    }
}
