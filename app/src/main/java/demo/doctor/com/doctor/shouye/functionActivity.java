package demo.doctor.com.doctor.shouye;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import demo.doctor.com.doctor.R;


/**
 * @author xiaojiu
 */
public class functionActivity extends BaseFragmentActivity implements
        OnClickListener {

    private static final String TAG = functionActivity.class.getSimpleName();
    public static final int INDEX_HOMEPAGE = 0;// 首页
    public static final int INDEX_ME = 3; // 我的

    private daohangFrament homepageFragment; // 主页
    private setFragment meFragment; // 我的

    private View mHpLayout;
    private View mMeLayout;

    private ImageView mHpImage;
    private ImageView mMeImage;

    private TextView hpText;
    private TextView meText;

    private int currentTabIndex = INDEX_HOMEPAGE;
     static String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);
        // 初始化布局
        initView();
        int intExtra = getIntent().getIntExtra("INDEX_ME", 0);
        if (intExtra != 0) {
            setTabSelection(INDEX_ME);
        } else {
            // 默认为首页
            setTabSelection(INDEX_HOMEPAGE);
        }
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index 每个tab页对应的下标。0表示消息，1表示联系人，2表示动态，3表示设置。
     */
    public void setTabSelection(int index) {
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        switch (index) {
            case INDEX_HOMEPAGE:// 首页
                currentTabIndex = INDEX_HOMEPAGE;

                setTabBg(INDEX_HOMEPAGE);// 设置背景高亮
                // 主界面点击事件
                hideAllFragment();// 隐藏所有fragment
                if (homepageFragment == null) {
                    homepageFragment = new daohangFrament();
                    addFragment(R.id.content, homepageFragment);
                } else {
                    showFragment(homepageFragment);
                }
                break;
            case INDEX_ME:

                // if (needLogin(INDEX_ME)) {
                currentTabIndex = INDEX_ME;

                setTabBg(INDEX_ME);

                hideAllFragment();// 隐藏所有fragment
                if (meFragment == null) {
                    meFragment = new setFragment();
                    addFragment(R.id.content, meFragment);
                } else {
                    showFragment(meFragment);
                }
                // }
                break;
        }
    }

    private void setTabBg(int index) {
        switch (index) {
            case INDEX_HOMEPAGE:
                mHpImage.setBackgroundResource(R.drawable.home_m);
                hpText.setTextColor(getResources().getColor(
                        R.color.fragment_highlight));
                // mMeLayout
                // .setBackgroundColor(getResources().getColor(R.color.white));
                break;

            case INDEX_ME:
                mMeImage.setBackgroundResource(R.drawable.me_m);
                meText.setTextColor(getResources().getColor(
                        R.color.fragment_highlight));
                // mLocLayout.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            default:
                break;
        }
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        mHpImage.setBackgroundResource(R.drawable.home_n);
        hpText.setTextColor(getResources().getColor(R.color.fragment_text_grey));
        mHpLayout.setBackgroundColor(getResources().getColor(
                R.color.fragment_background));

        mMeImage.setBackgroundResource(R.drawable.me_n);
        meText.setTextColor(getResources().getColor(R.color.fragment_text_grey));
        mMeLayout.setBackgroundColor(getResources().getColor(
                R.color.fragment_background));
    }

    /**
     * 在这里获取到每个需要用到的控件的实例，并给它们设置好必要的点击事件。
     */
    private void initView() {
        mHpLayout = findViewById(R.id.homepage_rlayout);
        mMeLayout = findViewById(R.id.me_rlayout);

        mHpImage = (ImageView) findViewById(R.id.homepage_image);
        mMeImage = (ImageView) findViewById(R.id.me_image);

        hpText = (TextView) findViewById(R.id.homepage_text);
        meText = (TextView) findViewById(R.id.me_text);

        mHpLayout.setOnClickListener(this);
        mMeLayout.setOnClickListener(this);
    }

    /**
     * 隐藏所有fragment
     */
    private void hideAllFragment() {
        if (homepageFragment != null) {
            hideFragment(homepageFragment);
        }
        if (meFragment != null) {
            hideFragment(meFragment);
        }
    }

    // 当前按钮没被点击
    boolean flag = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homepage_rlayout:// 主页:
                setTabSelection(INDEX_HOMEPAGE);
                flag = false;

                break;
            case R.id.me_rlayout:
                setTabSelection(INDEX_ME);
                flag = false;
                break;
            default:
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//        super.onSaveInstanceState(outState, outPersistentState);//祛除保存信息
    }
    public static String getname(){
        return name;
    }
}
