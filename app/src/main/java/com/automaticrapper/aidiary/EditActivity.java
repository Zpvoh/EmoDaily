package com.automaticrapper.aidiary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class EditActivity extends Activity {
    private Diary diary = null;
    private TextView photoPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        String str=null;
        Intent intent = getIntent();
        String status = intent.getStringExtra("status");
        if (status.equals("edit")) {
            diary = (Diary) intent.getSerializableExtra("diary_data");
            if (diary != null) {
                EditText editTitle = (EditText) findViewById(R.id.editText_Title);
                EditText editContext = (EditText) findViewById(R.id.editText_Context);
                editTitle.setText(diary.title);
                editContext.setText(diary.context);
            } else {
                new AlertDialog.Builder(this).setTitle("错误").setMessage("读取日记失败！").setPositiveButton("返回", null).show();
                this.finish();
            }
        }

        photoPath = (TextView)findViewById(R.id.photo_path);
        Button btnBack = (Button) findViewById(R.id.button_back);
        Button btnSave = (Button) findViewById(R.id.button_save);
        Button btnUpload = (Button) findViewById(R.id.button_upload);

        btnBack.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditActivity.this.finish();
            }
        });

        btnSave.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //保存日记的代码写在这里
            }
        });

        btnUpload.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra("return-data", true);
                startActivityForResult(intent, 1);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1 && resultCode == RESULT_OK){
            photoPath.setText(data.getDataString());
            photoPath.setVisibility(View.VISIBLE);
        }
    }
}
