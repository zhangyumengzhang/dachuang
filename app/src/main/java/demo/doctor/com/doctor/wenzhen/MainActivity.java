package demo.doctor.com.doctor.wenzhen;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import demo.doctor.com.doctor.R;
import demo.doctor.com.doctor.shouye.functionActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private String url = "http://192.168.0.101:8080/";
    List<Chat> chatList = new ArrayList<>();
    List<String> mVals = new ArrayList<>();
    List<String> mVals1 = new ArrayList<>();
    List<String> mVals2 = new ArrayList<>();
    List<String> mVals3 = new ArrayList<>();
    List<String> mVals4 = new ArrayList<>();
    List<String> mVals5 = new ArrayList<>();
    List<String> mVals6 = new ArrayList<>();
    List<String> mVals7 = new ArrayList<>();
    List<String> mVals8 = new ArrayList<>();
    List<String> mVals9 = new ArrayList<>();
    List<String> mVals10 = new ArrayList<>();
    List<String> mVals11 = new ArrayList<>();
    List<String> mVals12 = new ArrayList<>();
    private ChatAdapter mAdapter;
    int i = 1;
    private TagFlowLayout mFlowLayout;
    private RecyclerView lv;
    private EditText et;
    private LinearLayout root;
    private static int id;
    String question2, question3, question4, question5, question6,question7,question8,question9,question10,question11,question12;
    String answer1, answer2, answer3, answer4, answer5,answer6,answer7,answer8,answer9,answer10,answer11,answer12;
    String uresult;
    boolean issend=true;
    String name= functionActivity.getname();
    String sex;
    String gender;

    private String getgender(final String name) {
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
                            JSONObject jsonObject1 = new JSONObject(result);
                            sex = jsonObject1.getString("gender");
                        }
                    } catch (IOException | JSONException e) {
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
            return sex;
    }

    String content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFlowLayout = findViewById(R.id.flowlayout);
        root = findViewById(R.id.root);
        lv = findViewById(R.id.lv);
        Button btn_send = findViewById(R.id.btn_send);
        et = findViewById(R.id.et);
        gender=getgender(name);
        Log.d("MainActivity", "sex: " + gender);
        mVals.add("头痛是怎么回事");
        mVals.add("咳嗽是怎么回事");
        mFlowLayout.setAdapter(new TagAdapter<String>(mVals) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) parent.inflate(MainActivity.this, R.layout.tv, null);
                tv.setText(s);
                return tv;
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = 0;
                String s = et.getText().toString();
                if (TextUtils.isEmpty(s)) {
                    Toast.makeText(MainActivity.this, "请输入咨询的内容", Toast.LENGTH_LONG).show();
                    return;
                }
                et.setText("");
                if(issend) {
                    if(gender.equals("man")){
                        Log.d("MainActivity", "sex: " + gender);
                        sendChatman(s);
                    }else if(gender.equals("female")){
                        Log.d("MainActivity", "sex: " + gender);
                        sendChat(s);
                    }
                }
            }
        });


        mAdapter = new ChatAdapter(chatList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        lv.setLayoutManager(layoutManager);
        lv.setAdapter(mAdapter);

        Chat chat = new Chat();
        chat.content = "欢迎使用晓智医疗，我是你的智能家庭医生☺，您可以简单的输入或直接语音告诉我您的症状，请问有什么可以帮助您";
        chat.type = 1;
        mAdapter.addData(chat);


        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                String a = mVals.get(position);
                if(issend) {
                    if(gender.equals("man")){
                        Log.d("MainActivity", "sex: " + gender);
                        sendChatman(a);
                    }else if(gender.equals("female")){
                        Log.d("MainActivity", "sex: " + gender);
                        sendChat(a);
                    }
                    return true;
                }
                   return false;
            }

        });

        ImageView voice = findViewById(R.id.voice);

        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Speech speech = new Speech();
                RecognizerDialog mDialog = speech.initSpeech(MainActivity.this);
                //3.设置回调接口
                mDialog.setListener(new RecognizerDialogListener() {
                    @Override
                    public void onResult(RecognizerResult recognizerResult, boolean isLast) {
                        if (isLast) {
                            //解析语音
                            //返回的result为识别后的汉字,直接赋值到TextView上即可
                            String result = speech.parseVoice(recognizerResult.getResultString());
                            Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
                            if (!TextUtils.isEmpty(result)) {
                                if(issend) {
                                    if(gender.equals("man")){
                                        Log.d("MainActivity", "sex: " + gender);
                                        sendChatman(result);
                                    }else if(gender.equals("female")){
                                        Log.d("MainActivity", "sex: " + gender);
                                        sendChat(result);
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(SpeechError speechError) {

                    }
                });
            }
        });

        EPSoftKeyBoardListener.setListener(MainActivity.this, new EPSoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                root.setVisibility(View.GONE);
            }

            @Override
            public void keyBoardHide(int height) {
                root.setVisibility(View.VISIBLE);
            }
        });

    }


    private String[] mPerms = {Manifest.permission.RECORD_AUDIO
    };

    private static final int PERMISSIONS = 100;//请求码


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == PERMISSIONS) {

        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setRationale("没有该权限，此应用程序可能无法正常工作。打开应用设置屏幕以修改应用权限")
                    .setTitle("必需权限")
                    .build()
                    .show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        requestPermission();
    }

    //请求所需权限
    @AfterPermissionGranted(PERMISSIONS)
    private void requestPermission() {
        if (EasyPermissions.hasPermissions(this, mPerms)) {
            //Log.d(TAG, "onClick: 获取读写内存权限,Camera权限和wifi权限");
        } else {
            EasyPermissions.requestPermissions(this, "获取手机录音机使用权限，听写、识别、语义理解需要用到此权限", PERMISSIONS, mPerms);

        }
    }


    private void sendChat(final String a) {

        final Chat chat = new Chat();
        chat.content = a;
        chat.type = 2;
        //chatList.add(chat);
        mAdapter.addData(chat);

        switch (i) {
            case 1:
                final Chat chat2 = new Chat();
                if (a.contains("头痛") || a.contains("头疼")) {
                    answer1 = "头痛";
                    mVals1.add("怀孕");
                    mVals1.add("未怀孕");
                   Thread t1=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //记得修改IP地址
                            String path = url + "question2?answer1=" + answer1;
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
                                    chat2.content = question2 = result;
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
                else if (a.contains("腹痛") || a.contains("肚子疼") || a.contains("胃痛") || a.contains("胃疼")) {
                    answer1 = "腹痛";
                    mVals1.add("怀孕");
                    mVals1.add("未怀孕");
                    Thread t1=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //记得修改IP地址
                            String path = url + "question2?answer1=" + answer1;
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
                                    chat2.content = question2 = result;
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
                else if (a.contains("咳嗽")) {
                    answer1 = "咳嗽";
                    mVals1.add("怀孕");
                    mVals1.add("未怀孕");
                    Thread t1=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //记得修改IP地址
                            String path = url + "question2?answer1=" + answer1;
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
                                    chat2.content = question2 = result;
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
                else if (a.contains("排便困难")) {
                    answer1 = "排便困难";
                    mVals1.add("怀孕");
                    mVals1.add("未怀孕");
                   Thread t1= new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //记得修改IP地址
                            String path = url + "question2?answer1=" + answer1;
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
                                    chat2.content = question2 = result;
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
                else {
                    chat2.content = "非常抱歉，我现在还不能回答相关问题，您可以选择线下就医";
                    i=0;
                    mVals1.add("头痛是怎么回事");
                    mVals1.add("咳嗽是怎么回事");
                }

                if(issend){
                    content=a+"<br>"+question2+"   ";
                    chat2.type = 1;
                    mAdapter.addData(chat2);
                    mVals.clear();
                    mVals.addAll(mVals1);
                    mFlowLayout.onChanged();
                }
                break;
            case 2:
                    final Chat chat3 = new Chat();
                    content=content+a;
                    if(answer1=="头痛"||answer1=="咳嗽"){
                        mVals2.add("长期");
                        mVals2.add("短期");
                    }
                    else if(answer1=="腹痛"){
                        mVals2.add("饭前");
                        mVals2.add("饭后");
                        mVals2.add("无区别");
                    }
                    else if(answer1=="排便困难"){
                        mVals2.add("是");
                        mVals2.add("否");
                    }
                    if (a.contains("未怀孕") || a.contains("没有")) {
                        answer2 = "未怀孕";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //记得修改IP地址
                                String path = url + "question3?answer1=" + answer1 + "&answer2=" + answer2;
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
                                        chat3.content = question3 = result;
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
                    else if (a.contains("怀孕")) {
                        answer2 = "怀孕";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //记得修改IP地址
                                String path = url + "question3?answer1=" + answer1 + "&answer2=" + answer2;
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
                                        chat3.content = question3 = result;
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
                    else {
                        chat3.content = "非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals2.add("头痛是怎么回事");
                        mVals2.add("咳嗽是怎么回事");
                    }

                    if(issend){
                        content=content+"<br>"+question3+"   ";
                        chat3.type = 1;
                        mAdapter.addData(chat3);
                        mVals.clear();
                        mVals.addAll(mVals2);
                        mFlowLayout.onChanged();
                    }
                    break;
            case 3:
                final Chat chat4 = new Chat();
                content=content+a;
                if (answer1 == "头痛") {
                    if (a.contains("长期") || a.contains("长时间")) {
                        mVals3.add("是");
                        mVals3.add("否");
                        answer3 = "长期";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question4?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3;
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
                                        chat4.content = question4 = result;
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
                    else if (a.contains("短期") || a.contains("时间短") || a.contains("时间不长")) {
                        mVals3.add("轻微");
                        mVals3.add("中度");
                        mVals3.add("重度");
                        answer3 = "短期";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question4?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3;
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
                                        chat4.content = question4 = result;
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
                    else {
                        chat4.content = "非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals3.add("头痛是怎么回事");
                        mVals3.add("咳嗽是怎么回事");
                    }
                }
                else if (answer1 == "腹痛") {
                    mVals3.add("是");
                    mVals3.add("否");
                    if (a.contains("饭前") || a.contains("吃饭之前")) {
                        answer3 = "饭前";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question4?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3;
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
                                        chat4.content = question4 = result;
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
                    else if (a.contains("饭后") || a.contains("吃饭之后")) {
                        answer3 = "饭后";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question4?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3;
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
                                        chat4.content = question4 = result;
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
                    else if (a.contains("无区别") || a.contains("没有差别") || a.contains("一样痛")) {
                        answer3 = "无区别";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question4?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3;
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
                                        chat4.content = question4 = result;
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
                    else {
                        chat4.content = "非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals3.add("头痛是怎么回事");
                        mVals3.add("咳嗽是怎么回事");
                    }
                }
                else if (answer1 == "咳嗽") {
                    if (a.contains("长期") || a.contains("长时间")) {
                        mVals3.add("是");
                        mVals3.add("否");
                        answer3 = "长期";
                       Thread t1= new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question4?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3;
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
                                        chat4.content = question4 = result;
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
                    else if (a.contains("短期") || a.contains("时间短") || a.contains("时间不长")) {
                        mVals3.add("持续性");
                        mVals3.add("阵发性");
                        answer3 = "短期";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question4?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3;
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
                                        chat4.content = question4 = result;
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
                    else {
                        chat4.content = "非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals3.add("头痛是怎么回事");
                        mVals3.add("咳嗽是怎么回事");
                    }
                }
                else if (answer1 == "排便困难") {
                    mVals3.add("是");
                    mVals3.add("否");
                    if (a.contains("是")) {
                        answer3="是";
                        final Thread t2=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionid4?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3;
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
                                        id = Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question4?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3;
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
                                        chat4.content = question4 = result;
                                    }else{
                                        t2.start();
                                        t2.join();
                                        getresult(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else if (a.contains("否") || a.contains("没有")) {
                        answer3="否";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question4?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3;
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
                                        chat4.content = question4 = result;
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
                    else{
                        chat4.content = "非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals3.add("头痛是怎么回事");
                        mVals3.add("咳嗽是怎么回事");
                    }
                }

                if(issend){
                    content=content+"<br>"+question4+"   ";
                    chat4.type = 1;
                    Log.d("MainActivity", "hahahh" + chat4.content);
                    mAdapter.addData(chat4);
                    mVals.clear();
                    mVals.addAll(mVals3);
                    mFlowLayout.onChanged();
                }
                break;
            case 4:
                final Chat chat5 = new Chat();
                content=content+a;
                if (answer1 == "头痛") {
                    if(answer3=="短期"){
                        mVals4.add("胀痛");
                        mVals4.add("跳痛");
                        if (a.contains("轻微") || a.contains("轻度")) {
                                answer4 = "轻微";
                                Thread t1=new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String path = url + "question5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4;
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
                                                chat5.content = question5 = result;
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
                        else if (a.contains("中度") || a.contains("适中")) {
                                answer4 = "中度";
                                Thread t1=new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String path = url + "question5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4;
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
                                                chat5.content = question5 = result;
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
                        else if(a.contains("重度")||a.contains("非常痛")){
                                answer4="重度";
                                Thread t1=new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String path = url + "question5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4;
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
                                                chat5.content = question5 = result;
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
                        else {
                            chat5.content = "非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                            i=0;
                            mVals4.add("头痛是怎么回事");
                            mVals4.add("咳嗽是怎么回事");
                            }
                        }
                    else if(answer3=="长期"){
                        mVals4.add("是");
                        mVals4.add("否");
                        if (a.contains("是")) {
                                answer4 = "是";
                                Thread t1=new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String path = url + "question5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4;
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
                                                chat5.content = question5 = result;
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
                        else if (a.contains("否")||a.contains("没有")) {
                                answer4 = "否";
                                Thread t1=new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String path = url + "question5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4;
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
                                                chat5.content = question5 = result;
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
                        else {
                            chat5.content = "非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                            i=0;
                            mVals4.add("头痛是怎么回事");
                            mVals4.add("咳嗽是怎么回事");
                            }
                    }
                }
                else if (answer1 == "腹痛") {
                    if(answer3=="饭前"||answer3=="无区别"){
                        mVals4.add("是");
                        mVals4.add("否");
                    }
                    else if(answer3=="饭后"){
                        mVals4.add("上腹");
                        mVals4.add("中腹");
                    }
                    if(a.contains("是")||a.contains("有")){
                        answer4="是";
                        final Thread t4=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionid5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4;
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
                                        id= Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4;
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
                                        chat5.content = question5 = result;
                                    }else{
                                        t4.start();
                                        t4.join();
                                        getresult(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else if (a.contains("否")||a.contains("没有")) {
                        answer4 = "否";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4;
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
                                        chat5.content = question5 = result;
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
                    else {
                        chat5.content = "非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals4.add("头痛是怎么回事");
                        mVals4.add("咳嗽是怎么回事");
                    }
                }
                else if (answer1 == "咳嗽") {
                    if(answer3=="短期"){
                        if (a.contains("阵发性")) {
                            answer4 = "阵发性";
                            Thread t1=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String path = url + "question5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4;
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
                                            chat5.content = question5 = result;
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
                        else if (a.contains("持续性")) {
                            answer4 = "持续性";
                            Thread t1=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String path = url + "question5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4;
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
                                            chat5.content = question5 = result;
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
                        else {
                            chat5.content = "非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                            i=0;
                            mVals4.add("头痛是怎么回事");
                            mVals4.add("咳嗽是怎么回事");
                        }

                        if(answer4=="阵发性"){
                            mVals4.add("一天中的特殊时段（例如晨起、晚上）");
                            mVals4.add("活动加重");
                        }
                        else if(answer4=="持续性"){
                            mVals4.add("咳痰");
                            mVals4.add("咽痛、头痛");
                            mVals4.add("喘息、胸闷");
                        }
                    }
                    else if(answer3=="长期"){
                        if (a.contains("是")) {
                            answer4 = "是";
                            Thread t1=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String path = url + "question5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4;
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
                                            chat5.content = question5 = result;
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
                        else if (a.contains("否")||a.contains("没有")) {
                            answer4 = "否";
                            Thread t1=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String path = url + "question5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4;
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
                                            chat5.content = question5 = result;
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
                        else {
                            chat5.content = "非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                            i=0;
                            mVals4.add("头痛是怎么回事");
                            mVals4.add("咳嗽是怎么回事");
                        }
                        if(answer4=="是"){
                            mVals4.add("是");
                            mVals4.add("否");
                        }
                        else if(answer4=="否"){
                            mVals4.add("阵发性");
                            mVals4.add("持续性");
                        }
                    }
                }
                else if (answer1 == "排便困难") {
                    if (a.contains("是")) {
                        answer4 = "是";
                        final Thread t3=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionid5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4;
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
                                       id= Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4;
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
                                        chat5.content = question5 = result;
                                    }else{
                                        t3.start();
                                        t3.join();
                                        getresult(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else if (a.contains("否")||a.contains("没有")) {
                        answer4 = "否";
                        final Thread t3=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionid5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4;
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
                                      id =Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4;
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
                                        chat5.content = question5 = result;
                                    }else{
                                        t3.start();
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
                    else {
                        chat5.content = "非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i = 0;
                        mVals4.add("头痛是怎么回事");
                        mVals4.add("咳嗽是怎么回事");
                    }
                    if(answer3=="否"){
                        mVals4.add("大便干硬");
                        mVals4.add("粘液脓血便");
                    }
                }

                if(issend){
                    content=content+"<br>"+question5+"   ";
                    chat5.type = 1;
                    mAdapter.addData(chat5);
                    mVals.clear();
                    mVals.addAll(mVals4);
                    mFlowLayout.onChanged();
                }
                break;
            case 5:
                final Chat chat6 = new Chat();
                content=content+a;
                if(answer1=="头痛"){
                    if(answer4=="轻微"||answer4=="中度"||answer4=="重度"){
                        mVals5.add("是");
                        mVals5.add("否");
                        if(a.contains("胀痛")){
                            answer5="胀痛";
                            Thread t1=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String path = url + "question6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                            chat6.content = question6 = result;
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
                        else if(a.contains("跳痛")){
                            answer5="跳痛";
                            Thread t1=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String path = url + "question6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                            chat6.content = question6 = result;
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
                        else{
                            chat6.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                            i=0;
                            mVals5.add("头痛是怎么回事");
                            mVals5.add("咳嗽是怎么回事");
                        }
                    }
                    else if(answer4=="是"){
                        mVals5.add("是");
                        mVals5.add("否");
                        if(a.contains("是")){
                            answer5="是";
                            Thread t1=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String path = url + "question6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                            chat6.content = question6 = result;
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
                        else if(a.contains("否")||a.contains("没有")){
                            answer5="否";
                            Thread t1=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String path = url + "question6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                            chat6.content = question6 = result;
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
                        else{
                            chat6.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                            i=0;
                            mVals5.add("头痛是怎么回事");
                            mVals5.add("咳嗽是怎么回事");
                        }
                    }
                    else if(answer4=="否"){
                        mVals5.add("呕吐");
                        mVals5.add("发热");
                        mVals5.add("心悸、发汗、饥饿");
                        mVals5.add("颈部压痛");
                        mVals5.add("咽痛、乏力、咳嗽有痰（黄稠）");
                        if(a.contains("是")){
                            answer5="是";
                            Thread t1=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String path = url + "question6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                            chat6.content = question6 = result;
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
                        else if(a.contains("否")||a.contains("没有")){
                            answer5="否";
                            Thread t1=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String path = url + "question6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                            chat6.content = question6 = result;
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
                        else{
                            chat6.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                            i=0;
                            mVals5.add("头痛是怎么回事");
                            mVals5.add("咳嗽是怎么回事");
                        }
                    }
                }
                else if(answer1=="腹痛"){
                    if(a.contains("上腹")){
                        answer5="上腹";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                        chat6.content = question6 = result;
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
                    else if(a.contains("中腹")){
                        answer5="中腹";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                        chat6.content = question6 = result;
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
                    else if(a.contains("是")){
                        answer5="是";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                        chat6.content = question6 = result;
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
                    else if(a.contains("否")||a.contains("没有")){
                        answer5="否";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                        chat6.content = question6 = result;
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
                    else{
                        chat6.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals5.add("头痛是怎么回事");
                        mVals5.add("咳嗽是怎么回事");
                    }
                   if(answer5=="是"||answer5=="否"){
                      mVals5.add("上腹");
                      mVals5.add("中腹");
                   }
                   else if(answer5=="上腹"||answer5=="中腹"){
                       mVals5.add("是");
                       mVals5.add("否");
                   }
                }
                else if(answer1=="咳嗽"){
                     if(a.contains("阵发")){
                         answer5="阵发性";
                         mVals5.add("一天中的特殊时段（例如晨起、晚上）");
                         mVals5.add("活动加重");
                         mVals5.add("刺激（如气味）加重");
                         Thread t1=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "question6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                         chat6.content = question6 = result;
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
                     else if(a.contains("持续")){
                         answer5="持续性";
                         mVals5.add("咳痰");
                         mVals5.add("咽痛、头痛");
                         mVals5.add("喘息、胸闷");
                         Thread t1=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "question6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                         chat6.content = question6 = result;
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
                     else if(a.contains("早晨")||a.contains("晚上")||a.contains("一天")){
                         mVals5.add("咳痰");
                         mVals5.add("咽痛、头痛");
                         mVals5.add("喘息、胸闷");
                         answer5="一天中的特殊时段（例如晨起、晚上）";
                         Thread t1=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "question6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                         chat6.content = question6 = result;
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
                     else if(a.contains("活动")||a.contains("运动")){
                         answer5="活动加重";
                         mVals5.add("肢体乏力");
                         mVals5.add("喘息、胸闷");
                         Thread t1=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "question6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                         chat6.content = question6 = result;
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
                     else if(a.contains("刺激")){
                         answer5="刺激（如气味）加重";
                         mVals5.add("喘息、胸闷");
                         Thread t1=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "question6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                         chat6.content = question6 = result;
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
                     else if(a.contains("痰")){
                         answer5="咳痰";
                         mVals5.add("稀白");
                         mVals5.add("黄稠");
                         Thread t1=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "question6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                         chat6.content = question6 = result;
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
                     else if(a.contains("头痛")||a.contains("头疼")||a.contains("喉咙")||a.contains("咽")){
                         answer5="咽痛、头痛";
                         mVals5.add("是");
                         mVals5.add("否");
                         Thread t1=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "question6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                         chat6.content = question6 = result;
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
                     else if(a.contains("喘")||a.contains("胸闷")){
                         answer5="喘息、胸闷";
                         mVals5.add("是");
                         mVals5.add("否");
                         Thread t1=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "question6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                         chat6.content = question6= result;
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
                     else if(a.contains("是")){
                         answer5="是";
                         final Thread t5=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "questionid6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                    id = Integer.parseInt(result);
                                     }
                                 } catch (IOException e) {
                                     e.printStackTrace();
                                 }
                             }
                         });

                         Thread t1=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "question6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                         chat6.content = question6= result;
                                     }else{
                                         t5.start();
                                         t5.join();
                                         getresult(id);
                                     }
                                 } catch (IOException | InterruptedException e) {
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
                     else if(a.contains("否")||a.contains("没有")){
                         answer5="否";
                         final Thread t5=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "questionid6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                         id = Integer.parseInt(result);
                                     }
                                 } catch (IOException e) {
                                     e.printStackTrace();
                                 }
                                 //chat6.type = 1;
                                // mAdapter.addData(chat6);
                                 mVals.clear();
                                // mVals.addAll(mVals5);
                                 mFlowLayout.onChanged();
                             }
                         });
                         Thread t1=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "question6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                         chat6.content = question6 = result;
                                     }else{
                                         t5.start();
                                         t5.join();
                                         getresult(id);
                                     }
                                 } catch (IOException | InterruptedException e) {
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
                     else{
                         chat6.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                         i=0;
                         mVals5.add("头痛是怎么回事");
                         mVals5.add("咳嗽是怎么回事");
                     }
                }
                if(issend){
                    content=content+"<br>"+question6+"   ";
                    chat6.type = 1;
                    mAdapter.addData(chat6);
                    mVals.clear();
                    mVals.addAll(mVals5);
                    mFlowLayout.onChanged();
                }
                break;
            case 6:
                final Chat chat7 = new Chat();
                content=content+a;
                if(answer1=="头痛"){
                   if(a.contains("是")){
                       mVals6.add("呕吐");
                       mVals6.add("发热");
                       mVals6.add("咽痛、乏力、咳嗽有痰（黄稠）");
                       mVals6.add("心悸、发汗、饥饿");
                       mVals6.add("颈部压痛");
                       answer6="是";
                       final Thread t6=new Thread(new Runnable() {
                           @Override
                           public void run() {
                               String path = url + "questionid7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                       id = Integer.parseInt(result);
                                   }
                               } catch (IOException e) {
                                   e.printStackTrace();
                               }
                           }
                       });

                       Thread t1=new Thread(new Runnable() {
                           @Override
                           public void run() {
                               String path = url + "question7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                       chat7.content = question7 = result;
                                   }else{
                                       t6.start();
                                       t6.join();
                                       getresult(id);
                                   }
                               } catch (IOException | InterruptedException e) {
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
                   else if(a.contains("否")){
                       answer6="否";
                       mVals6.add("呕吐");
                       mVals6.add("发热");
                       mVals6.add("咽痛、乏力、咳嗽有痰（黄稠）");
                       mVals6.add("心悸、发汗、饥饿");
                       mVals6.add("颈部压痛");
                       final Thread t6=new Thread(new Runnable() {
                           @Override
                           public void run() {
                               String path = url + "questionid7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                       id = Integer.parseInt(result);
                                   }
                               } catch (IOException e) {
                                   e.printStackTrace();
                               }
                           }
                       });

                       Thread t1=new Thread(new Runnable() {
                           @Override
                           public void run() {
                               String path = url + "question7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                       chat7.content = question7 = result;
                                   }else{
                                       t6.start();
                                       t6.join();
                                       getresult(id);
                                   }
                               } catch (IOException | InterruptedException e) {
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
                   else if(a.contains("吐")){
                        answer6="呕吐";
                        mVals6.add("1周以下");
                        mVals6.add("1周以上");
                       Thread t1=new Thread(new Runnable() {
                           @Override
                           public void run() {
                               String path = url + "question7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                       chat7.content = question7 = result;
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
                   else if(a.contains("发热")||a.contains("发烧")){
                       answer6="发热";
                       mVals6.add("畏寒、浑身酸痛、流清涕、咳嗽有痰（稀白）");
                       Thread t1=new Thread(new Runnable() {
                           @Override
                           public void run() {
                               String path = url + "question7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                       chat7.content = question7 = result;
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
                   else if(a.contains("力")||a.contains("喉咙")||a.contains("痰")||a.contains("咽")){
                       answer6="咽痛、乏力、咳嗽有痰（黄稠）";
                       final Thread t6=new Thread(new Runnable() {
                           @Override
                           public void run() {
                               String path = url + "questionid7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                    id = Integer.parseInt(result);
                                   }
                               } catch (IOException e) {
                                   e.printStackTrace();
                               }
                           }
                       });

                       Thread t1=new Thread(new Runnable() {
                           @Override
                           public void run() {
                               String path = url + "question7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                       chat7.content = question7 = result;
                                   }else{
                                       t6.start();
                                       t6.join();
                                       getresult(id);
                                   }
                               } catch (IOException | InterruptedException e) {
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
                   else if(a.contains("饿")||a.contains("汗")||a.contains("心")){
                       answer6="心悸、发汗、饥饿";
                       final Thread t6=new Thread(new Runnable() {
                           @Override
                           public void run() {
                               String path = url + "questionid7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                       id = Integer.parseInt(result);
                                   }
                               } catch (IOException e) {
                                   e.printStackTrace();
                               }
                           }
                       });

                       Thread t1=new Thread(new Runnable() {
                           @Override
                           public void run() {
                               String path = url + "question7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                       chat7.content = question7 = result;
                                   }else{
                                       t6.start();
                                       t6.join();
                                       getresult(id);
                                   }
                               } catch (IOException | InterruptedException e) {
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
                   else if(a.contains("颈")||a.contains("脖子")){
                       answer6="颈部压痛";
                       mVals6.add("手臂麻木、眩晕");
                       Thread t1=new Thread(new Runnable() {
                           @Override
                           public void run() {
                               String path = url + "question7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                       chat7.content = question7 = result;
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
                   else{
                       chat7.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                       i=0;
                       mVals6.add("头痛是怎么回事");
                       mVals6.add("咳嗽是怎么回事");
                   }
                }
                else if(answer1=="腹痛"){
                    mVals6.add("是");
                    mVals6.add("否");
                    if(a.contains("上")){
                        answer6="上腹";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                        chat7.content = question7 = result;
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
                    else if(a.contains("中")){
                        answer6="中腹";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                        chat7.content = question7 = result;
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
                    else if(a.contains("是")){
                        answer6="是";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                        chat7.content = question7 = result;
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
                    else if(a.contains("否")||a.contains("没有")){
                        answer6="否";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                        chat7.content = question7 = result;
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
                    else{
                        chat7.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals6.add("头痛是怎么回事");
                        mVals6.add("咳嗽是怎么回事");
                    }
                }
                else if(answer1=="咳嗽"){
                     if(a.contains("痰")){
                        mVals6.add("稀白");
                        mVals6.add("黄稠");
                        answer6="咳痰";
                         Thread t1=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "question7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                         chat7.content = question7 = result;
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
                     else if(a.contains("咽痛")||a.contains("头痛")||a.contains("喉咙")){
                         mVals6.add("是");
                         mVals6.add("否");
                         answer6="咽痛、头痛";
                         Thread t1=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "question7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                         chat7.content = question7 = result;
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
                     else if(a.contains("喘")||a.contains("胸")){
                         mVals6.add("是");
                         mVals6.add("否");
                         answer6="喘息、胸闷";
                         Thread t1=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "question7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                         chat7.content = question7 = result;
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
                     else if(a.contains("力")){
                         answer6="肢体乏力";
                         final Thread t6=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "questionid7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                         id=Integer.parseInt(result);
                                     }
                                 } catch (IOException e) {
                                     e.printStackTrace();
                                 }
                             }
                         });
                         Thread t1=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "question7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                         chat7.content = question7 = result;
                                     }else{
                                         t6.start();
                                         t6.join();
                                         getresult(id);
                                     }
                                 } catch (IOException | InterruptedException e) {
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
                     else if(a.contains("稀")||a.contains("白")){
                         mVals6.add("是");
                         mVals6.add("否");
                         answer6="稀白";
                         Thread t1=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "question7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                         chat7.content = question7 = result;
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
                     else if(a.contains("黄")||a.contains("稠")){
                         mVals6.add("是");
                         mVals6.add("否");
                         answer6="黄稠";
                         Thread t1=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "question7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                         chat7.content = question7 = result;
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
                     else if(a.contains("是")){
                         mVals6.add("是");
                         mVals6.add("否");
                         answer6="是";
                         Thread t1=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "question7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                         chat7.content = question7 = result;
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
                     else if(a.contains("否")||a.contains("不")){
                         mVals6.add("是");
                         mVals6.add("否");
                         answer6="否";
                         Thread t1=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "question7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                         chat7.content = question7 = result;
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
                     else if(a.contains("早晨")||a.contains("晚上")){
                         mVals6.add("咳痰");
                         mVals6.add("咽痛、头痛");
                         mVals6.add("喘息、胸闷");
                         answer6="一天中的特殊时段（例如晨起、晚上）";
                         Thread t1=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "question7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                         chat7.content = question7 = result;
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
                     else if(a.contains("动")){
                         mVals6.add("肢体乏力");
                         mVals6.add("喘息、胸闷");
                         answer6="活动加重";
                         Thread t1=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "question7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                         chat7.content = question7 = result;
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
                     else if(a.contains("刺激")||a.contains("气味")){
                         mVals6.add("喘息、胸闷");
                         answer6="刺激（如气味）加重";
                         Thread t1=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "question7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                         chat7.content = question7 = result;
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
                     else{
                         chat7.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                         i=0;
                         mVals6.add("头痛是怎么回事");
                         mVals6.add("咳嗽是怎么回事");
                     }
                }

                if(issend){
                    content=content+"<br>"+question7+"   ";
                    chat7.type = 1;
                    mAdapter.addData(chat7);
                    mVals.clear();
                    mVals.addAll(mVals6);
                    mFlowLayout.onChanged();
                }
                break;
            case 7:
                final Chat chat8 = new Chat();
                content=content+a;
                if(answer1=="头痛"){
                    if(a.contains("吐")){
                        answer7="呕吐";
                        mVals7.add("1周以下");
                        mVals7.add("1周以上");
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                        chat8.content = question8 = result;
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
                    else if(a.contains("发热")||a.contains("发烧")){
                        answer7="发热";
                        mVals7.add("畏寒、浑身酸痛、流清涕、咳嗽有痰（稀白）");
                         Thread t1=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "question8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                         chat8.content = question8 = result;
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
                    else if(a.contains("力")||a.contains("喉咙")||a.contains("痰")||a.contains("咽")){
                        answer7="咽痛、乏力、咳嗽有痰（黄稠）";
                        final Thread t7=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionid8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+"&answer7="+answer7;
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
                                        id = Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                         Thread t1=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "question8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                         chat8.content = question8 = result;
                                     }else{
                                         t7.start();
                                         t7.join();
                                         getresult(id);
                                     }
                                 } catch (IOException | InterruptedException e) {
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
                    else if(a.contains("饿")||a.contains("汗")||a.contains("心")){
                        answer7="心悸、发汗、饥饿";
                         final Thread t7=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "questionid8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+"&answer7="+answer7;
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
                                         id = Integer.parseInt(result);
                                     }
                                 } catch (IOException e) {
                                     e.printStackTrace();
                                 }
                             }
                         });

                         Thread t1=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "question8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                         chat8.content = question8 = result;
                                     }else{
                                         t7.start();
                                         t7.join();
                                         getresult(id);
                                     }
                                 } catch (IOException | InterruptedException e) {
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
                    else if(a.contains("颈")||a.contains("脖子")){
                        answer7="颈部压痛";
                        mVals7.add("手臂麻木、眩晕");
                         final Thread t7=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "questionid8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+"&answer7="+answer7;
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
                                         id = Integer.parseInt(result);
                                     }
                                 } catch (IOException e) {
                                     e.printStackTrace();
                                 }
                             }
                         });
                         Thread t1=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "question8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                         chat8.content = question8 = result;
                                     }else{
                                         t7.start();
                                         t7.join();
                                         getresult(id);
                                     }
                                 } catch (IOException | InterruptedException e) {
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
                    else if(a.contains("手臂麻木、眩晕")){
                        answer7="是";
                         final Thread t7=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "questionid8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+"&answer7="+answer7;
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
                                         id = Integer.parseInt(result);
                                     }
                                 } catch (IOException e) {
                                     e.printStackTrace();
                                 }
                             }
                         });
                         Thread t1=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "question8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                         chat8.content = question8 = result;
                                     }else{
                                         t7.start();
                                         t7.join();
                                         getresult(id);
                                     }
                                 } catch (IOException | InterruptedException e) {
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
                    else if(a.contains("痰")||a.contains("涕")||a.contains("冷")||a.contains("酸痛")){
                        answer7="畏寒、浑身酸痛、流清涕、咳嗽有痰（稀白）";
                         final Thread t7=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "questionid8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+"&answer7="+answer7;
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
                                         id = Integer.parseInt(result);
                                     }
                                 } catch (IOException e) {
                                     e.printStackTrace();
                                 }
                             }
                         });
                         Thread t1=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "question8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                         chat8.content = question8 = result;
                                     }else{
                                         t7.start();
                                         t7.join();
                                         getresult(id);
                                     }
                                 } catch (IOException | InterruptedException e) {
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
                    else if(a.contains("下")){
                         answer7="1周以下";
                         final Thread t7=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "questionid8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+"&answer7="+answer7;
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
                                         id = Integer.parseInt(result);
                                     }
                                 } catch (IOException e) {
                                     e.printStackTrace();
                                 }
                             }
                         });
                         Thread t1=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "question8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                         chat8.content = question8= result;
                                     }else{
                                         t7.start();
                                         t7.join();
                                         getresult(id);
                                     }
                                 } catch (IOException | InterruptedException e) {
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
                    else if(a.contains("上")){
                         answer7="1周以上";
                         final Thread t7=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "questionid8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+"&answer7="+answer7;
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
                                         id = Integer.parseInt(result);
                                     }
                                 } catch (IOException e) {
                                     e.printStackTrace();
                                 }
                             }
                         });
                         Thread t1=new Thread(new Runnable() {
                             @Override
                             public void run() {
                                 String path = url + "question8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                         chat8.content = question8 = result;
                                     }else{
                                         t7.start();
                                         t7.join();
                                         getresult(id);
                                     }
                                 } catch (IOException | InterruptedException e) {
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
                    else{
                        chat8.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                         i=0;
                         mVals7.add("头痛是怎么回事");
                         mVals7.add("咳嗽是怎么回事");
                    }
                }
                else if(answer1=="腹痛"){
                    mVals7.add("是");
                    mVals7.add("否");
                    if(a.contains("是")){
                        answer7="是";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                        chat8.content = question8 = result;
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
                    else if(a.contains("否")||a.contains("没有")){
                        answer7="否";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                        chat8.content = question8 = result;
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
                    else{
                        chat8.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals7.add("头痛是怎么回事");
                        mVals7.add("咳嗽是怎么回事");
                    }
                }
                else if(answer1=="咳嗽"){
                    if(a.contains("痰")){
                        mVals7.add("稀白");
                        mVals7.add("黄稠");
                        answer7="咳痰";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                        chat8.content = question8 = result;
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
                    else if(a.contains("咽痛")||a.contains("头痛")||a.contains("喉咙")){
                        mVals7.add("是");
                        mVals7.add("否");
                        answer7="咽痛、头痛";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                        chat8.content = question8 = result;
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
                    else if(a.contains("喘")||a.contains("胸")){
                        mVals7.add("是");
                        mVals7.add("否");
                        answer7="喘息、胸闷";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                        chat8.content = question8 = result;
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
                    else if(a.contains("力")){
                        answer7="肢体乏力";
                        final Thread t7=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionid8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                       id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                        chat8.content = question8 = result;
                                    }else{
                                        t7.start();
                                        t7.join();
                                        getresult(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else if(a.contains("稀")||a.contains("白")){
                        mVals7.add("是");
                        mVals7.add("否");
                        answer7="稀白";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                        chat8.content = question8 = result;
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
                    else if(a.contains("黄")||a.contains("稠")){
                        mVals7.add("是");
                        mVals7.add("否");
                        answer7="黄稠";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                        chat8.content = question8 = result;
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
                    else if(a.contains("是")){
                        mVals7.add("是");
                        mVals7.add("否");
                        answer7="是";
                        final Thread t7=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionid8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                        id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                        chat8.content = question8 = result;
                                    }else{
                                        t7.start();
                                        t7.join();
                                        getresult(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else if(a.contains("否")||a.contains("不")){
                        mVals7.add("是");
                        mVals7.add("否");
                        answer7="否";
                        final Thread t7=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionid8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                        id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                        chat8.content = question8 = result;
                                    }else{
                                        t7.start();
                                        t7.join();
                                        getresult(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else{
                        chat8.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals7.add("头痛是怎么回事");
                        mVals7.add("咳嗽是怎么回事");
                    }
                }

                if(issend){
                    content=content+"<br>"+question8+"   ";
                    chat8.type = 1;
                    mAdapter.addData(chat8);
                    mVals.clear();
                    mVals.addAll(mVals7);
                    mFlowLayout.onChanged();
                }
                break;
            case 8:
                final Chat chat9 = new Chat();
                content=content+a;
                if(answer1=="头痛"){
                    if(a.contains("麻")||a.contains("晕")){
                        answer8="手臂麻木、眩晕";
                        Thread tr = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //获取系统当前时间
                                java.util.Date dt = new java.util.Date();
                                java.text.SimpleDateFormat sdf =
                                        new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String currentTime = sdf.format(dt);
                                String path = url + "charu?name=" + name+"&time="+currentTime+"&result="+uresult+"&content="+content;
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
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        final  Thread t8=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionid9?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8;
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
                                        id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question9?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8;
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
                                        chat9.content = question9 = result;
                                    }else{
                                        t8.start();
                                        t8.join();
                                        getresult(id);
                                    }
                                } catch (IOException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        try {
                            t1.start();
                            t1.join();
                            tr.start();
                            tr.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(a.contains("痰")||a.contains("涕")||a.contains("冷")||a.contains("酸痛")){
                        answer8="畏寒、浑身酸痛、流清涕、咳嗽有痰（稀白）";
                        Thread tr = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //获取系统当前时间
                                java.util.Date dt = new java.util.Date();
                                java.text.SimpleDateFormat sdf =
                                        new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String currentTime = sdf.format(dt);
                                String path = url + "charu?name=" + name+"&time="+currentTime+"&result="+uresult+"&content="+content;
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
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        final  Thread t8=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionid9?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8;
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
                                        id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question9?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8;
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
                                        chat9.content = question9= result;
                                    }else{
                                        t8.start();
                                        t8.join();
                                        getresult(id);
                                    }
                                } catch (IOException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        try {
                            t1.start();
                            t1.join();
                            tr.start();
                            tr.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(a.contains("下")){
                        answer8="1周以下";
                        Thread tr = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //获取系统当前时间
                                java.util.Date dt = new java.util.Date();
                                java.text.SimpleDateFormat sdf =
                                        new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String currentTime = sdf.format(dt);
                                String path = url + "charu?name=" + name+"&time="+currentTime+"&result="+uresult+"&content="+content;
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
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        final  Thread t8=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionid9?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8;
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
                                        id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question9?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8;
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
                                        chat9.content = question9 = result;
                                    }else{
                                        t8.start();
                                        t8.join();
                                        getresult(id);
                                    }
                                } catch (IOException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        try {
                            t1.start();
                            t1.join();
                            tr.start();
                            tr.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(a.contains("上")){
                        answer8="1周以上";
                        Thread tr = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //获取系统当前时间
                                java.util.Date dt = new java.util.Date();
                                java.text.SimpleDateFormat sdf =
                                        new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String currentTime = sdf.format(dt);
                                String path = url + "charu?name=" + name+"&time="+currentTime+"&result="+uresult+"&content="+content;
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
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        final  Thread t8=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionid9?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8;
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
                                        id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question9?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8;
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
                                        chat9.content = question9 = result;
                                    }else{
                                        t8.start();
                                        t8.join();
                                        getresult(id);
                                    }
                                } catch (IOException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        try {
                            t1.start();
                            t1.join();
                            tr.start();
                            tr.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        chat9.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals8.add("头痛是怎么回事");
                        mVals8.add("咳嗽是怎么回事");
                    }
                }
                else if(answer1=="腹痛"){
                    mVals8.add("是");
                    mVals8.add("否");
                    if(a.contains("是")){
                        answer8="是";
                        final  Thread t8=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionid9?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8;
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
                                        id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question9?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8;
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
                                        chat9.content = question9 = result;
                                    }else{
                                        t8.start();
                                        t8.join();
                                        getresult(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else if(a.contains("否")||a.contains("没有")){
                        answer8="否";
                        final  Thread t8=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionid9?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8;
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
                                        id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question9?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8;
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
                                        chat9.content = question9 = result;
                                    }else{
                                        t8.start();
                                        t8.join();
                                        getresult(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else{
                        chat9.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals8.add("头痛是怎么回事");
                        mVals8.add("咳嗽是怎么回事");
                    }
                }
                else if(answer1=="咳嗽"){
                    if(a.contains("稀")||a.contains("白")){
                        mVals8.add("是");
                        mVals8.add("否");
                        answer8="稀白";
                        final  Thread t8=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionid9?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8;
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
                                        id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question9?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8;
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
                                        chat9.content = question9 = result;
                                    }else{
                                        t8.start();
                                        t8.join();
                                        getresult(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else if(a.contains("黄")||a.contains("稠")){
                        mVals8.add("是");
                        mVals8.add("否");
                        answer8="黄稠";
                        final  Thread t8=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionid9?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8;
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
                                        id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question9?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8;
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
                                        chat9.content = question9= result;
                                    }else{
                                        t8.start();
                                        t8.join();
                                        getresult(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else if(a.contains("是")){
                        answer8="是";
                        final  Thread t8=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionid9?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8;
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
                                        id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question9?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8;
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
                                        chat9.content = question9 = result;
                                    }else{
                                        t8.start();
                                        t8.join();
                                        getresult(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else if(a.contains("否")||a.contains("不")){
                        answer8="否";
                        final  Thread t8=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionid9?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8;
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
                                        id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question9?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8;
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
                                        chat9.content = question9 = result;
                                    }else{
                                        t8.start();
                                        t8.join();
                                        getresult(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else{
                        chat9.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals8.add("头痛是怎么回事");
                        mVals8.add("咳嗽是怎么回事");
                    }
                }

                if(issend){
                    content=content+"<br>"+question9+"   ";
                    chat9.type = 1;
                    mAdapter.addData(chat9);
                    mVals.clear();
                    mVals.addAll(mVals8);
                    mFlowLayout.onChanged();
                }
                break;

            case 9:
                final Chat chat10=new Chat();
                content=content+a;
                if(answer1=="腹痛"){
                    mVals9.add("是");
                    mVals9.add("否");
                  if(a.contains("是")){
                    answer9="是";
                      final  Thread t9=new Thread(new Runnable() {
                          @Override
                          public void run() {
                              String path = url + "questionid10?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8+ "&answer9=" + answer9;
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
                                      id=Integer.parseInt(result);
                                  }
                              } catch (IOException e) {
                                  e.printStackTrace();
                              }
                          }
                      });
                    Thread t1=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String path = url + "question10?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8+ "&answer9=" + answer9;
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
                                    chat10.content = question10 = result;
                                }else{
                                    t9.start();
                                    t9.join();
                                    getresult(id);
                                }
                            } catch (IOException | InterruptedException e) {
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
                  else if(a.contains("否")||a.contains("没有")){
                    answer9="否";
                      final  Thread t9=new Thread(new Runnable() {
                          @Override
                          public void run() {
                              String path = url + "questionid10?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8+ "&answer9=" + answer9;
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
                                      id=Integer.parseInt(result);
                                  }
                              } catch (IOException e) {
                                  e.printStackTrace();
                              }
                          }
                      });
                      Thread t1=new Thread(new Runnable() {
                          @Override
                          public void run() {
                              String path = url + "question10?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8+ "&answer9=" + answer9;
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
                                      chat10.content = question10 = result;
                                  }else{
                                      t9.start();
                                      t9.join();
                                      getresult(id);
                                  }
                              } catch (IOException | InterruptedException e) {
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
                  else{
                    chat10.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                    i=0;
                    mVals9.add("头痛是怎么回事");
                    mVals9.add("咳嗽是怎么回事");
                }
                }
                else if(answer1=="咳嗽"){
                    if(a.contains("是")){
                    answer9="是";
                        Thread tr = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //获取系统当前时间
                                java.util.Date dt = new java.util.Date();
                                java.text.SimpleDateFormat sdf =
                                        new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String currentTime = sdf.format(dt);
                                String path = url + "charu?name=" + name+"&time="+currentTime+"&result="+uresult+"&content="+content;
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
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "qresult?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8+ "&answer9=" + answer9;
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
                                        chat10.content = uresult = result;
                                    }
                                } catch (IOException  e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        try {
                            t1.start();
                            t1.join();
                            tr.start();
                            tr.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                }
                    else if(a.contains("否")||a.contains("不")){
                    answer9="否";
                        Thread tr = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //获取系统当前时间
                                java.util.Date dt = new java.util.Date();
                                java.text.SimpleDateFormat sdf =
                                        new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String currentTime = sdf.format(dt);
                                String path = url + "charu?name=" + name+"&time="+currentTime+"&result="+uresult+"&content="+content;
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
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "qresult?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8+ "&answer9=" + answer9;
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
                                        chat10.content = uresult = result;
                                    }
                                } catch (IOException  e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        try {
                            t1.start();
                            t1.join();
                            tr.start();
                            tr.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                }
                    else{
                    chat10.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                    i=0;
                    mVals9.add("头痛是怎么回事");
                    mVals9.add("咳嗽是怎么回事");
                }
            }

                if(answer1=="腹痛"){
                    content=content+"<br>"+question10+"   ";
                    chat10.type = 1;
                    mAdapter.addData(chat10);
                }
                else if(answer1=="咳嗽"){
                    chat10.type=1;
                    chat10.content = "谢谢您的回答，您可能的疾病是" + uresult + ". " + "如果出现紧急情况，请及时就医哦。"+"已将您此次问诊生成电子病历。";
                    mAdapter.addData(chat10);
                    if (uresult.contains("风热感冒")) {
                        String jutiresult="风热感冒";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("风寒感冒")){
                        String jutiresult="风寒感冒";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("急性支气管炎")){
                        String jutiresult="急性支气管炎";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("慢性支气管炎")){
                        String jutiresult="慢性支气管炎";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("胃十二指肠溃疡")){
                        String jutiresult="胃十二指肠溃疡";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("慢性胃炎")){
                        String jutiresult="慢性胃炎";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("急性腹泻")){
                        String jutiresult="急性腹泻";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("便秘")){
                        String jutiresult="便秘";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("低血糖")){
                        String jutiresult="低血糖";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("类风湿、风湿")){
                        String jutiresult="类风湿、风湿";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("痛经、月经不调")){
                        String jutiresult="痛经、月经不调";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("偏头痛")){
                        String jutiresult="偏头痛";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("上呼吸道感染")){
                        String jutiresult="上呼吸道感染";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("颈椎病")){
                        String jutiresult="颈椎病";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("外伤")){
                        String jutiresult="外伤";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("食物中毒")){
                        String jutiresult="食物中毒";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    i=0;
                }
                mVals.clear();
                mVals.addAll(mVals9);
                mFlowLayout.onChanged();
                break;

            case 10:
                final Chat chat11=new Chat();
                content=content+a;
                if(answer1=="腹痛"){
                    mVals10.add("是");
                    mVals10.add("否");
                    if(a.contains("是")){
                        answer10="是";
                        final  Thread t10=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionid11?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8+ "&answer9=" + answer9+ "&answer10=" + answer10;
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
                                        id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question11?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8+ "&answer9=" + answer9+ "&answer10=" + answer10;
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
                                        chat11.content = question11 = result;
                                    }else{
                                        t10.start();
                                        t10.join();
                                        getresult(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else if(a.contains("否")||a.contains("没有")){
                        answer10="否";
                        final  Thread t10=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionid11?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8+ "&answer9=" + answer9+ "&answer10=" + answer10;
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
                                        id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question11?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8+ "&answer9=" + answer9+ "&answer10=" + answer10;
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
                                        chat11.content = question11 = result;
                                    }else{
                                        t10.start();
                                        t10.join();
                                        getresult(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else{
                        chat11.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals10.add("头痛是怎么回事");
                        mVals10.add("咳嗽是怎么回事");
                    }
                }
                if(issend){
                    content=content+"<br>"+question11+"   ";
                    chat11.type = 1;
                    mAdapter.addData(chat11);
                    mVals.clear();
                    mVals.addAll(mVals10);
                    mFlowLayout.onChanged();
                }
                break;
            case 11:
                final Chat chat12=new Chat();
                content=content+a;
                if(answer1=="腹痛"){
                    mVals11.add("是");
                    mVals11.add("否");
                    if(a.contains("是")){
                        answer11="是";
                        final  Thread t11=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionid12?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8+ "&answer9=" + answer9+ "&answer10=" + answer10+ "&answer11=" + answer11;
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
                                        id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question12?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8+ "&answer9=" + answer9+ "&answer10=" + answer10+ "&answer11=" + answer11;
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
                                        chat12.content = question12= result;
                                    }else{
                                        t11.start();
                                        t11.join();
                                        getresult(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else if(a.contains("否")||a.contains("没有")){
                        answer11="否";
                        final  Thread t11=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionid12?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8+ "&answer9=" + answer9+ "&answer10=" + answer10+ "&answer11=" + answer11;
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
                                        id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question12?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8+ "&answer9=" + answer9+ "&answer10=" + answer10+ "&answer11=" + answer11;
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
                                        chat12.content = question12= result;
                                    }else{
                                        t11.start();
                                        t11.join();
                                        getresult(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else{
                        chat12.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals11.add("头痛是怎么回事");
                        mVals11.add("咳嗽是怎么回事");
                    }
                }
                if(issend){
                    content=content+"<br>"+question12+"   ";
                    chat12.type = 1;
                    mAdapter.addData(chat12);
                    mVals.clear();
                    mVals.addAll(mVals11);
                    mFlowLayout.onChanged();
                }
                break;
            case 12:
                final Chat chat13=new Chat();
                content=content+a;
                if(answer1=="腹痛"){
                    mVals12.add("是");
                    mVals12.add("否");
                    if(a.contains("是")){
                        answer12="是";
                        Thread tr = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //获取系统当前时间
                                java.util.Date dt = new java.util.Date();
                                java.text.SimpleDateFormat sdf =
                                        new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String currentTime = sdf.format(dt);
                                String path = url + "charu?name=" + name+"&time="+currentTime+"&result="+uresult+"&content="+content;
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
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "qresult?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8+ "&answer9=" + answer9+ "&answer10=" + answer10+ "&answer11=" + answer11+ "&answer12=" + answer12;
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
                                        chat13.content = uresult = result;
                                    }
                                } catch (IOException  e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        try {
                            t1.start();
                            t1.join();
                            tr.start();
                            tr.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(a.contains("否")||a.contains("不")){
                        answer12="否";
                        Thread tr = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //获取系统当前时间
                                java.util.Date dt = new java.util.Date();
                                java.text.SimpleDateFormat sdf =
                                        new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String currentTime = sdf.format(dt);
                                String path = url + "charu?name=" + name+"&time="+currentTime+"&result="+uresult+"&content="+content;
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
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "qresult?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8+ "&answer9=" + answer9+ "&answer10=" + answer10+ "&answer11=" + answer11+ "&answer12=" + answer12;
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
                                        chat13.content = uresult = result;
                                    }
                                } catch (IOException  e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        try {
                            t1.start();
                            t1.join();
                            tr.start();
                            tr.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        chat13.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals12.add("头痛是怎么回事");
                        mVals12.add("咳嗽是怎么回事");
                    }
                }

                if(issend){
                    chat13.type=1;
                    chat13.content = "谢谢您的回答，您可能的疾病是" + uresult + ". " + "如果出现紧急情况，请及时就医哦。"+"已将您此次问诊生成电子病历。";
                    mAdapter.addData(chat13);
                    if (uresult.contains("风热感冒")) {
                        String jutiresult="风热感冒";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("风寒感冒")){
                        String jutiresult="风寒感冒";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("急性支气管炎")){
                        String jutiresult="急性支气管炎";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("慢性支气管炎")){
                        String jutiresult="慢性支气管炎";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("胃十二指肠溃疡")){
                        String jutiresult="胃十二指肠溃疡";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("慢性胃炎")){
                        String jutiresult="慢性胃炎";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("急性腹泻")){
                        String jutiresult="急性腹泻";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("便秘")){
                        String jutiresult="便秘";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("低血糖")){
                        String jutiresult="低血糖";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("类风湿、风湿")){
                        String jutiresult="类风湿、风湿";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("痛经、月经不调")){
                        String jutiresult="痛经、月经不调";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("偏头痛")){
                        String jutiresult="偏头痛";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("上呼吸道感染")){
                        String jutiresult="上呼吸道感染";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("颈椎病")){
                        String jutiresult="颈椎病";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("外伤")){
                        String jutiresult="外伤";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("食物中毒")){
                        String jutiresult="食物中毒";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    i=0;
                    mVals.clear();
                    mVals.addAll(mVals12);
                    mFlowLayout.onChanged();
                }
                break;

        }

        i++;

        lv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                lv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                lv.smoothScrollToPosition(mAdapter.getItemCount() - 1);
            }
        });
    }

    private void sendChatman(final String a) {

        final Chat chat = new Chat();
        chat.content = a;
        chat.type = 2;
        //chatList.add(chat);
        mAdapter.addData(chat);

        switch (i) {
            case 1:
                final Chat chat2 = new Chat();
                if (a.contains("头痛") || a.contains("头疼")) {
                    answer1 = "头痛";
                    Thread t1=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //记得修改IP地址
                            String path = url + "questionman2?answer1=" + answer1;
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
                                    chat2.content = question2 = result;
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
                else if (a.contains("腹痛") || a.contains("肚子疼") || a.contains("胃痛") || a.contains("胃疼")) {
                    answer1 = "腹痛";
                    Thread t1=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //记得修改IP地址
                            String path = url + "questionman2?answer1=" + answer1;
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
                                    chat2.content = question2 = result;
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
                else if (a.contains("咳嗽")) {
                    answer1 = "咳嗽";
                    Thread t1=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //记得修改IP地址
                            String path = url + "questionman2?answer1=" + answer1;
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
                                    chat2.content = question2 = result;
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
                else if (a.contains("排便困难")) {
                    answer1 = "排便困难";
                    Thread t1= new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //记得修改IP地址
                            String path = url + "questionman2?answer1=" + answer1;
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
                                    chat2.content = question2 = result;
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
                else {
                    chat2.content = "非常抱歉，我现在还不能回答相关问题，您可以选择线下就医";
                    i=0;
                    mVals1.add("头痛是怎么回事");
                    mVals1.add("咳嗽是怎么回事");
                }
                if(answer1=="头痛"||answer1=="咳嗽"){
                    mVals2.add("长期");
                    mVals2.add("短期");
                }
                else if(answer1=="腹痛"){
                    mVals2.add("饭前");
                    mVals2.add("饭后");
                    mVals2.add("无区别");
                }
                else if(answer1=="排便困难"){
                    mVals2.add("是");
                    mVals2.add("否");
                }
                if(issend){
                    content=a+"<br>"+question2+"   ";
                    chat2.type = 1;
                    mAdapter.addData(chat2);
                    mVals.clear();
                    mVals.addAll(mVals2);
                    mFlowLayout.onChanged();
                }
                break;
            case 2:
                final Chat chat4 = new Chat();
                content=content+a;
                if (answer1 == "头痛") {
                    if (a.contains("长期") || a.contains("长时间")) {
                        mVals3.add("是");
                        mVals3.add("否");
                        answer2 = "长期";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman3?answer1=" + answer1 + "&answer2=" + answer2 ;
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
                                        chat4.content = question3 = result;
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
                    else if (a.contains("短期") || a.contains("时间短") || a.contains("时间不长")) {
                        mVals3.add("轻微");
                        mVals3.add("中度");
                        mVals3.add("重度");
                        answer2 = "短期";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman3?answer1=" + answer1 + "&answer2=" + answer2;
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
                                        chat4.content = question3= result;
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
                    else {
                        chat4.content = "非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals3.add("头痛是怎么回事");
                        mVals3.add("咳嗽是怎么回事");
                    }
                }
                else if (answer1 == "腹痛") {
                    mVals3.add("是");
                    mVals3.add("否");
                    if (a.contains("饭前") || a.contains("吃饭之前")) {
                        answer2 = "饭前";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman3?answer1=" + answer1 + "&answer2=" + answer2 ;
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
                                        chat4.content = question3 = result;
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
                    else if (a.contains("饭后") || a.contains("吃饭之后")) {
                        answer2= "饭后";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman3?answer1=" + answer1 + "&answer2=" + answer2;
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
                                        chat4.content = question3 = result;
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
                    else if (a.contains("无区别") || a.contains("没有差别") || a.contains("一样痛")) {
                        answer2 = "无区别";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman3?answer1=" + answer1 + "&answer2=" + answer2;
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
                                        chat4.content = question3 = result;
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
                    else {
                        chat4.content = "非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals3.add("头痛是怎么回事");
                        mVals3.add("咳嗽是怎么回事");
                    }
                }
                else if (answer1 == "咳嗽") {
                    if (a.contains("长期") || a.contains("长时间")) {
                        mVals3.add("是");
                        mVals3.add("否");
                        answer2 = "长期";
                        Thread t1= new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman3?answer1=" + answer1 + "&answer2=" + answer2;
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
                                        chat4.content = question3 = result;
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
                    else if (a.contains("短期") || a.contains("时间短") || a.contains("时间不长")) {
                        mVals3.add("持续性");
                        mVals3.add("阵发性");
                        answer2 = "短期";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman3?answer1=" + answer1 + "&answer2=" + answer2;
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
                                        chat4.content = question3 = result;
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
                    else {
                        chat4.content = "非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals3.add("头痛是怎么回事");
                        mVals3.add("咳嗽是怎么回事");
                    }
                }
                else if (answer1 == "排便困难") {
                    mVals3.add("是");
                    mVals3.add("否");
                    if (a.contains("是")) {
                        answer2="是";
                        final Thread t2=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman3?answer1=" + answer1 + "&answer2=" + answer2 ;
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
                                        id = Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman3?answer1=" + answer1 + "&answer2=" + answer2;
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
                                        chat4.content = question3 = result;
                                    }else{
                                        t2.start();
                                        t2.join();
                                        getresultman(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else if (a.contains("否") || a.contains("没有")) {
                        answer3="否";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman3?answer1=" + answer1 + "&answer2=" + answer2;
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
                                        chat4.content = question3 = result;
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
                    else{
                        chat4.content = "非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals3.add("头痛是怎么回事");
                        mVals3.add("咳嗽是怎么回事");
                    }
                }

                if(issend){
                    content=content+"<br>"+question3+"   ";
                    chat4.type = 1;
                    Log.d("MainActivity", "hahahh" + chat4.content);
                    mAdapter.addData(chat4);
                    mVals.clear();
                    mVals.addAll(mVals3);
                    mFlowLayout.onChanged();
                }
                break;
            case 3:
                final Chat chat5 = new Chat();
                content=content+a;
                if (answer1 == "头痛") {
                    if(answer2=="短期"){
                        mVals4.add("胀痛");
                        mVals4.add("跳痛");
                        if (a.contains("轻微") || a.contains("轻度")) {
                            answer3 = "轻微";
                            Thread t1=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String path = url + "questionman4?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3;
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
                                            chat5.content = question4 = result;
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
                        else if (a.contains("中度") || a.contains("适中")) {
                            answer3 = "中度";
                            Thread t1=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String path = url + "questionmna4?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3;
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
                                            chat5.content = question4 = result;
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
                        else if(a.contains("重度")||a.contains("非常痛")){
                            answer3="重度";
                            Thread t1=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String path = url + "questionman4?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 ;
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
                                            chat5.content = question4 = result;
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
                        else {
                            chat5.content = "非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                            i=0;
                            mVals4.add("头痛是怎么回事");
                            mVals4.add("咳嗽是怎么回事");
                        }
                    }
                    else if(answer2=="长期"){
                        mVals4.add("是");
                        mVals4.add("否");
                        if (a.contains("是")) {
                            answer3 = "是";
                            Thread t1=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String path = url + "questionman4?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3;
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
                                            chat5.content = question4 = result;
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
                        else if (a.contains("否")||a.contains("没有")) {
                            answer3 = "否";
                            Thread t1=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String path = url + "questionman4?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3;
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
                                            chat5.content = question4 = result;
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
                        else {
                            chat5.content = "非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                            i=0;
                            mVals4.add("头痛是怎么回事");
                            mVals4.add("咳嗽是怎么回事");
                        }
                    }
                }
                else if (answer1 == "腹痛") {
                    if(answer2=="饭前"||answer2=="无区别"){
                        mVals4.add("是");
                        mVals4.add("否");
                    }
                    else if(answer2=="饭后"){
                        mVals4.add("上腹");
                        mVals4.add("中腹");
                    }
                    if(a.contains("是")||a.contains("有")){
                        answer3="是";
                        final Thread t4=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman4?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3;
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
                                        id= Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman4?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3;
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
                                        chat5.content = question4 = result;
                                    }else{
                                        t4.start();
                                        t4.join();
                                        getresultman(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else if (a.contains("否")||a.contains("没有")) {
                        answer3 = "否";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman4?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 ;
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
                                        chat5.content = question4 = result;
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
                    else {
                        chat5.content = "非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals4.add("头痛是怎么回事");
                        mVals4.add("咳嗽是怎么回事");
                    }
                }
                else if (answer1 == "咳嗽") {
                    if(answer2=="短期"){
                        if (a.contains("阵发性")) {
                            answer3 = "阵发性";
                            Thread t1=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String path = url + "questionman4?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3;
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
                                            chat5.content = question4 = result;
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
                        else if (a.contains("持续性")) {
                            answer3 = "持续性";
                            Thread t1=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String path = url + "questionman4?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3;
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
                                            chat5.content = question4 = result;
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
                        else {
                            chat5.content = "非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                            i=0;
                            mVals4.add("头痛是怎么回事");
                            mVals4.add("咳嗽是怎么回事");
                        }

                        if(answer3=="阵发性"){
                            mVals4.add("一天中的特殊时段（例如晨起、晚上）");
                            mVals4.add("活动加重");
                        }
                        else if(answer3=="持续性"){
                            mVals4.add("咳痰");
                            mVals4.add("咽痛、头痛");
                            mVals4.add("喘息、胸闷");
                        }
                    }
                    else if(answer2=="长期"){
                        if (a.contains("是")) {
                            answer3 = "是";
                            Thread t1=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String path = url + "questionman4?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3;
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
                                            chat5.content = question4 = result;
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
                        else if (a.contains("否")||a.contains("没有")) {
                            answer3 = "否";
                            Thread t1=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String path = url + "questionman4?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3;
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
                                            chat5.content = question4 = result;
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
                        else {
                            chat5.content = "非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                            i=0;
                            mVals4.add("头痛是怎么回事");
                            mVals4.add("咳嗽是怎么回事");
                        }
                        if(answer3=="是"){
                            mVals4.add("是");
                            mVals4.add("否");
                        }
                        else if(answer3=="否"){
                            mVals4.add("阵发性");
                            mVals4.add("持续性");
                        }
                    }
                }
                else if (answer1 == "排便困难") {
                    if (a.contains("是")) {
                        answer3= "是";
                        final Thread t3=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman4?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 ;
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
                                        id= Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman4?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3;
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
                                        chat5.content = question4 = result;
                                    }else{
                                        t3.start();
                                        t3.join();
                                        getresultman(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else if (a.contains("否")||a.contains("没有")) {
                        answer3 = "否";
                        final Thread t3=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman4?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 ;
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
                                        id =Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman4?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3;
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
                                        chat5.content = question4 = result;
                                    }else{
                                        t3.start();
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
                    else {
                        chat5.content = "非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i = 0;
                        mVals4.add("头痛是怎么回事");
                        mVals4.add("咳嗽是怎么回事");
                    }
                    if(answer3=="否"){
                        mVals4.add("大便干硬");
                        mVals4.add("粘液脓血便");
                    }
                }

                if(issend){
                    content=content+"<br>"+question4+"   ";
                    chat5.type = 1;
                    mAdapter.addData(chat5);
                    mVals.clear();
                    mVals.addAll(mVals4);
                    mFlowLayout.onChanged();
                }
                break;
            case 4:
                final Chat chat6 = new Chat();
                content=content+a;
                if(answer1=="头痛"){
                    if(answer3=="轻微"||answer3=="中度"||answer3=="重度"){
                        mVals5.add("是");
                        mVals5.add("否");
                        if(a.contains("胀痛")){
                            answer4="胀痛";
                            Thread t1=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String path = url + "questionman5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4;
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
                                            chat6.content = question5 = result;
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
                        else if(a.contains("跳痛")){
                            answer4="跳痛";
                            Thread t1=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String path = url + "questionman5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4;
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
                                            chat6.content = question5 = result;
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
                        else{
                            chat6.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                            i=0;
                            mVals5.add("头痛是怎么回事");
                            mVals5.add("咳嗽是怎么回事");
                        }
                    }
                    else if(answer3=="是"){
                        mVals5.add("是");
                        mVals5.add("否");
                        if(a.contains("是")){
                            answer4="是";
                            Thread t1=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String path = url + "questionman5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4;
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
                                            chat6.content = question5 = result;
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
                        else if(a.contains("否")||a.contains("没有")){
                            answer4="否";
                            Thread t1=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String path = url + "questionman5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4;
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
                                            chat6.content = question5 = result;
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
                        else{
                            chat6.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                            i=0;
                            mVals5.add("头痛是怎么回事");
                            mVals5.add("咳嗽是怎么回事");
                        }
                    }
                    else if(answer4=="否"){
                        mVals5.add("呕吐");
                        mVals5.add("发热");
                        mVals5.add("心悸、发汗、饥饿");
                        mVals5.add("颈部压痛");
                        mVals5.add("咽痛、乏力、咳嗽有痰（黄稠）");
                        if(a.contains("是")){
                            answer4="是";
                            Thread t1=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String path = url + "questionman5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4;
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
                                            chat6.content = question5 = result;
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
                        else if(a.contains("否")||a.contains("没有")){
                            answer4="否";
                            Thread t1=new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String path = url + "questionman5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4;
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
                                            chat6.content = question5 = result;
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
                        else{
                            chat6.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                            i=0;
                            mVals5.add("头痛是怎么回事");
                            mVals5.add("咳嗽是怎么回事");
                        }
                    }
                }
                else if(answer1=="腹痛"){
                    if(a.contains("上腹")){
                        answer4="上腹";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4;
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
                                        chat6.content = question5 = result;
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
                    else if(a.contains("中腹")){
                        answer4="中腹";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4;
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
                                        chat6.content = question5 = result;
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
                    else if(a.contains("是")){
                        answer4="是";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4;
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
                                        chat6.content = question5 = result;
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
                    else if(a.contains("否")||a.contains("没有")){
                        answer4="否";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4;
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
                                        chat6.content = question5 = result;
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
                    else{
                        chat6.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals5.add("头痛是怎么回事");
                        mVals5.add("咳嗽是怎么回事");
                    }
                    if(answer4=="是"||answer4=="否"){
                        mVals5.add("上腹");
                        mVals5.add("中腹");
                    }
                    else if(answer4=="上腹"||answer4=="中腹"){
                        mVals5.add("是");
                        mVals5.add("否");
                    }
                }
                else if(answer1=="咳嗽"){
                    if(a.contains("阵发")){
                        answer4="阵发性";
                        mVals5.add("一天中的特殊时段（例如晨起、晚上）");
                        mVals5.add("活动加重");
                        mVals5.add("刺激（如气味）加重");
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4;
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
                                        chat6.content = question5 = result;
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
                    else if(a.contains("持续")){
                        answer4="持续性";
                        mVals5.add("咳痰");
                        mVals5.add("咽痛、头痛");
                        mVals5.add("喘息、胸闷");
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4;
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
                                        chat6.content = question5 = result;
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
                    else if(a.contains("早晨")||a.contains("晚上")||a.contains("一天")){
                        mVals5.add("咳痰");
                        mVals5.add("咽痛、头痛");
                        mVals5.add("喘息、胸闷");
                        answer4="一天中的特殊时段（例如晨起、晚上）";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4;
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
                                        chat6.content = question5 = result;
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
                    else if(a.contains("活动")||a.contains("运动")){
                        answer4="活动加重";
                        mVals5.add("肢体乏力");
                        mVals5.add("喘息、胸闷");
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 ;
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
                                        chat6.content = question5 = result;
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
                    else if(a.contains("刺激")){
                        answer4="刺激（如气味）加重";
                        mVals5.add("喘息、胸闷");
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 ;
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
                                        chat6.content = question5 = result;
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
                    else if(a.contains("痰")){
                        answer4="咳痰";
                        mVals5.add("稀白");
                        mVals5.add("黄稠");
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 ;
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
                                        chat6.content = question5 = result;
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
                    else if(a.contains("头痛")||a.contains("头疼")||a.contains("喉咙")||a.contains("咽")){
                        answer4="咽痛、头痛";
                        mVals5.add("是");
                        mVals5.add("否");
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4;
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
                                        chat6.content = question5 = result;
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
                    else if(a.contains("喘")||a.contains("胸闷")){
                        answer4="喘息、胸闷";
                        mVals5.add("是");
                        mVals5.add("否");
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4;
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
                                        chat6.content = question5= result;
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
                    else if(a.contains("是")){
                        answer4="是";
                        final Thread t5=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4;
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
                                        id = Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4;
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
                                        chat6.content = question5= result;
                                    }else{
                                        t5.start();
                                        t5.join();
                                        getresultman(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else if(a.contains("否")||a.contains("没有")){
                        answer4="否";
                        final Thread t5=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4;
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
                                        id = Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                //chat6.type = 1;
                                // mAdapter.addData(chat6);
                                mVals.clear();
                                // mVals.addAll(mVals5);
                                mFlowLayout.onChanged();
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman5?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 ;
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
                                        chat6.content = question5 = result;
                                    }else{
                                        t5.start();
                                        t5.join();
                                        getresultman(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else{
                        chat6.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals5.add("头痛是怎么回事");
                        mVals5.add("咳嗽是怎么回事");
                    }
                }
                if(issend){
                    content=content+"<br>"+question5+"   ";
                    chat6.type = 1;
                    mAdapter.addData(chat6);
                    mVals.clear();
                    mVals.addAll(mVals5);
                    mFlowLayout.onChanged();
                }
                break;
            case 5:
                final Chat chat7 = new Chat();
                content=content+a;
                if(answer1=="头痛"){
                    if(a.contains("是")){
                        mVals6.add("呕吐");
                        mVals6.add("发热");
                        mVals6.add("咽痛、乏力、咳嗽有痰（黄稠）");
                        mVals6.add("心悸、发汗、饥饿");
                        mVals6.add("颈部压痛");
                        answer5="是";
                        final Thread t6=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                        id = Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                        chat7.content = question6 = result;
                                    }else{
                                        t6.start();
                                        t6.join();
                                        getresultman(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else if(a.contains("否")){
                        answer5="否";
                        mVals6.add("呕吐");
                        mVals6.add("发热");
                        mVals6.add("咽痛、乏力、咳嗽有痰（黄稠）");
                        mVals6.add("心悸、发汗、饥饿");
                        mVals6.add("颈部压痛");
                        final Thread t6=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                        id = Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                        chat7.content = question6 = result;
                                    }else{
                                        t6.start();
                                        t6.join();
                                        getresultman(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else if(a.contains("吐")){
                        answer5="呕吐";
                        mVals6.add("1周以下");
                        mVals6.add("1周以上");
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                        chat7.content = question6 = result;
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
                    else if(a.contains("发热")||a.contains("发烧")){
                        answer5="发热";
                        mVals6.add("畏寒、浑身酸痛、流清涕、咳嗽有痰（稀白）");
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                        chat7.content = question6= result;
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
                    else if(a.contains("力")||a.contains("喉咙")||a.contains("痰")||a.contains("咽")){
                        answer5="咽痛、乏力、咳嗽有痰（黄稠）";
                        final Thread t6=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                        id = Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                        chat7.content = question6= result;
                                    }else{
                                        t6.start();
                                        t6.join();
                                        getresultman(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else if(a.contains("饿")||a.contains("汗")||a.contains("心")){
                        answer5="心悸、发汗、饥饿";
                        final Thread t6=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                        id = Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                        chat7.content = question6 = result;
                                    }else{
                                        t6.start();
                                        t6.join();
                                        getresultman(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else if(a.contains("颈")||a.contains("脖子")){
                        answer5="颈部压痛";
                        mVals6.add("手臂麻木、眩晕");
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                        chat7.content = question6 = result;
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
                    else{
                        chat7.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals6.add("头痛是怎么回事");
                        mVals6.add("咳嗽是怎么回事");
                    }
                }
                else if(answer1=="腹痛"){
                    mVals6.add("是");
                    mVals6.add("否");
                    if(a.contains("上")){
                        answer5="上腹";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                        chat7.content = question6 = result;
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
                    else if(a.contains("中")){
                        answer5="中腹";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                        chat7.content = question6 = result;
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
                    else if(a.contains("是")){
                        answer5="是";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                        chat7.content = question6 = result;
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
                    else if(a.contains("否")||a.contains("没有")){
                        answer5="否";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                        chat7.content = question6 = result;
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
                    else{
                        chat7.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals6.add("头痛是怎么回事");
                        mVals6.add("咳嗽是怎么回事");
                    }
                }
                else if(answer1=="咳嗽"){
                    if(a.contains("痰")){
                        mVals6.add("稀白");
                        mVals6.add("黄稠");
                        answer5="咳痰";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                        chat7.content = question6 = result;
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
                    else if(a.contains("咽痛")||a.contains("头痛")||a.contains("喉咙")){
                        mVals6.add("是");
                        mVals6.add("否");
                        answer5="咽痛、头痛";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                        chat7.content = question6 = result;
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
                    else if(a.contains("喘")||a.contains("胸")){
                        mVals6.add("是");
                        mVals6.add("否");
                        answer5="喘息、胸闷";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                        chat7.content = question6 = result;
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
                    else if(a.contains("力")){
                        answer5="肢体乏力";
                        final Thread t6=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                        id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                        chat7.content = question6 = result;
                                    }else{
                                        t6.start();
                                        t6.join();
                                        getresultman(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else if(a.contains("稀")||a.contains("白")){
                        mVals6.add("是");
                        mVals6.add("否");
                        answer5="稀白";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                        chat7.content = question6 = result;
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
                    else if(a.contains("黄")||a.contains("稠")){
                        mVals6.add("是");
                        mVals6.add("否");
                        answer5="黄稠";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                        chat7.content = question6 = result;
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
                    else if(a.contains("是")){
                        mVals6.add("是");
                        mVals6.add("否");
                        answer5="是";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                        chat7.content = question6 = result;
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
                    else if(a.contains("否")||a.contains("不")){
                        mVals6.add("是");
                        mVals6.add("否");
                        answer5="否";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                        chat7.content = question6 = result;
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
                    else if(a.contains("早晨")||a.contains("晚上")){
                        mVals6.add("咳痰");
                        mVals6.add("咽痛、头痛");
                        mVals6.add("喘息、胸闷");
                        answer5="一天中的特殊时段（例如晨起、晚上）";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                        chat7.content = question6 = result;
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
                    else if(a.contains("动")){
                        mVals6.add("肢体乏力");
                        mVals6.add("喘息、胸闷");
                        answer5="活动加重";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                        chat7.content = question6 = result;
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
                    else if(a.contains("刺激")||a.contains("气味")){
                        mVals6.add("喘息、胸闷");
                        answer5="刺激（如气味）加重";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman6?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5;
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
                                        chat7.content = question6 = result;
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
                    else{
                        chat7.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals6.add("头痛是怎么回事");
                        mVals6.add("咳嗽是怎么回事");
                    }
                }

                if(issend){
                    content=content+"<br>"+question6+"   ";
                    chat7.type = 1;
                    mAdapter.addData(chat7);
                    mVals.clear();
                    mVals.addAll(mVals6);
                    mFlowLayout.onChanged();
                }
                break;
            case 6:
                final Chat chat8 = new Chat();
                content=content+a;
                if(answer1=="头痛"){
                    if(a.contains("吐")){
                        answer6="呕吐";
                        mVals7.add("1周以下");
                        mVals7.add("1周以上");
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                        chat8.content = question7 = result;
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
                    else if(a.contains("发热")||a.contains("发烧")){
                        answer6="发热";
                        mVals7.add("畏寒、浑身酸痛、流清涕、咳嗽有痰（稀白）");
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                        chat8.content = question7 = result;
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
                    else if(a.contains("力")||a.contains("喉咙")||a.contains("痰")||a.contains("咽")){
                        answer6="咽痛、乏力、咳嗽有痰（黄稠）";
                        final Thread t7=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                        id = Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                        chat8.content = question7 = result;
                                    }else{
                                        t7.start();
                                        t7.join();
                                        getresultman(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else if(a.contains("饿")||a.contains("汗")||a.contains("心")){
                        answer6="心悸、发汗、饥饿";
                        final Thread t7=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                        id = Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                        chat8.content = question7 = result;
                                    }else{
                                        t7.start();
                                        t7.join();
                                        getresultman(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else if(a.contains("颈")||a.contains("脖子")){
                        answer6="颈部压痛";
                        mVals7.add("手臂麻木、眩晕");
                        final Thread t7=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                        id = Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                        chat8.content = question7 = result;
                                    }else{
                                        t7.start();
                                        t7.join();
                                        getresultman(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else if(a.contains("手臂麻木、眩晕")){
                        answer6="是";
                        final Thread t7=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                        id = Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                        chat8.content = question7 = result;
                                    }else{
                                        t7.start();
                                        t7.join();
                                        getresultman(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else if(a.contains("痰")||a.contains("涕")||a.contains("冷")||a.contains("酸痛")){
                        answer6="畏寒、浑身酸痛、流清涕、咳嗽有痰（稀白）";
                        final Thread t7=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                        id = Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                        chat8.content = question7 = result;
                                    }else{
                                        t7.start();
                                        t7.join();
                                        getresultman(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else if(a.contains("下")){
                        answer6="1周以下";
                        final Thread t7=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                        id = Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                        chat8.content = question7= result;
                                    }else{
                                        t7.start();
                                        t7.join();
                                        getresultman(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else if(a.contains("上")){
                        answer6="1周以上";
                        final Thread t7=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                        id = Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                        chat8.content = question7 = result;
                                    }else{
                                        t7.start();
                                        t7.join();
                                        getresultman(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else{
                        chat8.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals7.add("头痛是怎么回事");
                        mVals7.add("咳嗽是怎么回事");
                    }
                }
                else if(answer1=="腹痛"){
                    mVals7.add("是");
                    mVals7.add("否");
                    if(a.contains("是")){
                        answer6="是";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                        chat8.content = question7 = result;
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
                    else if(a.contains("否")||a.contains("没有")){
                        answer6="否";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                        chat8.content = question7 = result;
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
                    else{
                        chat8.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals7.add("头痛是怎么回事");
                        mVals7.add("咳嗽是怎么回事");
                    }
                }
                else if(answer1=="咳嗽"){
                    if(a.contains("痰")){
                        mVals7.add("稀白");
                        mVals7.add("黄稠");
                        answer6="咳痰";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                        chat8.content = question7 = result;
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
                    else if(a.contains("咽痛")||a.contains("头痛")||a.contains("喉咙")){
                        mVals7.add("是");
                        mVals7.add("否");
                        answer6="咽痛、头痛";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                        chat8.content = question7 = result;
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
                    else if(a.contains("喘")||a.contains("胸")){
                        mVals7.add("是");
                        mVals7.add("否");
                        answer6="喘息、胸闷";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                        chat8.content = question7 = result;
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
                    else if(a.contains("力")){
                        answer6="肢体乏力";
                        final Thread t7=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                        id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                        chat8.content = question7 = result;
                                    }else{
                                        t7.start();
                                        t7.join();
                                        getresultman(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else if(a.contains("稀")||a.contains("白")){
                        mVals7.add("是");
                        mVals7.add("否");
                        answer6="稀白";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                        chat8.content = question7 = result;
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
                    else if(a.contains("黄")||a.contains("稠")){
                        mVals7.add("是");
                        mVals7.add("否");
                        answer6="黄稠";
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                        chat8.content = question7 = result;
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
                    else if(a.contains("是")){
                        mVals7.add("是");
                        mVals7.add("否");
                        answer6="是";
                        final Thread t7=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                        id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                        chat8.content = question7 = result;
                                    }else{
                                        t7.start();
                                        t7.join();
                                        getresultman(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else if(a.contains("否")||a.contains("不")){
                        mVals7.add("是");
                        mVals7.add("否");
                        answer6="否";
                        final Thread t7=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                        id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman7?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6;
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
                                        chat8.content = question7 = result;
                                    }else{
                                        t7.start();
                                        t7.join();
                                        getresultman(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else{
                        chat8.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals7.add("头痛是怎么回事");
                        mVals7.add("咳嗽是怎么回事");
                    }
                }

                if(issend){
                    content=content+"<br>"+question7+"   ";
                    chat8.type = 1;
                    mAdapter.addData(chat8);
                    mVals.clear();
                    mVals.addAll(mVals7);
                    mFlowLayout.onChanged();
                }
                break;
            case 7:
                final Chat chat9 = new Chat();
                content=content+a;
                if(answer1=="头痛"){
                    if(a.contains("麻")||a.contains("晕")){
                        answer7="手臂麻木、眩晕";
                        Thread tr = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //获取系统当前时间
                                java.util.Date dt = new java.util.Date();
                                java.text.SimpleDateFormat sdf =
                                        new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String currentTime = sdf.format(dt);
                                String path = url + "charu?name=" + name+"&time="+currentTime+"&result="+uresult+"&content="+content;
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
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        final  Thread t8=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                        id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                        chat9.content = question8= result;
                                    }else{
                                        t8.start();
                                        t8.join();
                                        getresultman(id);
                                    }
                                } catch (IOException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        try {
                            t1.start();
                            t1.join();
                            tr.start();
                            tr.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(a.contains("痰")||a.contains("涕")||a.contains("冷")||a.contains("酸痛")){
                        answer7="畏寒、浑身酸痛、流清涕、咳嗽有痰（稀白）";
                        Thread tr = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //获取系统当前时间
                                java.util.Date dt = new java.util.Date();
                                java.text.SimpleDateFormat sdf =
                                        new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String currentTime = sdf.format(dt);
                                String path = url + "charu?name=" + name+"&time="+currentTime+"&result="+uresult+"&content="+content;
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
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        final  Thread t8=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                        id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                        chat9.content = question8= result;
                                    }else{
                                        t8.start();
                                        t8.join();
                                        getresultman(id);
                                    }
                                } catch (IOException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        try {
                            t1.start();
                            t1.join();
                            tr.start();
                            tr.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(a.contains("下")){
                        answer7="1周以下";
                        Thread tr = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //获取系统当前时间
                                java.util.Date dt = new java.util.Date();
                                java.text.SimpleDateFormat sdf =
                                        new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String currentTime = sdf.format(dt);
                                String path = url + "charu?name=" + name+"&time="+currentTime+"&result="+uresult+"&content="+content;
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
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        final  Thread t8=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                        id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                        chat9.content = question8 = result;
                                    }else{
                                        t8.start();
                                        t8.join();
                                        getresultman(id);
                                    }
                                } catch (IOException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        try {
                            t1.start();
                            t1.join();
                            tr.start();
                            tr.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(a.contains("上")){
                        answer7="1周以上";
                        Thread tr = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //获取系统当前时间
                                java.util.Date dt = new java.util.Date();
                                java.text.SimpleDateFormat sdf =
                                        new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String currentTime = sdf.format(dt);
                                String path = url + "charu?name=" + name+"&time="+currentTime+"&result="+uresult+"&content="+content;
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
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        final  Thread t8=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                        id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                        chat9.content = question8 = result;
                                    }else{
                                        t8.start();
                                        t8.join();
                                        getresultman(id);
                                    }
                                } catch (IOException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        try {
                            t1.start();
                            t1.join();
                            tr.start();
                            tr.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        chat9.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals8.add("头痛是怎么回事");
                        mVals8.add("咳嗽是怎么回事");
                    }
                }
                else if(answer1=="腹痛"){
                    mVals8.add("是");
                    mVals8.add("否");
                    if(a.contains("是")){
                        answer7="是";
                        final  Thread t8=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                        id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                        chat9.content = question8 = result;
                                    }else{
                                        t8.start();
                                        t8.join();
                                        getresultman(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else if(a.contains("否")||a.contains("没有")){
                        answer7="否";
                        final  Thread t8=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                        id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                        chat9.content = question8 = result;
                                    }else{
                                        t8.start();
                                        t8.join();
                                        getresultman(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else{
                        chat9.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals8.add("头痛是怎么回事");
                        mVals8.add("咳嗽是怎么回事");
                    }
                }
                else if(answer1=="咳嗽"){
                    if(a.contains("稀")||a.contains("白")){
                        mVals8.add("是");
                        mVals8.add("否");
                        answer7="稀白";
                        final  Thread t8=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                        id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                        chat9.content = question8 = result;
                                    }else{
                                        t8.start();
                                        t8.join();
                                        getresultman(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else if(a.contains("黄")||a.contains("稠")){
                        mVals8.add("是");
                        mVals8.add("否");
                        answer7="黄稠";
                        final  Thread t8=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                        id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                        chat9.content = question8= result;
                                    }else{
                                        t8.start();
                                        t8.join();
                                        getresultman(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else if(a.contains("是")){
                        answer7="是";
                        final  Thread t8=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                        id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                        chat9.content = question8 = result;
                                    }else{
                                        t8.start();
                                        t8.join();
                                        getresultman(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else if(a.contains("否")||a.contains("不")){
                        answer7="否";
                        final  Thread t8=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                        id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman8?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7;
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
                                        chat9.content = question8 = result;
                                    }else{
                                        t8.start();
                                        t8.join();
                                        getresultman(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else{
                        chat9.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals8.add("头痛是怎么回事");
                        mVals8.add("咳嗽是怎么回事");
                    }
                }

                if(issend){
                    content=content+"<br>"+question8+"   ";
                    chat9.type = 1;
                    mAdapter.addData(chat9);
                    mVals.clear();
                    mVals.addAll(mVals8);
                    mFlowLayout.onChanged();
                }
                break;

            case 8:
                final Chat chat10=new Chat();
                content=content+a;
                if(answer1=="腹痛"){
                    mVals9.add("是");
                    mVals9.add("否");
                    if(a.contains("是")){
                        answer8="是";
                        final  Thread t9=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman9?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8;
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
                                        id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "question9?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8;
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
                                        chat10.content = question9 = result;
                                    }else{
                                        t9.start();
                                        t9.join();
                                        getresultman(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else if(a.contains("否")||a.contains("没有")){
                        answer8="否";
                        final  Thread t9=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman9?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8;
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
                                        id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman9?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8;
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
                                        chat10.content = question9= result;
                                    }else{
                                        t9.start();
                                        t9.join();
                                        getresultman(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else{
                        chat10.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals9.add("头痛是怎么回事");
                        mVals9.add("咳嗽是怎么回事");
                    }
                }
                else if(answer1=="咳嗽"){
                    if(a.contains("是")){
                        answer8="是";
                        Thread tr = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //获取系统当前时间
                                java.util.Date dt = new java.util.Date();
                                java.text.SimpleDateFormat sdf =
                                        new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String currentTime = sdf.format(dt);
                                String path = url + "charu?name=" + name+"&time="+currentTime+"&result="+uresult+"&content="+content;
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
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "qresult?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8;
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
                                        chat10.content = uresult = result;
                                    }
                                } catch (IOException  e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        try {
                            t1.start();
                            t1.join();
                            tr.start();
                            tr.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(a.contains("否")||a.contains("不")){
                        answer8="否";
                        Thread tr = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //获取系统当前时间
                                java.util.Date dt = new java.util.Date();
                                java.text.SimpleDateFormat sdf =
                                        new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String currentTime = sdf.format(dt);
                                String path = url + "charu?name=" + name+"&time="+currentTime+"&result="+uresult+"&content="+content;
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
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "qresult?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8;
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
                                        chat10.content = uresult = result;
                                    }
                                } catch (IOException  e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        try {
                            t1.start();
                            t1.join();
                            tr.start();
                            tr.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        chat10.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals9.add("头痛是怎么回事");
                        mVals9.add("咳嗽是怎么回事");
                    }
                }

                if(answer1=="腹痛"){
                    content=content+"<br>"+question9+"   ";
                    chat10.type = 1;
                    mAdapter.addData(chat10);
                }
                else if(answer1=="咳嗽"){
                    chat10.type=1;
                    chat10.content = "谢谢您的回答，您可能的疾病是" + uresult + ". " + "如果出现紧急情况，请及时就医哦。"+"已将您此次问诊生成电子病历。";
                    mAdapter.addData(chat10);
                    if (uresult.contains("风热感冒")) {
                        String jutiresult="风热感冒";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("风寒感冒")){
                        String jutiresult="风寒感冒";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("急性支气管炎")){
                        String jutiresult="急性支气管炎";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("慢性支气管炎")){
                        String jutiresult="慢性支气管炎";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("胃十二指肠溃疡")){
                        String jutiresult="胃十二指肠溃疡";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("慢性胃炎")){
                        String jutiresult="慢性胃炎";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("急性腹泻")){
                        String jutiresult="急性腹泻";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("便秘")){
                        String jutiresult="便秘";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("低血糖")){
                        String jutiresult="低血糖";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("类风湿、风湿")){
                        String jutiresult="类风湿、风湿";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("痛经、月经不调")){
                        String jutiresult="痛经、月经不调";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("偏头痛")){
                        String jutiresult="偏头痛";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("上呼吸道感染")){
                        String jutiresult="上呼吸道感染";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("颈椎病")){
                        String jutiresult="颈椎病";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("外伤")){
                        String jutiresult="外伤";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("食物中毒")){
                        String jutiresult="食物中毒";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }

                    i=0;
                }
                mVals.clear();
                mVals.addAll(mVals9);
                mFlowLayout.onChanged();
                break;

            case 9:
                final Chat chat11=new Chat();
                content=content+a;
                if(answer1=="腹痛"){
                    mVals10.add("是");
                    mVals10.add("否");
                    if(a.contains("是")){
                        answer9="是";
                        final  Thread t10=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman10?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8+ "&answer9=" + answer9;
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
                                        id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman10?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8+ "&answer9=" + answer9;
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
                                        chat11.content = question10 = result;
                                    }else{
                                        t10.start();
                                        t10.join();
                                        getresultman(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else if(a.contains("否")||a.contains("没有")){
                        answer9="否";
                        final  Thread t10=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman10?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8+ "&answer9=" + answer9;
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
                                        id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman10?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8+ "&answer9=" + answer9;
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
                                        chat11.content = question10 = result;
                                    }else{
                                        t10.start();
                                        t10.join();
                                        getresultman(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else{
                        chat11.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals10.add("头痛是怎么回事");
                        mVals10.add("咳嗽是怎么回事");
                    }
                }
                if(issend){
                    content=content+"<br>"+question10+"   ";
                    chat11.type = 1;
                    mAdapter.addData(chat11);
                    mVals.clear();
                    mVals.addAll(mVals10);
                    mFlowLayout.onChanged();
                }
                break;
            case 10:
                final Chat chat12=new Chat();
                content=content+a;
                if(answer1=="腹痛"){
                    mVals11.add("是");
                    mVals11.add("否");
                    if(a.contains("是")){
                        answer10="是";
                        final  Thread t11=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman11?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8+ "&answer9=" + answer9+ "&answer10=" + answer10;
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
                                        id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman11?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8+ "&answer9=" + answer9+ "&answer10=" + answer10;
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
                                        chat12.content = question11= result;
                                    }else{
                                        t11.start();
                                        t11.join();
                                        getresultman(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else if(a.contains("否")||a.contains("没有")){
                        answer10="否";
                        final  Thread t11=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionidman11?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8+ "&answer9=" + answer9+ "&answer10=" + answer10;
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
                                        id=Integer.parseInt(result);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "questionman11?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8+ "&answer9=" + answer9+ "&answer10=" + answer10;
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
                                        chat12.content = question11= result;
                                    }else{
                                        t11.start();
                                        t11.join();
                                        getresultman(id);
                                    }
                                } catch (IOException | InterruptedException e) {
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
                    else{
                        chat12.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals11.add("头痛是怎么回事");
                        mVals11.add("咳嗽是怎么回事");
                    }
                }
                if(issend){
                    content=content+"<br>"+question11+"   ";
                    chat12.type = 1;
                    mAdapter.addData(chat12);
                    mVals.clear();
                    mVals.addAll(mVals11);
                    mFlowLayout.onChanged();
                }
                break;
            case 11:
                final Chat chat13=new Chat();
                content=content+a;
                if(answer1=="腹痛"){
                    mVals12.add("是");
                    mVals12.add("否");
                    if(a.contains("是")){
                        answer11="是";
                        Thread tr = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //获取系统当前时间
                                java.util.Date dt = new java.util.Date();
                                java.text.SimpleDateFormat sdf =
                                        new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String currentTime = sdf.format(dt);
                                String path = url + "charu?name=" + name+"&time="+currentTime+"&result="+uresult+"&content="+content;
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
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "qresultman?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8+ "&answer9=" + answer9+ "&answer10=" + answer10+ "&answer11=" + answer11;
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
                                        chat13.content = uresult = result;
                                    }
                                } catch (IOException  e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        try {
                            t1.start();
                            t1.join();
                            tr.start();
                            tr.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(a.contains("否")||a.contains("不")){
                        answer11="否";
                        Thread tr = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //获取系统当前时间
                                java.util.Date dt = new java.util.Date();
                                java.text.SimpleDateFormat sdf =
                                        new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String currentTime = sdf.format(dt);
                                String path = url + "charu?name=" + name+"&time="+currentTime+"&result="+uresult+"&content="+content;
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
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        Thread t1=new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = url + "qresultman?answer1=" + answer1 + "&answer2=" + answer2 + "&answer3=" + answer3 + "&answer4=" + answer4 + "&answer5=" + answer5+ "&answer6=" + answer6+ "&answer7=" + answer7+ "&answer8=" + answer8+ "&answer9=" + answer9+ "&answer10=" + answer10+ "&answer11=" + answer11;
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
                                        chat13.content = uresult = result;
                                    }
                                } catch (IOException  e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        try {
                            t1.start();
                            t1.join();
                            tr.start();
                            tr.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        chat13.content="非常抱歉，我无法理解您的意思，您可以换种方式提问，或者选择线下就医";
                        i=0;
                        mVals12.add("头痛是怎么回事");
                        mVals12.add("咳嗽是怎么回事");
                    }
                }

                if(issend){
                    chat13.type=1;
                    chat13.content = "谢谢您的回答，您可能的疾病是" + uresult + ". " + "如果出现紧急情况，请及时就医哦。"+"已将您此次问诊生成电子病历。";
                    mAdapter.addData(chat13);
                    if (uresult.contains("风热感冒")) {
                        String jutiresult="风热感冒";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("风寒感冒")){
                        String jutiresult="风寒感冒";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("急性支气管炎")){
                        String jutiresult="急性支气管炎";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("慢性支气管炎")){
                        String jutiresult="慢性支气管炎";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("胃十二指肠溃疡")){
                        String jutiresult="胃十二指肠溃疡";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("慢性胃炎")){
                        String jutiresult="慢性胃炎";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("急性腹泻")){
                        String jutiresult="急性腹泻";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("便秘")){
                        String jutiresult="便秘";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("低血糖")){
                        String jutiresult="低血糖";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("类风湿、风湿")){
                        String jutiresult="类风湿、风湿";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("痛经、月经不调")){
                        String jutiresult="痛经、月经不调";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("偏头痛")){
                        String jutiresult="偏头痛";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("上呼吸道感染")){
                        String jutiresult="上呼吸道感染";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("颈椎病")){
                        String jutiresult="颈椎病";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("外伤")){
                        String jutiresult="外伤";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    if(uresult.contains("食物中毒")){
                        String jutiresult="食物中毒";
                        List<String> list = new ArrayList();
                        list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

                        for(int i=0;i<list.size();i++){
                            Chat chatmedic=new Chat();
                            chatmedic.type=1;
                            chatmedic.content=jutiresult+"\r\n" +list.get(i);
                            mAdapter.addData(chatmedic);
                        }
                    }
                    i=0;
                    mVals.clear();
                    mVals.addAll(mVals12);
                    mFlowLayout.onChanged();
                }
                break;

        }

        i++;

        lv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                lv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                lv.smoothScrollToPosition(mAdapter.getItemCount() - 1);
            }
        });
    }
    public void getresult(final int aid) {
        final Chat chatresult = new Chat();
        chatresult.type = 1;
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                String path = url + "result?id=" + aid;
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
                        uresult = result;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Log.d("MainActivity", "allnam: " + name);
        Thread tr = new Thread(new Runnable() {
            @Override
            public void run() {
                //获取系统当前时间
                java.util.Date dt = new java.util.Date();
                java.text.SimpleDateFormat sdf =
                        new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentTime = sdf.format(dt);
                String path = url + "charu?name=" + name+"&time="+currentTime+"&result="+uresult+"&content="+content;
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            t1.start();
            t1.join();
            tr.start();
            tr.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        chatresult.content = "谢谢您的回答，您可能的疾病是" + uresult + ". " + "如果出现紧急情况，请及时就医哦。"+"已将您此次问诊生成电子病历。";
        mAdapter.addData(chatresult);
        mVals.clear();
        mVals.add("头痛是怎么回事");
        mVals.add("咳嗽是怎么回事");

        if (uresult.contains("风热感冒")) {
            String jutiresult="风热感冒";
            List<String> list = new ArrayList();
            list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

            for(int i=0;i<list.size();i++){
                Chat chatmedic=new Chat();
                chatmedic.type=1;
                chatmedic.content=jutiresult+"\r\n" +list.get(i);
                mAdapter.addData(chatmedic);
            }
        }
        if(uresult.contains("风寒感冒")){
            String jutiresult="风寒感冒";
            List<String> list = new ArrayList();
            list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

            for(int i=0;i<list.size();i++){
                Chat chatmedic=new Chat();
                chatmedic.type=1;
                chatmedic.content=jutiresult+"\r\n" +list.get(i);
                mAdapter.addData(chatmedic);
            }
        }
        if(uresult.contains("急性支气管炎")){
            String jutiresult="急性支气管炎";
            List<String> list = new ArrayList();
            list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

            for(int i=0;i<list.size();i++){
                Chat chatmedic=new Chat();
                chatmedic.type=1;
                chatmedic.content=jutiresult+"\r\n" +list.get(i);
                mAdapter.addData(chatmedic);
            }
        }
        if(uresult.contains("慢性支气管炎")){
            String jutiresult="慢性支气管炎";
            List<String> list = new ArrayList();
            list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

            for(int i=0;i<list.size();i++){
                Chat chatmedic=new Chat();
                chatmedic.type=1;
                chatmedic.content=jutiresult+"\r\n" +list.get(i);
                mAdapter.addData(chatmedic);
            }
        }
        if(uresult.contains("胃十二指肠溃疡")){
            String jutiresult="胃十二指肠溃疡";
            List<String> list = new ArrayList();
            list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

            for(int i=0;i<list.size();i++){
                Chat chatmedic=new Chat();
                chatmedic.type=1;
                chatmedic.content=jutiresult+"\r\n" +list.get(i);
                mAdapter.addData(chatmedic);
            }
        }
        if(uresult.contains("慢性胃炎")){
            String jutiresult="慢性胃炎";
            List<String> list = new ArrayList();
            list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

            for(int i=0;i<list.size();i++){
                Chat chatmedic=new Chat();
                chatmedic.type=1;
                chatmedic.content=jutiresult+"\r\n" +list.get(i);
                mAdapter.addData(chatmedic);
            }
        }
        if(uresult.contains("急性腹泻")){
            String jutiresult="急性腹泻";
            List<String> list = new ArrayList();
            list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

            for(int i=0;i<list.size();i++){
                Chat chatmedic=new Chat();
                chatmedic.type=1;
                chatmedic.content=jutiresult+"\r\n" +list.get(i);
                mAdapter.addData(chatmedic);
            }
        }
        if(uresult.contains("便秘")){
            String jutiresult="便秘";
            List<String> list = new ArrayList();
            list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

            for(int i=0;i<list.size();i++){
                Chat chatmedic=new Chat();
                chatmedic.type=1;
                chatmedic.content=jutiresult+"\r\n" +list.get(i);
                mAdapter.addData(chatmedic);
            }
        }
        if(uresult.contains("低血糖")){
            String jutiresult="低血糖";
            List<String> list = new ArrayList();
            list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

            for(int i=0;i<list.size();i++){
                Chat chatmedic=new Chat();
                chatmedic.type=1;
                chatmedic.content=jutiresult+"\r\n" +list.get(i);
                mAdapter.addData(chatmedic);
            }
        }
        if(uresult.contains("类风湿、风湿")){
            String jutiresult="类风湿、风湿";
            List<String> list = new ArrayList();
            list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

            for(int i=0;i<list.size();i++){
                Chat chatmedic=new Chat();
                chatmedic.type=1;
                chatmedic.content=jutiresult+"\r\n" +list.get(i);
                mAdapter.addData(chatmedic);
            }
        }
        if(uresult.contains("痛经、月经不调")){
            String jutiresult="痛经、月经不调";
            List<String> list = new ArrayList();
            list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

            for(int i=0;i<list.size();i++){
                Chat chatmedic=new Chat();
                chatmedic.type=1;
                chatmedic.content=jutiresult+"\r\n" +list.get(i);
                mAdapter.addData(chatmedic);
            }
        }
        if(uresult.contains("偏头痛")){
            String jutiresult="偏头痛";
            List<String> list = new ArrayList();
            list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

            for(int i=0;i<list.size();i++){
                Chat chatmedic=new Chat();
                chatmedic.type=1;
                chatmedic.content=jutiresult+"\r\n" +list.get(i);
                mAdapter.addData(chatmedic);
            }
        }
        if(uresult.contains("上呼吸道感染")){
            String jutiresult="上呼吸道感染";
            List<String> list = new ArrayList();
            list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

            for(int i=0;i<list.size();i++){
                Chat chatmedic=new Chat();
                chatmedic.type=1;
                chatmedic.content=jutiresult+"\r\n" +list.get(i);
                mAdapter.addData(chatmedic);
            }
        }
        if(uresult.contains("颈椎病")){
            String jutiresult="颈椎病";
            List<String> list = new ArrayList();
            list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

            for(int i=0;i<list.size();i++){
                Chat chatmedic=new Chat();
                chatmedic.type=1;
                chatmedic.content=jutiresult+"\r\n" +list.get(i);
                mAdapter.addData(chatmedic);
            }
        }
        if(uresult.contains("外伤")){
            String jutiresult="外伤";
            List<String> list = new ArrayList();
            list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

            for(int i=0;i<list.size();i++){
                Chat chatmedic=new Chat();
                chatmedic.type=1;
                chatmedic.content=jutiresult+"\r\n" +list.get(i);
                mAdapter.addData(chatmedic);
            }
        }
        if(uresult.contains("食物中毒")){
            String jutiresult="食物中毒";
            List<String> list = new ArrayList();
            list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

            for(int i=0;i<list.size();i++){
                Chat chatmedic=new Chat();
                chatmedic.type=1;
                chatmedic.content=jutiresult+"\r\n" +list.get(i);
                mAdapter.addData(chatmedic);
            }
        }
        i=0;
        issend=false;
    }

    public void getresultman(final int aid) {
        final Chat chatresult = new Chat();
        chatresult.type = 1;
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                String path = url + "resultman?id=" + aid;
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
                        uresult = result;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Log.d("MainActivity", "name: " + name);
        Thread tr = new Thread(new Runnable() {
            @Override
            public void run() {
                //获取系统当前时间
                java.util.Date dt = new java.util.Date();
                java.text.SimpleDateFormat sdf =
                        new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentTime = sdf.format(dt);
                String path = url + "charu?name=" + name+"&time="+currentTime+"&result="+uresult+"&content="+content;
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            t1.start();
            t1.join();
            tr.start();
            tr.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        chatresult.content = "谢谢您的回答，您可能的疾病是" + uresult + ". " + "如果出现紧急情况，请及时就医哦。"+"已将您此次问诊生成电子病历。";
        mAdapter.addData(chatresult);
        mVals.clear();
        mVals.add("头痛是怎么回事");
        mVals.add("咳嗽是怎么回事");
        if (uresult.contains("风热感冒")) {
            String jutiresult="风热感冒";
            List<String> list = new ArrayList();
            list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

            for(int i=0;i<list.size();i++){
                Chat chatmedic=new Chat();
                chatmedic.type=1;
                chatmedic.content=jutiresult+"\r\n" +list.get(i);
                mAdapter.addData(chatmedic);
            }
        }
        if(uresult.contains("风寒感冒")){
            String jutiresult="风寒感冒";
            List<String> list = new ArrayList();
            list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

            for(int i=0;i<list.size();i++){
                Chat chatmedic=new Chat();
                chatmedic.type=1;
                chatmedic.content=jutiresult+"\r\n" +list.get(i);
                mAdapter.addData(chatmedic);
            }
        }
        if(uresult.contains("急性支气管炎")){
            String jutiresult="急性支气管炎";
            List<String> list = new ArrayList();
            list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

            for(int i=0;i<list.size();i++){
                Chat chatmedic=new Chat();
                chatmedic.type=1;
                chatmedic.content=jutiresult+"\r\n" +list.get(i);
                mAdapter.addData(chatmedic);
            }
        }
        if(uresult.contains("慢性支气管炎")){
            String jutiresult="慢性支气管炎";
            List<String> list = new ArrayList();
            list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

            for(int i=0;i<list.size();i++){
                Chat chatmedic=new Chat();
                chatmedic.type=1;
                chatmedic.content=jutiresult+"\r\n" +list.get(i);
                mAdapter.addData(chatmedic);
            }
        }
        if(uresult.contains("胃十二指肠溃疡")){
            String jutiresult="胃十二指肠溃疡";
            List<String> list = new ArrayList();
            list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

            for(int i=0;i<list.size();i++){
                Chat chatmedic=new Chat();
                chatmedic.type=1;
                chatmedic.content=jutiresult+"\r\n" +list.get(i);
                mAdapter.addData(chatmedic);
            }
        }
        if(uresult.contains("慢性胃炎")){
            String jutiresult="慢性胃炎";
            List<String> list = new ArrayList();
            list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

            for(int i=0;i<list.size();i++){
                Chat chatmedic=new Chat();
                chatmedic.type=1;
                chatmedic.content=jutiresult+"\r\n" +list.get(i);
                mAdapter.addData(chatmedic);
            }
        }
        if(uresult.contains("急性腹泻")){
            String jutiresult="急性腹泻";
            List<String> list = new ArrayList();
            list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

            for(int i=0;i<list.size();i++){
                Chat chatmedic=new Chat();
                chatmedic.type=1;
                chatmedic.content=jutiresult+"\r\n" +list.get(i);
                mAdapter.addData(chatmedic);
            }
        }
        if(uresult.contains("便秘")){
            String jutiresult="便秘";
            List<String> list = new ArrayList();
            list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

            for(int i=0;i<list.size();i++){
                Chat chatmedic=new Chat();
                chatmedic.type=1;
                chatmedic.content=jutiresult+"\r\n" +list.get(i);
                mAdapter.addData(chatmedic);
            }
        }
        if(uresult.contains("低血糖")){
            String jutiresult="低血糖";
            List<String> list = new ArrayList();
            list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

            for(int i=0;i<list.size();i++){
                Chat chatmedic=new Chat();
                chatmedic.type=1;
                chatmedic.content=jutiresult+"\r\n" +list.get(i);
                mAdapter.addData(chatmedic);
            }
        }
        if(uresult.contains("类风湿、风湿")){
            String jutiresult="类风湿、风湿";
            List<String> list = new ArrayList();
            list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

            for(int i=0;i<list.size();i++){
                Chat chatmedic=new Chat();
                chatmedic.type=1;
                chatmedic.content=jutiresult+"\r\n" +list.get(i);
                mAdapter.addData(chatmedic);
            }
        }
        if(uresult.contains("痛经、月经不调")){
            String jutiresult="痛经、月经不调";
            List<String> list = new ArrayList();
            list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

            for(int i=0;i<list.size();i++){
                Chat chatmedic=new Chat();
                chatmedic.type=1;
                chatmedic.content=jutiresult+"\r\n" +list.get(i);
                mAdapter.addData(chatmedic);
            }
        }
        if(uresult.contains("偏头痛")){
            String jutiresult="偏头痛";
            List<String> list = new ArrayList();
            list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

            for(int i=0;i<list.size();i++){
                Chat chatmedic=new Chat();
                chatmedic.type=1;
                chatmedic.content=jutiresult+"\r\n" +list.get(i);
                mAdapter.addData(chatmedic);
            }
        }
        if(uresult.contains("上呼吸道感染")){
            String jutiresult="上呼吸道感染";
            List<String> list = new ArrayList();
            list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

            for(int i=0;i<list.size();i++){
                Chat chatmedic=new Chat();
                chatmedic.type=1;
                chatmedic.content=jutiresult+"\r\n" +list.get(i);
                mAdapter.addData(chatmedic);
            }
        }
        if(uresult.contains("颈椎病")){
            String jutiresult="颈椎病";
            List<String> list = new ArrayList();
            list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

            for(int i=0;i<list.size();i++){
                Chat chatmedic=new Chat();
                chatmedic.type=1;
                chatmedic.content=jutiresult+"\r\n" +list.get(i);
                mAdapter.addData(chatmedic);
            }
        }
        if(uresult.contains("外伤")){
            String jutiresult="外伤";
            List<String> list = new ArrayList();
            list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

            for(int i=0;i<list.size();i++){
                Chat chatmedic=new Chat();
                chatmedic.type=1;
                chatmedic.content=jutiresult+"\r\n" +list.get(i);
                mAdapter.addData(chatmedic);
            }
        }
        if(uresult.contains("食物中毒")){
            String jutiresult="食物中毒";
            List<String> list = new ArrayList();
            list=yaopinActivity.parseWithJSON(yaopinActivity.getyaopininfo(jutiresult));

            for(int i=0;i<list.size();i++){
                Chat chatmedic=new Chat();
                chatmedic.type=1;
                chatmedic.content=jutiresult+"\r\n" +list.get(i);
                mAdapter.addData(chatmedic);
            }
        }
        i=0;
        issend=false;
    }
}
