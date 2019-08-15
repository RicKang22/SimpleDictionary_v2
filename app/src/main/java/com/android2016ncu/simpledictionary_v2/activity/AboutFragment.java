package com.android2016ncu.simpledictionary_v2.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android2016ncu.simpledictionary_v2.R;
import com.android2016ncu.simpledictionary_v2.other.aboutDialog;
import com.android2016ncu.simpledictionary_v2.util.FileUtil;


public class AboutFragment extends Fragment {

    private aboutDialog aboutDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_fragment,container,false);
        TextView textView1 = (TextView)view.findViewById(R.id.textView1);
        TextView clearBtn = (TextView)view.findViewById(R.id.clear);
        TextView about_us = (TextView)view.findViewById(R.id.about_us);


        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("警告")
                        .setCancelable(false)
                        .setMessage("确定清除所有本地缓存吗?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FileUtil.getInstance().deleteFile();
                                Toast.makeText(getContext(), "清除成功", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
        about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutDialog = new aboutDialog(getActivity());
                aboutDialog.show();
            }
        });

        return view;
    }




}
