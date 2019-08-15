package com.android2016ncu.simpledictionary_v2.activity;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android2016ncu.simpledictionary_v2.R;
import com.android2016ncu.simpledictionary_v2.model.Words;
import com.android2016ncu.simpledictionary_v2.util.DownloadService;
import com.android2016ncu.simpledictionary_v2.util.HttpCallBackListener;
import com.android2016ncu.simpledictionary_v2.util.LogUtil;
import com.android2016ncu.simpledictionary_v2.util.MyApplication;
import com.android2016ncu.simpledictionary_v2.util.ParseXML;
import com.android2016ncu.simpledictionary_v2.util.VocabularyAction;
import com.android2016ncu.simpledictionary_v2.util.WordsAction;
import com.android2016ncu.simpledictionary_v2.util.WordsHandler;

import java.io.InputStream;

public class InterpretationFragment extends Fragment {

    private TextView searchWords_key, searchWords_psE, searchWords_psA, searchWords_posAcceptation, searchWords_sent;
    private ImageButton searchWords_voiceE, searchWords_voiceA;
    private ImageView word_like;
    private LinearLayout searchWords_posA_layout,searchWords_posE_layout, searchWords_linerLayout;
    private WordsAction wordsAction;
    private VocabularyAction vocabularyAction;
    private Words words = new Words();

    //服务
    private DownloadService.DownloadBinder downloadBinder;
    private ServiceConnection connection;




    // 网络查词完成后回调handleMessage方法
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 111:
                    //判断网络查找不到该词的情况
                    if (words.getSent().length() > 0) {
                        upDateView();
                    } else {
                        searchWords_linerLayout.setVisibility(View.GONE);
                        Toast.makeText(MyApplication.getContext(), "抱歉！找不到该词！", Toast.LENGTH_SHORT).show();
                    }
                    LogUtil.d("测试", "查询成功并播放发音");
            }
            return true;
        }
    });






    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.interpretation_fragment_layout,container,false);

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
        Intent intent = new Intent(getActivity(),DownloadService.class);
        getActivity().startService(intent);
        getActivity().bindService(intent,connection, Context.BIND_AUTO_CREATE);

        wordsAction = WordsAction.getInstance(getContext());
        vocabularyAction = VocabularyAction.getInstance(getContext());
        //初始化控件
        searchWords_linerLayout = (LinearLayout)view.findViewById(R.id.searchWords_linerLayout);
        searchWords_posA_layout = (LinearLayout)view.findViewById(R.id.searchWords_posA_layout);
        searchWords_posE_layout = (LinearLayout)view.findViewById(R.id.searchWords_posE_layout);

        searchWords_key = (TextView)view.findViewById(R.id.searchWords_key);
        searchWords_psE = (TextView)view.findViewById(R.id.searchWords_psE);
        searchWords_psA = (TextView)view.findViewById(R.id.searchWords_psA);
        searchWords_posAcceptation = (TextView)view.findViewById(R.id.searchWords_posAcceptation);
        searchWords_sent = (TextView)view.findViewById(R.id.searchWords_sent);
        searchWords_voiceE = (ImageButton)view.findViewById(R.id.searchWords_voiceE);
        searchWords_voiceE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordsAction.playMP3(words.getKey(), "E", MyApplication.getContext());
            }
        });
        searchWords_voiceA = (ImageButton)view.findViewById(R.id.searchWords_voiceA);
        searchWords_voiceA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordsAction.playMP3(words.getKey(), "A", MyApplication.getContext());
            }
        });


        //从搜索框传入的值
        String KEY = getArguments().getString("key");

        //红心控件实例
        word_like = (ImageView)view.findViewById(R.id.like);
        /**
         * 判断单词是否在生词表中
         * 根据不同状态设定不同资源图片
         * 以及不同的点击事件
         */
        //生词本中不存在该单词
        if (!vocabularyAction.isExistInVocabulary(KEY)){
            word_like.setImageResource(R.drawable.ic_heart_like);
            word_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    word_like.setImageResource(R.drawable.ic_heart_like_clicked);
                    //添加到生词本
                    vocabularyAction.addToVocabulary(words);
                    Toast.makeText(MyApplication.getContext(), "已保存到生词本", Toast.LENGTH_SHORT).show();
                }
            });
        }else if (vocabularyAction.isExistInVocabulary(KEY)){//生词本中存在该单词
            word_like.setImageResource(R.drawable.ic_heart_like_clicked);
            word_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    word_like.setImageResource(R.drawable.ic_heart_like);
                    //从生词本中删除
                    vocabularyAction.deleteFormVocabulary(words.getKey());
                    Toast.makeText(MyApplication.getContext(), "已从生词本中删除!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        LogUtil.d("测试", "传入的值为:" + KEY);
        //加载单词
        loadWords(KEY);

        return view;
    }



    // 读取words的方法，优先从数据中搜索，没有再通过网络搜索
    public void loadWords(String key) {
        words = wordsAction.getWordsFromSQLite(key);
        if ("" == words.getKey()) {//数据库中没有该词
            String address = wordsAction.getAddressForWords(key);
//            HttpUtil.sentHttpRequest(address, new HttpCallBackListener() {
//                @Override
//                public void onFinish(InputStream inputStream) {
//                    WordsHandler wordsHandler = new WordsHandler();
//                    ParseXML.parse(wordsHandler, inputStream);
//                    words = wordsHandler.getWords();
//                    wordsAction.saveWords(words);
//                    wordsAction.saveWordsMP3(words);
//                    handler.sendEmptyMessage(111);
//                }
//
//                @Override
//                public void onError() {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getContext(), ",网络错误,请连接网络重试", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//                }
//            });
            downloadBinder.sentHttpRequest(address, new HttpCallBackListener() {
                @Override
                public void onFinish(InputStream inputStream) {
                    WordsHandler wordsHandler = new WordsHandler();
                    ParseXML.parse(wordsHandler, inputStream);
                    words = wordsHandler.getWords();
                    wordsAction.saveWords(words);
                    wordsAction.saveWordsMP3(words);
                    handler.sendEmptyMessage(111);
                }

                @Override
                public void onError() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), ",网络错误,请连接网络重试", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } else {
            upDateView();
        }
    }

    //更新UI显示
    public void upDateView() {
        if (words.getIsChinese()) {
            searchWords_posAcceptation.setText(words.getFy());
            searchWords_posA_layout.setVisibility(View.GONE);
            searchWords_posE_layout.setVisibility(View.GONE);
        } else {
            searchWords_posAcceptation.setText(words.getPosAcceptation());
            if(words.getPsE()!="") {
                searchWords_psE.setText(String.format(getResources().getString(R.string.psE), words.getPsE()));
                searchWords_posE_layout.setVisibility(View.VISIBLE);
            }else {
                searchWords_posE_layout.setVisibility(View.GONE);
            }
            if(words.getPsA()!="") {
                searchWords_psA.setText(String.format(getResources().getString(R.string.psA), words.getPsA()));
                searchWords_posA_layout.setVisibility(View.VISIBLE);
            }else {
                searchWords_posA_layout.setVisibility(View.GONE);
            }
        }
        searchWords_key.setText(words.getKey());
        searchWords_sent.setText(words.getSent());
        searchWords_linerLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        getActivity().unbindService(connection);
        super.onDestroy();
    }
}
