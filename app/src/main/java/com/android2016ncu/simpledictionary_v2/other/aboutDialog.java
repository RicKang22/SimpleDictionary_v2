package com.android2016ncu.simpledictionary_v2.other;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android2016ncu.simpledictionary_v2.R;

public class aboutDialog extends Dialog {

    private Button yes;//确定按钮
    private TextView title;//消息标题文本
    private TextView memberOne,memberTwo,memberThree;//成员信息
    private ImageView one,two,three;
    private Context context;

    public aboutDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_dialog);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);

        initView();
    }

    public void initView(){
        title = (TextView)findViewById(R.id.about_dialog_title);
        one = (ImageView)findViewById(R.id.image1);
        two = (ImageView)findViewById(R.id.image2);
        three = (ImageView)findViewById(R.id.image3);
        memberOne = (TextView)findViewById(R.id.info1);
        memberTwo = (TextView)findViewById(R.id.info2);
        memberThree = (TextView)findViewById(R.id.info3);
        yes = (Button)findViewById(R.id.about_dialog_yes);

        title.setText("小组信息");
        one.setImageResource(R.drawable.wenkangkang);
        two.setImageResource(R.drawable.denghaocheng);
        three.setImageResource(R.drawable.zouweida);

        memberOne.setText(R.string.about_dialog_one);
        memberTwo.setText(R.string.about_dialog_two);
        memberThree.setText(R.string.about_dialog_three);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
