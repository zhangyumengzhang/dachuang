package demo.doctor.com.doctor.login;

import android.content.Intent;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.view.View;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import demo.doctor.com.doctor.R;


public class ForgetPwdActivity extends AppCompatActivity implements View.OnClickListener{

    public EventHandler eh; //事件接收器
    private TimeCount Count;//计时器
    private EditText et_retrieve_tel,et_retrieve_code_input;
    private TextView retrieve_sms_call;
    private String name;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_retrieve_pwd);
        Count = new TimeCount(60000, 1000);
        et_retrieve_tel=findViewById(R.id.et_retrieve_tel);
        et_retrieve_code_input=findViewById(R.id.et_retrieve_code_input);
        retrieve_sms_call=findViewById(R.id.retrieve_sms_call);
        retrieve_sms_call.setOnClickListener(this);
        findViewById(R.id.ib_navigation_back).setOnClickListener(this);
        findViewById(R.id.bt_retrieve_submit).setOnClickListener(this);
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

                        Intent intent = new Intent(ForgetPwdActivity.this,resetpwd.class);
                        intent.putExtra("name",name);
                        startActivity(intent);

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
            case R.id.retrieve_sms_call:

                if(!et_retrieve_tel.getText().toString().trim().equals("")){
                    if (checkTel(et_retrieve_tel.getText().toString().trim())) {
                        SMSSDK.getVerificationCode("+86",et_retrieve_tel.getText().toString());//获取验证码
                        Count.start();
                    }else{
                        Toast.makeText(ForgetPwdActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ForgetPwdActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.bt_retrieve_submit:
                if (!et_retrieve_tel.getText().toString().trim().equals("")) {
                    if (checkTel(et_retrieve_tel.getText().toString().trim())) {
                        if (!et_retrieve_code_input.getText().toString().trim().equals("")) {
                            SMSSDK.submitVerificationCode("+86",et_retrieve_tel.getText().toString().trim(),et_retrieve_code_input.getText().toString().trim());//提交验证
                        }else{
                            Toast.makeText(ForgetPwdActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(ForgetPwdActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ForgetPwdActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                }
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
            retrieve_sms_call.setClickable(false);
            retrieve_sms_call.setText(l/1000 + "秒后重新获取");
        }
        @Override
        public void onFinish() {
            retrieve_sms_call.setClickable(true);
            retrieve_sms_call.setText("获取验证码");
        }
    }
}