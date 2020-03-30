package demo.doctor.com.doctor.wenzhen;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class yaopinActivity {
    private static  String url = "http://192.168.0.105:8080/";
   static  List<String> list = new ArrayList();
    static String response;
    public static String getyaopininfo(final String result) {

        Thread t1=new Thread(new Runnable() {
            @Override
            public void run() {
                //记得修改IP地址
                String path = url + "yaopin?result=" + result;
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
                        response=result;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            t1.start();
            t1.join();
            return response;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static List<String> parseWithJSON(String response) {
        try {
            JSONArray obj = new JSONArray(response);
            for(int i=0;i<obj.length();i++){
                JSONObject jsonobj = (JSONObject)obj.get(i);
                String medicine = jsonobj.getString("medicine");
                String link = jsonobj.getString("link");
                String instruction = jsonobj.getString("instruction");
                String function = jsonobj.getString("function");
                String content="推荐药品： "+medicine+"\r\n"+"药品购买链接： "+link+"\r\n"+"使用说明： "+instruction+"\r\n"
                        +"功能主治："+function;
                list.add(content);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
