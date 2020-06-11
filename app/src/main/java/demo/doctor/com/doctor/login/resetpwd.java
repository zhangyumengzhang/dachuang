package demo.doctor.com.doctor.login;

import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import demo.doctor.com.doctor.R;

public class resetpwd extends AppCompatActivity implements View.OnClickListener, TextWatcher, View.OnFocusChangeListener {
    String name,password;
    EditText edpwd,etname;
    Button bt_reset_submit;
    private String url="http://123.56.175.138:8080/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_reset_pwd);
        edpwd=findViewById(R.id.et_reset_pwd);
        etname=findViewById(R.id.et_reset_username);
        etname.setText(name);
        bt_reset_submit=findViewById(R.id.bt_reset_submit);
        findViewById(R.id.ib_navigation_back).setOnClickListener(this);
        findViewById(R.id.bt_reset_submit).setOnClickListener(this);


        etname.addTextChangedListener(this);

        edpwd.addTextChangedListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_reset_submit:
                password=edpwd.getText().toString();
                update();
                break;
            case R.id.ib_navigation_back:

                finish();

                break;
        }
    }
    private void update() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //记得修改IP地址
                String path=url+"update?name="+name+"&password="+password;
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
                        if (result.equals("修改成功")){
                            Looper.prepare();
                            AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(resetpwd.this);
                            alertdialogbuilder.setMessage("密码修改成功！");
                            // alertdialogbuilder.setPositiveButton("确定", null);
                            alertdialogbuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                @Override

                                public void onClick(DialogInterface dialog, int which) {

                                    Intent i=new Intent(resetpwd.this, LoginActivity.class);
                                    startActivity(i);
                                }

                            });
                            final AlertDialog alertdialog1 = alertdialogbuilder.create();
                            alertdialog1.show();
                            Looper.loop();
                        }if (result.equals("用户名不存在")){
                            Looper.prepare();
                            AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(resetpwd.this);
                            alertdialogbuilder.setMessage("此用户名不存在！");
                            // alertdialogbuilder.setPositiveButton("确定", null);
                            alertdialogbuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                @Override

                                public void onClick(DialogInterface dialog, int which) {

                                }

                            });
                            final AlertDialog alertdialog1 = alertdialogbuilder.create();
                            alertdialog1.show();
                            Looper.loop();
                        }
                    }catch (MalformedURLException e){}
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

        String pwd = edpwd.getText().toString().trim();

        //登录按钮是否可用

        if (!TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(username)) {

            bt_reset_submit.setBackgroundResource(R.drawable.bg_login_submit);

            bt_reset_submit.setTextColor(getResources().getColor(R.color.white));

        } else {

            bt_reset_submit.setBackgroundResource(R.drawable.bg_login_submit_lock);

            bt_reset_submit.setTextColor(getResources().getColor(R.color.account_lock_font_color));

        }

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }
}
