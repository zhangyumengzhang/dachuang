package demo.doctor.com.doctor.login;

import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import demo.doctor.com.doctor.R;

public class resetpwd extends AppCompatActivity implements View.OnClickListener{
    String name,password;
    EditText edpwd,etname;
    private String url="http://192.168.43.177:8080/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_reset_pwd);
        edpwd=findViewById(R.id.et_reset_pwd);
        etname=findViewById(R.id.et_reset_username);
        etname.setText(name);
        findViewById(R.id.ib_navigation_back).setOnClickListener(this);
        findViewById(R.id.bt_reset_submit).setOnClickListener(this);
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
}
