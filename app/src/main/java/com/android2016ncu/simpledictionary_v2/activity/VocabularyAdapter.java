package com.android2016ncu.simpledictionary_v2.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android2016ncu.simpledictionary_v2.R;
import com.android2016ncu.simpledictionary_v2.model.Vocabulary;
import com.android2016ncu.simpledictionary_v2.util.VocabularyAction;

import java.util.List;

//适配器,显示生词的key和其词义


class ViewHolder{
    public TextView words_key;
    public TextView words_trans;

    View itemView;

    public ViewHolder(View itemView){
        if (itemView == null){
            throw new IllegalArgumentException("itemView can not be null");
        }
        this.itemView = itemView;
        words_key = (TextView)itemView.findViewById(R.id.words_key);
        words_trans = (TextView)itemView.findViewById(R.id.words_trans);
    }


}


public class VocabularyAdapter extends BaseAdapter {

    private VocabularyAction vocabularyAction;
    private List<Vocabulary>vocabularyList;
    private LayoutInflater layoutInflater;
    private Context context;
    private ViewHolder holder = null;



    public  VocabularyAdapter(Context context,List<Vocabulary>vocabularyList){
        this.context = context;
        this.vocabularyList = vocabularyList;

        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return vocabularyList.size();
    }

    @Override
    public Object getItem(int position) {
        return vocabularyList.get(position).getWordsKey();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void refreshDataSet(){
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.vocabulary_list_item,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.words_key.setText(vocabularyList.get(position).getWordsKey());
        holder.words_trans.setText(vocabularyList.get(position).getTranslation());


        return convertView;
    }
}
