package com.android2016ncu.simpledictionary_v2.util;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.Toast;

import com.android2016ncu.simpledictionary_v2.db.DailySentSQLiteOpenHelper;
import com.android2016ncu.simpledictionary_v2.model.DailySent;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * "每日一句"的工具类 方法:
 * 获取address地址
 * 保存到数据库
 * 播放句子读音
 * 保存句子读音MP3文件
 */
public class DailySentAction {
    //本类的实例
    private static DailySentAction dailySentAction;

    // Words的表名
    private final String TABLE_DAILYSENT = "DailySent";

    //数据库工具，用于增、删、该、查
    private SQLiteDatabase db;

    // 播放器,播放读音
    private MediaPlayer player = null;

    //图片资源
    private Bitmap picture;

    // 私有化的构造器
    private DailySentAction(Context context) {
        DailySentSQLiteOpenHelper helper = new DailySentSQLiteOpenHelper(context, TABLE_DAILYSENT, null, 1);
        db = helper.getWritableDatabase();
    }

    /**
     * 单例类WordsAction获取实例方法
     * context 上下文
     */
    public static DailySentAction getInstance(Context context) {
        if (dailySentAction == null) {
            synchronized (WordsAction.class) {
                if (dailySentAction == null) {
                    dailySentAction = new DailySentAction(context);
                }
            }
        }
        return dailySentAction;
    }

    //向数据库中保存DailySent信息
    public boolean saveDailySent(DailySent dailySent){
        //判断是否有数据
        if (dailySent.getContent().length() > 0){
            ContentValues values = new ContentValues();
            values.put("content",dailySent.getContent());
            values.put("tts",dailySent.getTts());
            values.put("note",dailySent.getNote());
            values.put("todate",dailySent.getToDate());
            values.put("pictureAddress",dailySent.getPictuteAddress());
            values.put("trans",dailySent.getTrans());
            db.insert(TABLE_DAILYSENT,null,values);
            values.clear();
            return true;
        }

        return false;
    }


    //获取address
    public String getDailyAddress(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HHHH-yy-dd");
        Date date = new Date(System.currentTimeMillis());

        String address1 = "http://open.iciba.com/dsapi/?date=";
        String address2 = simpleDateFormat.format(date);


        return address1 + address2;

    }








    //获取图片
    public Bitmap getBitmap(DailySent dailySent){
        String address = dailySent.getPictuteAddress();

        if (address != ""){
            HttpUtil.sentHttpRequest(address, new HttpCallBackListener() {
                @Override
                public void onFinish(InputStream inputStream) {
                    try {
                        picture = BitmapFactory.decodeStream(inputStream,null,null);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError() {
                    Toast.makeText(MyApplication.getContext(), "网络错误", Toast.LENGTH_SHORT).show();
                }
            });
        }
        //picture.prepareToDraw();
        return picture;
    }


    //保存发音MP3文件并存入SD卡
    public void saveTtsMP3(final DailySent dailySent){
        String ttsAddress = dailySent.getTts();
        //确认有效性
        if (ttsAddress != ""){
            //建立以日期为名的文件夹
            final String pathDate = dailySent.getToDate();
            HttpUtil.sentHttpRequest(ttsAddress, new HttpCallBackListener() {
                @Override
                public void onFinish(InputStream inputStream) {
                    FileUtil.getInstance().writeToSD(pathDate,dailySent.getToDate()+".MP3",inputStream);
                }

                @Override
                public void onError() {
                    Toast.makeText(MyApplication.getContext(), "保存失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    //播放句子发音MP3
    public void playMP3(String date,Context context){
        //获得文件
        String fileName = date+ "/" + date +  ".mp3";
        String filePath = FileUtil.getInstance().getPathInSD(fileName);
        LogUtil.d("文件夹全名", filePath);
        //重置播放器
        if (player != null){
            if (player.isPlaying()){
                player.stop();
            }
            player.release();
            player = null;
        }
        if (filePath != null){//有内容则播放
            player = MediaPlayer.create(context, Uri.parse(filePath));
            player.start();
        }else {//如果没有则去下载
            DailySent dailySent = new DailySent(null,null,null,null,date);
            dailySentAction.saveTtsMP3(dailySent);
        }

    }


}
