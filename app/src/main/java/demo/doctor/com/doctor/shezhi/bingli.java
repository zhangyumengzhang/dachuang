package demo.doctor.com.doctor.shezhi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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

import demo.doctor.com.doctor.R;
import demo.doctor.com.doctor.wenzhen.MainActivity;

public class bingli extends Activity {
    private String url = "http://123.56.175.138:8080/";
    private ListView listView;
    String name;

    List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
    Map<String,Object> map = new HashMap<String, Object>();
    @Override
    public void onCreate(Bundle savedInstanceState)	{
        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bingli);
        listView= (ListView)this.findViewById(R.id.lv);
        getData();
    }
    private void getData() {
        Log.d("MainActivity", "run: " + "paoqilai");
        Thread t1=new Thread(new Runnable() {
            @Override
            public void run() {
                String path = url + "getall?name=" + name;
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
    public void parseWithJSON(String response) {
        try {
            JSONArray obj = new JSONArray(response);
            for(int i=0;i<obj.length();i++){
                JSONObject jsonobj = (JSONObject)obj.get(i);
                String token = jsonobj.getString("name");
                String time = jsonobj.getString("time");
                String result = jsonobj.getString("result");
                String content = jsonobj.getString("content");
                Log.d("MainActivity", "name=  " + token+ "   time=" + time + "\n");
                String[] chrstr = content.split("<br>");
                String allcontent="";
                for(int j=0;j<chrstr.length;j++) {
                    allcontent =allcontent+chrstr[j] + "\r\n";
                }
                allcontent =allcontent+"\r\n"+result;
                map = new HashMap<String, Object>();
                map.put("name", token);
                map.put("time", time);
                map.put("content", allcontent);
                list.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.card_item, new String[]{"name","time","content",}, new int[]{R.id.name,R.id.time,R.id.content}

        );
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
    }
}
