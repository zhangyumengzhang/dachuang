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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import demo.doctor.com.doctor.R;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private String url = "http://192.168.0.105:8080/";
    List<Chat> chatList = new ArrayList<>();
    List<String> mVals = new ArrayList<>();
    List<String> mVals1 = new ArrayList<>();
    List<String> mVals2 = new ArrayList<>();
    List<String> mVals3 = new ArrayList<>();
    List<String> mVals4 = new ArrayList<>();
    List<String> mVals5 = new ArrayList<>();
    List<String> mVals6 = new ArrayList<>();
    List<String> mVals7 = new ArrayList<>();
    private ChatAdapter mAdapter;
    int i = 1;
    private TagFlowLayout mFlowLayout;
    private RecyclerView lv;
    private EditText et;
    private LinearLayout root;
    private static int id;
    String question2, question3, question4, question5, question6;
    String answer1, answer2, answer3, answer4, answer5;
    String uresult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFlowLayout = findViewById(R.id.flowlayout);
        root = findViewById(R.id.root);
        lv = findViewById(R.id.lv);
        Button btn_send = findViewById(R.id.btn_send);
        et = findViewById(R.id.et);
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
                sendChat(s);
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
                sendChat(a);
                return true;
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
                                sendChat(result);
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
                }
                mVals1.add("怀孕");
                mVals1.add("未怀孕");
                chat2.type = 1;
                Log.d("MainActivity", "lalal" + chat2.content);
                mAdapter.addData(chat2);
                mVals.clear();
                mVals.addAll(mVals1);
                mFlowLayout.onChanged();
                break;
            case 2:
                final Chat chat3 = new Chat();
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
                chat3.type = 1;
                mAdapter.addData(chat3);
                mVals.clear();
                mVals.addAll(mVals2);
                mFlowLayout.onChanged();
                break;
            case 3:
                final Chat chat4 = new Chat();
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
                    }
                }
                chat4.type = 1;
                Log.d("MainActivity", "hahahh" + chat4.content);
                mAdapter.addData(chat4);
                mVals.clear();
                mVals.addAll(mVals3);
                mFlowLayout.onChanged();
                break;
            case 4:
                final Chat chat5 = new Chat();
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
                        }

                        if(answer4=="阵发性"){
                            mVals5.add("一天中的特殊时段（例如晨起、晚上）");
                            mVals5.add("活动加重");
                        }else if(answer4=="持续性"){
                            mVals5.add("咳痰");
                            mVals5.add("咽痛");
                            mVals5.add("喘息");
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
                        }
                        if(answer4=="是"){
                            mVals5.add("是");
                            mVals5.add("否");
                        }else if(answer4=="否"){
                            mVals5.add("阵发性");
                            mVals5.add("持续性");
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
                    }
                    if(answer3=="否"){
                        mVals4.add("大便干硬");
                        mVals4.add("粘液脓血便");
                    }
                }
                chat5.type = 1;
                mAdapter.addData(chat5);
                mVals.clear();
                mVals.addAll(mVals4);
                mFlowLayout.onChanged();
                break;
            case 5:
                final Chat chat6 = new Chat();
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
                         mVals6.add("一天中的特殊时段（例如晨起、晚上）");
                         mVals6.add("活动加重");
                         mVals6.add("刺激（如气味）加重");
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
                         answer5="持续性";
                         mVals6.add("咳痰");
                         mVals6.add("咽痛");
                         mVals6.add("喘息");
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
                         mVals6.add("咳痰");
                         mVals6.add("咽痛");
                         mVals6.add("喘息");
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
                         answer5="活动加重";
                         mVals6.add("肢体乏力");
                         mVals6.add("喘息、胸闷");
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
                         answer5="刺激（如气味）加重";
                         mVals6.add("喘息、胸闷");
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
                         answer5="咳痰";
                         mVals6.add("稀白");
                         mVals6.add("黄稠");
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
                         answer5="咽痛、头痛";
                         mVals6.add("是");
                         mVals6.add("否");
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
                         answer5="喘息、胸闷";
                         mVals6.add("是");
                         mVals6.add("否");
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
                                         chat6.content = question5 = result;
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
                                         chat6.content = question5 = result;
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
                     }
                }
                chat6.type = 1;
                mAdapter.addData(chat6);
                mVals.clear();
                mVals.addAll(mVals5);
                mFlowLayout.onChanged();
                break;
            case 6:
                final Chat chat7 = new Chat();
                if(answer1=="头痛"){

                }
                else if(answer1=="腹痛"){

                }
                else if(answer1=="咳嗽"){

                }
                chat7.type = 1;
                mAdapter.addData(chat7);

                mVals.clear();
                mVals.addAll(mVals6);
                mFlowLayout.onChanged();
                break;
            case 7:
                Chat chat8 = new Chat();
                chat8.content = "发病前患有感冒吗？";
                chat8.type = 1;
                mAdapter.addData(chat8);
                mVals.clear();
                mVals.addAll(mVals7);
                mFlowLayout.onChanged();
                break;
            case 8:
                Chat chat10 = new Chat();
                chat10.content = "谢谢您的回答，您可能的疾病是" + "如果出现紧急情况，请及时就医哦。";
                chat10.type = 1;
                mAdapter.addData(chat10);
                mVals.clear();
                mVals.add("感冒是怎么回事");
                mVals.add("咳嗽是怎么回事");
                mFlowLayout.onChanged();
                i = 0;
                break;
            case 9:

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
public void getresult(final int aid){
    final Chat chatresult = new Chat();
    chatresult.type=1;
    Thread t1=new Thread(new Runnable() {
        @Override
        public void run() {
            String path = url + "result?id=" + aid ;
            try {
                URL url = new URL(path); //新建url并实例化
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");//获取服务器数据
                connection.setReadTimeout(8000);//设置读取超时的毫秒数
                connection.setConnectTimeout(8000);//设置连接超时的毫秒数
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String result = reader.readLine();//读取服务器进行逻辑处理后页面显示的数据
                Log.d("MainActivity", "run: " + result);
                if (result != null) {
                   uresult=result;
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
    chatresult.content= "谢谢您的回答，您可能的疾病是" + uresult+". "+"如果出现紧急情况，请及时就医哦。";
    mAdapter.addData(chatresult);
}
}