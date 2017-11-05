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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {
    private ArrayList<Diary> diaryItems;
    ArrayList<Float> emotions;
    private String trim(String str){
//        Pattern p = Pattern.compile("\\s*|\t|\r|\n|\\|");
//        Matcher m = p.matcher(str);
//        String dest = m.replaceAll("");
  //      str = str.replace('\\','');

        return "";
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emotions = new ArrayList<Float>();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }




        diaryItems = new ArrayList<Diary>();

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
        String[] temp = str.substring(2,str.length()-2).split("[}][,][{]");
        try{
            for (int i=0;i<temp.length;i++){
                temp[i] = "{"+temp[i]+"}";
                JSONObject jsonObject = HttpRequestUtils.stringToJSON(temp[i]);
                Diary item = new Diary(jsonObject.getString("title"),jsonObject.getString("content"),(float)jsonObject.getDouble("emotion"));
                item.imageUri = jsonObject.getString("src");
                item.postTime = jsonObject.getString("date");
                emotions.add((float)jsonObject.getDouble("emotion"));
                diaryItems.add(item);
//                while(iterator.hasNext()){
//                    String key = (String) iterator.next();
//                    JSONObject object = jsonObject.getJSONObject(key);
//                    Diary newItem = new Diary(object.getString("title"),object.getString("content"),(float)object.getDouble("emotion"));
//                    newItem.imageUri = object.getString("src");
//                    newItem.postTime = new Date(object.getString("date"));
//                    diaryItems.add(newItem);
//                }
            }



        }catch (JSONException e){
            System.out.println(e);
        }

        progressDialog.dismiss();


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

        Diary arr[] = new Diary[diaryItems.size()];
        ListView lv = (ListView)findViewById(R.id.main_listview);
        diaryItems.toArray(arr);
        DiaryListAdapter adapter = new DiaryListAdapter(this,arr);
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

            ProgressDialog progressDialog =  new ProgressDialog(this);
            progressDialog.setMessage("加载图表数据中...");
            progressDialog.setCancelable(false);
            progressDialog.show();




            progressDialog.dismiss();

            intent.putExtra("emotion_data",emotions);

            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
