package demo.doctor.com.doctor.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import demo.doctor.com.doctor.R;

public class RegistertwoActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    TextView female, man;
    String gender, name, password, age;
    EditText etname, etpassword, etage;
    private Button bt_register_submit;
    private String url = "http://192.168.0.101:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register_step_two);
        findViewById(R.id.ib_navigation_back).setOnClickListener(this);
        female = findViewById(R.id.tv_register_female);
        female.setOnClickListener(this);
        man = findViewById(R.id.tv_register_man);
        man.setOnClickListener(this);
        etname = findViewById(R.id.et_register_username);
        etname.setOnClickListener(this);
        etpassword = findViewById(R.id.et_register_pwd_input);
        etpassword.setOnClickListener(this);
        etage = findViewById(R.id.et_register_age_input);
        etage.setOnClickListener(this);
        findViewById(R.id.bt_register_submit).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register_female:
                if (female.isSelected()) {

                    female.setSelected(false);

                } else {

                    female.setSelected(true);
                    gender = "female";
                }
                break;
            case R.id.tv_register_man:
                if (man.isSelected()) {

                    man.setSelected(false);

                } else {

                    man.setSelected(true);
                    gender = "man";
                }
                break;
            case R.id.ib_navigation_back:

                finish();

                break;
            case R.id.bt_register_submit:

                name = etname.getText().toString();
                password = etpassword.getText().toString();
                age = etage.getText().toString();
                register();
                break;
        }
    }

    private void register() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //记得修改IP地址
                String path = url + "register?name=" + name + "&password=" + password + "&gender=" + gender + "&age=" + age;
                try {
                    try {
                        URL url = new URL(path); //新建url并实例化
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");//获取服务器数据
                        connection.setReadTimeout(8000);//设置读取超时的毫秒数
                        connection.setConnectTimeout(8000);//设置连接超时的毫秒数
                        InputStream in = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        String result = reader.readLine();//读取服务器进行逻辑处理后页面显示的数据
                        Log.d("MainActivity", "run: " + result);
                        if (result.equals("注册成功")) {
                            Looper.prepare();
                            AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(RegistertwoActivity.this);
                            alertdialogbuilder.setMessage("注册成功！");
                            // alertdialogbuilder.setPositiveButton("确定", null);
                            alertdialogbuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(RegistertwoActivity.this, LoginActivity.class);
                                    startActivity(i);
                                }
                            });
                            final AlertDialog alertdialog1 = alertdialogbuilder.create();
                            alertdialog1.show();
                            Looper.loop();
                        }
                    } catch (MalformedURLException e) {
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override

    public void afterTextChanged(Editable s) {

        String username = etname.getText().toString().trim();

        String pwd = etpassword.getText().toString().trim();

        String age = etage.getText().toString().trim();
        //登录按钮是否可用

        if (!TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(age)) {

            bt_register_submit.setBackgroundResource(R.drawable.bg_login_submit);

            bt_register_submit.setTextColor(getResources().getColor(R.color.white));

        } else {

            bt_register_submit.setBackgroundResource(R.drawable.bg_login_submit_lock);

            bt_register_submit.setTextColor(getResources().getColor(R.color.account_lock_font_color));

        }

    }
}