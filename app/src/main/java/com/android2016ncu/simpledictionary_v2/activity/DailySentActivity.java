package com.android2016ncu.simpledictionary_v2.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android2016ncu.simpledictionary_v2.R;
import com.android2016ncu.simpledictionary_v2.model.DailySent;
import com.android2016ncu.simpledictionary_v2.util.DailySentAction;
import com.android2016ncu.simpledictionary_v2.util.DownloadService;
import com.android2016ncu.simpledictionary_v2.util.HttpCallBackListener;
import com.android2016ncu.simpledictionary_v2.util.MyApplication;
import com.android2016ncu.simpledictionary_v2.util.ParseJSON;

import java.io.IOException;
import java.io.InputStream;


public class DailySentActivity extends AppCompatActivity {

    private TextView todata,sentContent,sentNote,sentTrans;
    private ImageButton sentPron;
    private ImageView sentPicture;
    private Bitmap bitmap;
    private Context mContext;


    private DailySentAction dailySentAction;
    private DailySent dailySent;

    private DownloadService.DownloadBinder downloadBinder;
    private ServiceConnection connection;


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    //判断是否获得正确数据
                    if (dailySent.getContent().length() > 0){

                        updateView();
                    }else {
                        Toast.makeText(mContext, "出现未知错误", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    if (dailySent.getPictuteAddress().length() > 0) {
                        sentPicture.setImageBitmap(bitmap);
                        //Toast.makeText(DailySentActivity.this, "已接受到数据", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(mContext, "出现未知错误", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            return true;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_sent);

        //初始化服务相关
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                downloadBinder = (DownloadService.DownloadBinder)service;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        Intent intent = new Intent(this,DownloadService.class);
        startService(intent);
        bindService(intent,connection, Context.BIND_AUTO_CREATE);

        //初始化控件
        todata = (TextView)findViewById(R.id.to_date);
        sentContent = (TextView)findViewById(R.id.sent_content);
        sentNote = (TextView)findViewById(R.id.sent_note);
        sentPron = (ImageButton)findViewById(R.id.sent_pron);
        sentPicture = (ImageView)findViewById(R.id.sent_picture);
        sentTrans = (TextView)findViewById(R.id.sent_trans);

        //获取实例
        dailySentAction = DailySentAction.getInstance(this);

        //加载每日一句
        loadSent();
        //加载图片
        new loadSentForPicture().execute(dailySentAction.getDailyAddress());

    }

    public void loadSent(){
        String date = dailySentAction.getDailyAddress();
//        HttpUtil.sentHttpRequest(date, new HttpCallBackListener() {
//            @Override
//            public void onFinish(InputStream inputStream) {
//                ParseJSON parseJSON = new ParseJSON();
//                try {
//                    String data = parseJSON.stranToSting(inputStream);
//                    parseJSON.parseJSON(data);
//                    dailySent = parseJSON.getDailySent();
//                    dailySentAction.saveDailySent(dailySent);
//                    dailySentAction.saveTtsMP3(dailySent);
//                    handler.sendEmptyMessage(1);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onError() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(MyApplication.getContext(), "网络错误", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
        downloadBinder.sentHttpRequest(date, new HttpCallBackListener() {
            @Override
            public void onFinish(InputStream inputStream) {
                ParseJSON parseJSON = new ParseJSON();
                try {
                    String data = parseJSON.stranToSting(inputStream);
                    parseJSON.parseJSON(data);
                    dailySent = parseJSON.getDailySent();
                    dailySentAction.saveDailySent(dailySent);
                    dailySentAction.saveTtsMP3(dailySent);
                    handler.sendEmptyMessage(1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyApplication.getContext(), "网络错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    //更新UI
    public void updateView(){
        todata.setText(dailySent.getToDate());
        //sentPicture.setImageBitmap(dailySentAction.getBitmap(dailySent));
        sentContent.setText(dailySent.getContent());
        sentNote.setText(dailySent.getNote());
        sentTrans.setText(dailySent.getTrans());
        sentPron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailySentAction.playMP3(dailySent.getToDate(), MyApplication.getContext());
            }
        });


    }

    public class loadSentForPicture extends AsyncTask<String, Void, Bitmap> {
        String addr;
        @Override
        protected Bitmap doInBackground(String... strings) {
            addr = strings[0];
//            HttpUtil.sentHttpRequest(addr, new HttpCallBackListener() {
//                @Override
//                public void onFinish(InputStream inputStream) {
//                    ParseJSON parseJSON = new ParseJSON();
//                    try {
//                        addr = parseJSON.stranToSting(inputStream);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    parseJSON.parseJSON(addr);
//                    dailySent = parseJSON.getDailySent();
//                    bitmap = dailySentAction.getBitmap(dailySent);
//                    handler.sendEmptyMessage(2);
//                }
//
//                @Override
//                public void onError() {
//
//                }
//            });
            downloadBinder.sentHttpRequest(addr, new HttpCallBackListener() {
                @Override
                public void onFinish(InputStream inputStream) {
                    ParseJSON parseJSON = new ParseJSON();
                    try {
                        addr = parseJSON.stranToSting(inputStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    parseJSON.parseJSON(addr);
                    dailySent = parseJSON.getDailySent();
                    bitmap = dailySentAction.getBitmap(dailySent);
                    handler.sendEmptyMessage(2);
                }

                @Override
                public void onError() {

                }
            });
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            sentPicture.setImageBitmap(bitmap);
        }
    }


    @Override
    protected void onDestroy() {
        unbindService(connection);
        super.onDestroy();
    }
}
