package demo.doctor.com.doctor.login;

import android.animation.Animator;

import android.animation.AnimatorListenerAdapter;

import android.animation.ValueAnimator;

import android.content.Intent;

import android.graphics.Rect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import java.io.InputStreamReader;

import android.os.Bundle;

import android.text.Editable;

import android.text.TextUtils;

import android.text.TextWatcher;

import android.util.Log;
import android.view.View;

import android.view.ViewGroup;

import android.view.ViewTreeObserver;

import android.view.animation.DecelerateInterpolator;

import android.widget.Button;

import android.widget.EditText;

import android.widget.ImageButton;

import android.widget.ImageView;

import android.widget.LinearLayout;

import android.widget.TextView;

import android.widget.Toast;

import android.support.v7.app.AppCompatActivity;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import demo.doctor.com.doctor.shouye.functionActivity;
import demo.doctor.com.doctor.R;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, ViewTreeObserver.OnGlobalLayoutListener, TextWatcher {


    /**
     * BaseURL需要改成你们要访问的ip的地址。
     * 如果是在模拟器上使用，要写10.0.2.2,写localhost会被认为是模拟器本身。
     * 如果是在手机上使用，手机和电脑要在同一局域网，ip地址使用电脑的ipv4，可以在命令行输出ipconfig查看。
     */


    private String url="http://192.168.0.105:8080/";

    private String TAG = "ifu25";



    private ImageButton mIbNavigationBack;

    private LinearLayout mLlLoginPull;

    private View mLlLoginLayer;

    private LinearLayout mLlLoginOptions;

    private EditText mEtLoginUsername;

    private EditText mEtLoginPwd;

    private LinearLayout mLlLoginUsername;

    private ImageView mIvLoginUsernameDel;

    private Button mBtLoginSubmit;

    private LinearLayout mLlLoginPwd;

    private ImageView mIvLoginPwdDel;

    private ImageView mIvLoginLogo;

    private LinearLayout mLayBackBar;

    private TextView mTvLoginForgetPwd;

    private Button mBtLoginRegister;

    public static String nam,password;

    //全局Toast

    private Toast mToast;



    private int mLogoHeight;

    private int mLogoWidth;



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_login);

        initView();

    }



    //初始化视图

    private void initView() {

        //登录层、下拉层、其它登录方式层

        //导航栏+返回按钮

        mLayBackBar = findViewById(R.id.ly_retrieve_bar);

        mIbNavigationBack = findViewById(R.id.ib_navigation_back);



        //logo

        mIvLoginLogo = findViewById(R.id.iv_login_logo);


        //username

        mLlLoginUsername = findViewById(R.id.ll_login_username);

        mEtLoginUsername = findViewById(R.id.et_login_username);

        mIvLoginUsernameDel = findViewById(R.id.iv_login_username_del);


        //passwd

        mLlLoginPwd = findViewById(R.id.ll_login_pwd);

        mEtLoginPwd = findViewById(R.id.et_login_pwd);

        mIvLoginPwdDel = findViewById(R.id.iv_login_pwd_del);



        //提交、注册

        mBtLoginSubmit = findViewById(R.id.bt_login_submit);

        mBtLoginRegister = findViewById(R.id.bt_login_register);



        //忘记密码

        mTvLoginForgetPwd = findViewById(R.id.tv_login_forget_pwd);

        mTvLoginForgetPwd.setOnClickListener(this);



        //注册点击事件


        mIbNavigationBack.setOnClickListener(this);

        mEtLoginUsername.setOnClickListener(this);

        mIvLoginUsernameDel.setOnClickListener(this);

        mBtLoginSubmit.setOnClickListener(this);

        mBtLoginRegister.setOnClickListener(this);

        mEtLoginPwd.setOnClickListener(this);

        mIvLoginPwdDel.setOnClickListener(this);





        //注册其它事件

        mLayBackBar.getViewTreeObserver().addOnGlobalLayoutListener(this);

        mEtLoginUsername.setOnFocusChangeListener(this);

        mEtLoginUsername.addTextChangedListener(this);

        mEtLoginPwd.setOnFocusChangeListener(this);

        mEtLoginPwd.addTextChangedListener(this);

    }



    @Override

    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.ib_navigation_back:

                //返回

                finish();

                break;

            case R.id.et_login_username:

                mEtLoginPwd.clearFocus();

                mEtLoginUsername.setFocusableInTouchMode(true);

                mEtLoginUsername.requestFocus();

                break;

            case R.id.et_login_pwd:

                mEtLoginUsername.clearFocus();

                mEtLoginPwd.setFocusableInTouchMode(true);

                mEtLoginPwd.requestFocus();

                break;

            case R.id.iv_login_username_del:

                //清空用户名

                mEtLoginUsername.setText(null);

                break;

            case R.id.iv_login_pwd_del:

                //清空密码

                mEtLoginPwd.setText(null);

                break;

            case R.id.bt_login_submit:

                //登录
                nam=mEtLoginUsername.getText().toString();
                password=mEtLoginPwd.getText().toString();
                if(mEtLoginUsername!=null&&mEtLoginPwd!=null){
                    login();
                } else{
                    Toast.makeText(LoginActivity.this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.bt_login_register:

                //注册
                Intent i=new Intent(LoginActivity.this, RegisterActivity.class);

                startActivity(i);

                break;

            case R.id.tv_login_forget_pwd:

                //忘记密码
                if(mEtLoginUsername.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = new Intent(LoginActivity.this, ForgetPwdActivity.class);
                    intent.putExtra("name",mEtLoginUsername.getText().toString());
                    startActivity(intent);
                }

                break;

            default:

                break;

        }

    }

    private void login() {
        new Thread(new Runnable() {
        @Override
        public void run() {
            //记得修改IP地址
            String path=url+"login?name="+nam+"&password="+password;
            try {
                try{
                    URL url = new URL(path); //新建url并实例化
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");//获取服务器数据
                    connection.setReadTimeout(8000);//设置读取超时的毫秒数
                    connection.setConnectTimeout(8000);//设置连接超时的毫秒数
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String result = reader.readLine();//读取服务器进行逻辑处理后页面显示的数据
                    Log.d("MainActivity","run: "+result);
                    if (result.equals("登录成功")){
                        Intent i=new Intent(LoginActivity.this, functionActivity.class);
                        i.putExtra("name",mEtLoginUsername.getText().toString());
                        startActivity(i);
                    }
                }catch (MalformedURLException e){}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }).start();
}


    //用户名密码焦点改变

    @Override

    public void onFocusChange(View v, boolean hasFocus) {

        int id = v.getId();



        if (id == R.id.et_login_username) {

            if (hasFocus) {

                mLlLoginUsername.setActivated(true);

                mLlLoginPwd.setActivated(false);

            }

        } else {

            if (hasFocus) {

                mLlLoginPwd.setActivated(true);

                mLlLoginUsername.setActivated(false);

            }

        }

    }



    /**

     * menu glide

     *

     * @param height   height

     * @param progress progress

     * @param time     time

     */

    private void glide(int height, float progress, int time) {

        mLlLoginPull.animate()

                .translationYBy(height - height * progress)

                .translationY(height)

                .setDuration(time)

                .start();



        mLlLoginLayer.animate()

                .alphaBy(1 * progress)

                .alpha(0)

                .setDuration(time)

                .setListener(new AnimatorListenerAdapter() {



                    @Override

                    public void onAnimationCancel(Animator animation) {

                        if (animation instanceof ValueAnimator) {

                            mLlLoginLayer.setTag(((ValueAnimator) animation).getAnimatedValue());

                        }

                    }



                    @Override

                    public void onAnimationEnd(Animator animation) {

                        if (animation instanceof ValueAnimator) {

                            mLlLoginLayer.setTag(((ValueAnimator) animation).getAnimatedValue());

                        }

                        mLlLoginLayer.setVisibility(View.GONE);

                    }

                })

                .start();

    }



    /**

     * menu up glide

     *

     * @param height   height

     * @param progress progress

     * @param time     time

     */

    private void upGlide(int height, float progress, int time) {

        mLlLoginPull.animate()

                .translationYBy(height * progress)

                .translationY(0)

                .setDuration(time)

                .start();

        mLlLoginLayer.animate()

                .alphaBy(1 - progress)

                .alpha(1)

                .setDuration(time)

                .setListener(new AnimatorListenerAdapter() {

                    @Override

                    public void onAnimationStart(Animator animation) {

                        mLlLoginLayer.setVisibility(View.VISIBLE);

                    }



                    @Override

                    public void onAnimationCancel(Animator animation) {

                        if (animation instanceof ValueAnimator) {

                            mLlLoginLayer.setTag(((ValueAnimator) animation).getAnimatedValue());

                        }

                    }



                    @Override

                    public void onAnimationEnd(Animator animation) {

                        if (animation instanceof ValueAnimator) {

                            mLlLoginLayer.setTag(((ValueAnimator) animation).getAnimatedValue());

                        }

                    }

                })

                .start();

    }



    //显示或隐藏logo

    @Override

    public void onGlobalLayout() {

        final ImageView ivLogo = this.mIvLoginLogo;

        Rect KeypadRect = new Rect();



        mLayBackBar.getWindowVisibleDisplayFrame(KeypadRect);



        int screenHeight = mLayBackBar.getRootView().getHeight();

        int keypadHeight = screenHeight - KeypadRect.bottom;



        //隐藏logo

        if (keypadHeight > 300 && ivLogo.getTag() == null) {

            final int height = ivLogo.getHeight();

            final int width = ivLogo.getWidth();

            this.mLogoHeight = height;

            this.mLogoWidth = width;



            ivLogo.setTag(true);



            ValueAnimator valueAnimator = ValueAnimator.ofFloat(1, 0);

            valueAnimator.setDuration(400).setInterpolator(new DecelerateInterpolator());

            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override

                public void onAnimationUpdate(ValueAnimator animation) {

                    float animatedValue = (float) animation.getAnimatedValue();

                    ViewGroup.LayoutParams layoutParams = ivLogo.getLayoutParams();

                    layoutParams.height = (int) (height * animatedValue);

                    layoutParams.width = (int) (width * animatedValue);

                    ivLogo.requestLayout();

                    ivLogo.setAlpha(animatedValue);

                }

            });



            if (valueAnimator.isRunning()) {

                valueAnimator.cancel();

            }

            valueAnimator.start();

        }

        //显示logo

        else if (keypadHeight < 300 && ivLogo.getTag() != null) {

            final int height = mLogoHeight;

            final int width = mLogoWidth;



            ivLogo.setTag(null);



            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);

            valueAnimator.setDuration(400).setInterpolator(new DecelerateInterpolator());

            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override

                public void onAnimationUpdate(ValueAnimator animation) {

                    float animatedValue = (float) animation.getAnimatedValue();

                    ViewGroup.LayoutParams layoutParams = ivLogo.getLayoutParams();

                    layoutParams.height = (int) (height * animatedValue);

                    layoutParams.width = (int) (width * animatedValue);

                    ivLogo.requestLayout();

                    ivLogo.setAlpha(animatedValue);

                }

            });



            if (valueAnimator.isRunning()) {

                valueAnimator.cancel();

            }

            valueAnimator.start();

        }

    }



    @Override

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {



    }



    @Override

    public void onTextChanged(CharSequence s, int start, int before, int count) {



    }



    //用户名密码输入事件

    @Override

    public void afterTextChanged(Editable s) {

        String username = mEtLoginUsername.getText().toString().trim();

        String pwd = mEtLoginPwd.getText().toString().trim();



        //是否显示清除按钮

        if (username.length() > 0) {

            mIvLoginUsernameDel.setVisibility(View.VISIBLE);

        } else {

            mIvLoginUsernameDel.setVisibility(View.INVISIBLE);

        }

        if (pwd.length() > 0) {

            mIvLoginPwdDel.setVisibility(View.VISIBLE);

        } else {

            mIvLoginPwdDel.setVisibility(View.INVISIBLE);

        }



        //登录按钮是否可用

        if (!TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(username)) {

            mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit);

            mBtLoginSubmit.setTextColor(getResources().getColor(R.color.white));

        } else {

            mBtLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);

            mBtLoginSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));

        }

    }


        /**
         * 显示Toast
         *
         * @param msg 提示信息内容
         */

        private void showToast(int msg) {

            if (null != mToast) {

                mToast.setText(msg);

            } else {

                mToast = Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT);

            }
            mToast.show();

        }
}