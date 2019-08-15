package com.android2016ncu.simpledictionary_v2.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android2016ncu.simpledictionary_v2.R;
import com.android2016ncu.simpledictionary_v2.model.Vocabulary;
import com.android2016ncu.simpledictionary_v2.util.MyApplication;
import com.android2016ncu.simpledictionary_v2.util.VocabularyAction;

import java.util.ArrayList;
import java.util.List;


public class VocabularyActivity extends Activity {
    private ListView listView;
    private VocabularyAction vocabularyAction;
    private List<Vocabulary>list = new ArrayList<Vocabulary>();
    private VocabularyAdapter adapter;

    //当前长按的位置
    private int itemPos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);

        listView = (ListView)findViewById(R.id.vocabulary_listView);
        vocabularyAction = VocabularyAction.getInstance(MyApplication.getContext());
        //获取全部生词
        list = vocabularyAction.getVocabularyList();

        adapter = new VocabularyAdapter(MyApplication.getContext(),list);
        listView.setAdapter(adapter);

        //注册上下文菜单
        registerForContextMenu(listView);
        //设置长按操作
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                itemPos = position;
                listView.showContextMenu();
                return true;
            }
        });

    }



    //ListView的上下文菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0,0,0,"删除");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 0:
                vocabularyAction.deleteFormVocabulary(list.get(itemPos).getWordsKey());
                refreshData();
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }


    //刷新生词表的ListView
    public void refreshData(){
        adapter = new VocabularyAdapter(MyApplication.getContext(),vocabularyAction.getVocabularyList());
        listView.setAdapter(adapter);
    }


}
