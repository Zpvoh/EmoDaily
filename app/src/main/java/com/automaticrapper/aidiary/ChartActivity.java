package com.automaticrapper.aidiary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;


public class ChartActivity extends Activity{
    LineChartView lineChartView;
    LinearLayout chartContainer;
    List<PointValue> values = new ArrayList<PointValue>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        chartContainer = (LinearLayout)findViewById(R.id.chart_container);

        Intent intent = getIntent();
        ArrayList<Float> emotions = (ArrayList<Float>) intent.getSerializableExtra("emotion_data");
        if(emotions != null){
            lineChartView = new LineChartView(this);
            lineChartView.setInteractive(true);
            lineChartView.setZoomType(ZoomType.VERTICAL);
            lineChartView.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);

            for(int i=0;i<emotions.size();i++){
                values.add(new PointValue(i + 1 , emotions.get(i)));
            }

            Line line = new Line(values).setColor(Color.BLUE).setCubic(true);
            List<Line> lines = new ArrayList<Line>();
            lines.add(line);

            LineChartData data = new LineChartData();
            data.setLines(lines);
            lineChartView.setLineChartData(data);

            chartContainer.addView(lineChartView);
        }else {
            new AlertDialog.Builder(this).setTitle("错误").setMessage("无法读取图表数据").setPositiveButton("返回",null).show();
            finish();
        }

    }
}
