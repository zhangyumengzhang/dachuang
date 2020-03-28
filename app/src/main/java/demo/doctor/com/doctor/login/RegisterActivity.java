package demo.doctor.com.doctor.login;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v7.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import demo.doctor.com.doctor.R;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    public EventHandler eh; //事件接收器
    private TimeCount mTimeCount;//计时器
    private EditText et_register_auth_code,et_register_username;
    private TextView tv_register_sms_call,tv_protocol;
    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_register_step_one);
        mTimeCount = new TimeCount(60000, 1000);
        et_register_auth_code=findViewById(R.id.et_register_auth_code);
        et_register_username=findViewById(R.id.et_register_username);
        tv_register_sms_call=findViewById(R.id.tv_register_sms_call);
        tv_protocol=findViewById(R.id.tv_protocol);
        findViewById(R.id.ib_navigation_back).setOnClickListener(this);
        findViewById(R.id.bt_register_submit).setOnClickListener(this);
        findViewById(R.id.tv_register_sms_call).setOnClickListener(this);
        init();
    }
    /**
     * 初始化事件接收器
     */
    private void init(){
        eh = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {

                if (result == SMSSDK.RESULT_COMPLETE) { //回调完成

                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) { //提交验证码成功

                        startActivity(new Intent(RegisterActivity.this, RegistertwoActivity.class)); //页面跳转

                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){ //获取验证码成功

                    } else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){ //返回支持发送验证码的国家列表

                    }
                } else{
                    ((Throwable)data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }

    @Override

    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.ib_navigation_back:

                finish();

                break;
            case R.id.tv_register_sms_call:

                if(!et_register_username.getText().toString().trim().equals("")){
                    if (checkTel(et_register_username.getText().toString().trim())) {
                        SMSSDK.getVerificationCode("+86",et_register_username.getText().toString());//获取验证码
                        mTimeCount.start();
                    }else{
                        Toast.makeText(RegisterActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(RegisterActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_register_submit:
                if (!et_register_username.getText().toString().trim().equals("")) {
                    if (checkTel(et_register_username.getText().toString().trim())) {
                        if (!et_register_auth_code.getText().toString().trim().equals("")) {
                            SMSSDK.submitVerificationCode("+86",et_register_username.getText().toString().trim(),et_register_auth_code.getText().toString().trim());//提交验证
                        }else{
                            Toast.makeText(RegisterActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(RegisterActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(RegisterActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_protocol:

                Intent i=new Intent(RegisterActivity.this, ProtocolActivity.class);

                startActivity(i);

                break;
        }
    }
    /**
     * 正则匹配手机号码
     * @param tel
     * @return
     */
    public boolean checkTel(String tel){
        Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
        Matcher matcher = p.matcher(tel);
        return matcher.matches();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);
    }
    /**
     * 计时器
     */
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            tv_register_sms_call.setClickable(false);
            tv_register_sms_call.setText(l/1000 + "秒后重新获取");
        }

        @Override
        public void onFinish() {
            tv_register_sms_call.setClickable(true);
            tv_register_sms_call.setText("获取验证码");
        }
    }
}