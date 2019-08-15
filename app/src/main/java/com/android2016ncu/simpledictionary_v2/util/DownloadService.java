package com.android2016ncu.simpledictionary_v2.util;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadService extends Service {
    public DownloadService() {
    }


    private DownloadBinder mBinder = new DownloadBinder();
    @Override
    public IBinder onBind(Intent intent) {
        return new DownloadBinder();
    }

    public static class DownloadBinder extends Binder{
        public static void sentHttpRequest(final String address, final HttpCallBackListener listener) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpURLConnection connection = null;
                    try {
                        URL url = new URL(address);
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(8000);
                        connection.setReadTimeout(8000);
                        InputStream inputStream = connection.getInputStream();
                        if (listener != null) {
                            listener.onFinish(inputStream);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (listener != null) {
                            listener.onError();
                        }
                    } finally {
                        if (connection != null) {
                            connection.disconnect();
                        }
                    }
                }
            }).start();
        }
    }
}
