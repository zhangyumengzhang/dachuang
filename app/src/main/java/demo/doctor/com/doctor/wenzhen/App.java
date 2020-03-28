package demo.doctor.com.doctor.wenzhen;

import android.app.Activity;
import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;


/**
 * Created by lv on 2019/3/26 for ClassCard
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID +"=5d2f3449");
    }

}
