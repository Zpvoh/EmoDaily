package com.automaticrapper.aidiary;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class DiaryListAdapter extends BaseAdapter {
    private Diary[] data;
    private Context context;
    private LayoutInflater layoutInflater;
    private SimpleDateFormat df;

    public DiaryListAdapter(Context context, Diary[] data){
        this.context = context;
        this.data = data;
        layoutInflater = LayoutInflater.from(context);
        df = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.list_view_item,null);
        TextView titleView = (TextView)view.findViewById(R.id.textView_title);
        TextView dateView = (TextView)view.findViewById(R.id.textView_date);
        ImageView emotionIndicator = (ImageView)view.findViewById(R.id.emotion_indicator);
        titleView.setText(((Diary)this.getItem(i)).title);
        dateView.setText(((Diary)this.getItem(i)).postTime);
        float e = ((Diary)this.getItem(i)).emotion;
        if(e >=0 && e <= 0.3){
            emotionIndicator.setBackgroundColor(view.getResources().getColor(R.color.goodEmotion));
        }else if(e > 0.3 && e <= 0.7){
            emotionIndicator.setBackgroundColor(view.getResources().getColor(R.color.midEmotion));
        }else {
            emotionIndicator.setBackgroundColor(view.getResources().getColor(R.color.badEmotion));
        }

        return view;
    }

    @Override
    public Object getItem(int i) {
        return data[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
}
