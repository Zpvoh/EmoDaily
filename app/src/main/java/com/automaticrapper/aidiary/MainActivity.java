package com.automaticrapper.aidiary;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private Diary[] diaryItems = {new Diary("日记1","日记1的内容",0.1f),
                                    new Diary("日记2","日记2的内容",0.2f),
                                    new Diary("日记3","日记3的内容",0.3f),
                                    new Diary("日记4","日记4的内容",0.4f),
                                    new Diary("日记5","日记5的内容",0.5f),
                                    new Diary("日记6","日记6的内容",0.6f),
                                    new Diary("日记7","日记7的内容",0.7f),
                                    new Diary("日记8","日记8的内容",0.8f)};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        ProgressDialog progressDialog =  new ProgressDialog(this);
        progressDialog.setMessage("加载日记中...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        //请在这里初始化diaryItems
        try {
            JSONObject jsonObject = HttpRequestUtils.stringToJSON("{}");
             HttpRequestUtils.requestPost("http://120.78.173.81/demo/hack/php/label.php",jsonObject);
        }catch (JSONException e){
            System.out.println(e);
        }

        String str = HttpRequestUtils.getRequest("http://120.78.173.81/demo/hack/php/home.php");
        try{
            JSONObject jsonObject = HttpRequestUtils.stringToJSON(str);

        }catch (JSONException e){
            System.out.println(e);
        }

        progressDialog.dismiss();

        diaryItems[0].imageUri = "http://img4.imgtn.bdimg.com/it/u=2192668381,2116711447&fm=200&gp=0.jpg";

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setElevation(0);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,EditActivity.class);
                intent.putExtra("status","create");
                startActivity(intent);
            }
        });

        ListView lv = (ListView)findViewById(R.id.main_listview);
        DiaryListAdapter adapter = new DiaryListAdapter(this,diaryItems);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,PreviewActivity.class);
                intent.putExtra("diary_data",((Diary)adapterView.getItemAtPosition(i)));
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_new_diary) {
            Intent intent = new Intent(MainActivity.this,EditActivity.class);
            intent.putExtra("status","create");
            startActivity(intent);
            return true;
        }else if(id == R.id.menu_report){
            Intent intent = new Intent(MainActivity.this,ChartActivity.class);
            ArrayList<Float> emotions = new ArrayList<Float>();

            ProgressDialog progressDialog =  new ProgressDialog(this);
            progressDialog.setMessage("加载图表数据中...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            emotions.add(0.0f);
            emotions.add(0.1f);
            emotions.add(0.3f);
            emotions.add(0.2f);
            emotions.add(0.5f);

            progressDialog.dismiss();

            intent.putExtra("emotion_data",emotions);

            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
