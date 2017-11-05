package com.automaticrapper.aidiary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;


public class PreviewActivity extends Activity {
    private Diary diary;
    ImageView imgView;
    ProgressBar loadingAnim;
    LinearLayout contextLayout;

    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_preview);

        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        Bitmap bmp=(Bitmap)msg.obj;
                        imgView.setImageBitmap(bmp);
                        contextLayout.removeView(loadingAnim);
                        break;
                }
            };
        };


        Intent intent = this.getIntent();
        diary = (Diary)intent.getSerializableExtra("diary_data");
        if(diary != null){
            TextView title = (TextView)findViewById(R.id.textView_Title);
            TextView context = (TextView)findViewById(R.id.textView_Content);
            imgView = (ImageView)findViewById(R.id.imageView_photo);
            loadingAnim = (ProgressBar)findViewById(R.id.loading_anim);
            contextLayout = (LinearLayout)findViewById(R.id.context_layout);

            title.setText(diary.title);
            context.setText("\t" + diary.context);
            if(!diary.imageUri.isEmpty()){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bmp = getURLimage(diary.imageUri);
                        Message finishMessage = new Message();
                        finishMessage.what = 0;
                        finishMessage.obj = bmp;
                        handler.sendMessage(finishMessage);
                    }
                }).start();

            }else {
                contextLayout.removeView(imgView);
                contextLayout.removeView(loadingAnim);
            }
        }else {
            new AlertDialog.Builder(this).setTitle("错误").setMessage("读取日记失败！").setPositiveButton("返回",null).show();
            this.finish();
        }

        Button btnBack = (Button)findViewById(R.id.button_back);
        Button btnEdit = (Button)findViewById(R.id.button_edit);

        btnBack.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                PreviewActivity.this.finish();
            }
        });

        btnEdit.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PreviewActivity.this,EditActivity.class);
                intent.putExtra("status","edit");
                intent.putExtra("diary_data",diary);
                startActivity(intent);
            }
        });
    }

    public Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }
}
