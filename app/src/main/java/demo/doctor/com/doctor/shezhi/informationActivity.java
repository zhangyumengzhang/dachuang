package demo.doctor.com.doctor.shezhi;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import demo.doctor.com.doctor.R;
import demo.doctor.com.doctor.login.LoginActivity;
import demo.doctor.com.doctor.wenzhen.MainActivity;

public class informationActivity extends AppCompatActivity {
    String name,sgender,sage;
    TextView uname,gender,age;
    Button exit;
    private static String url = "http://123.56.175.138:8080/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent=getIntent();
        name=intent.getStringExtra("name");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informatiin);
        uname=findViewById(R.id.unamejuti);
        gender=findViewById(R.id.sexjuti);
        age=findViewById(R.id.agejuti);
        exit=findViewById(R.id.exit);

        startThread();
        uname.setText(name);
        gender.setText(sgender);
        age.setText(sage);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(informationActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    public  void startThread() {
        Thread t1=new Thread(new Runnable() {
            @Override
            public void run() {
                //记得修改IP地址
                String path = url + "getinformation?name=" + name;
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
                    if (result != null) {
                        parseWithJSON(result);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            t1.start();
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected  void parseWithJSON(String response) {
        try {
            JSONObject jsonObject1 = new JSONObject(response);
            sgender = jsonObject1.getString("gender");
            sage = jsonObject1.getString("age");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
