package demo.doctor.com.doctor.shouye;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.umeng.analytics.MobclickAgent;

/**
 * @author xiaojiu
 * @date 2016-4-1
 * @version V1.0
 * @Description:
 */
public class BaseFragmentActivity extends FragmentActivity {
    private static final String TAG = BaseFragmentActivity.class.getSimpleName();

    public BaseFragmentActivity() {
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
    }

    protected void hideFragment(Fragment fragment) {
        try {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.hide(fragment);
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
//			Logger.e(TAG, "hideFragment", e);
        }
    }

    protected void addFragment(int layoutId, Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(layoutId, fragment);
        transaction.commitAllowingStateLoss();
    }

    protected void addFragment(int layoutId, Fragment fragment) {
        addFragment(layoutId, fragment, false);
    }

    protected void showFragment(Fragment fragment) {
        try {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.show(fragment);
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
//			Logger.e(TAG, "showFragment", e);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SplashScreen");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SplashScreen");
        MobclickAgent.onPause(this);
    }

    /**
     * 控制系统显示不对app有影响
     * @return
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }
}

