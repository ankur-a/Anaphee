package com.example.ankur.anaphee;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class statistics extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mdrawerLayout;
    private ActionBarDrawerToggle mToggle;
    DatabaseHelper mydb;
    TextView min,max,avg;
    Button btnAddData,viewallBtn;
    GraphView graphView;
    LineGraphSeries<DataPoint> series;
    int minV=2000,maxV=0,avgV;
    SimpleDateFormat sdf = new SimpleDateFormat("M hh:mm:ss");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        min = (TextView)findViewById(R.id.min);
        max = (TextView)findViewById(R.id.max);
        avg = (TextView)findViewById(R.id.avg);
        mdrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this,mdrawerLayout,R.string.open,R.string.close);
        mdrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationview= findViewById(R.id.nav_view);
        navigationview.bringToFront();
        navigationview.setNavigationItemSelectedListener(this);

        mydb = new DatabaseHelper(this);
        graphView=(GraphView) findViewById(R.id.grpahview);
        series = new LineGraphSeries<>(getDataPoint());
        series.setColor(Color.RED);
        graphView.setTitle("Today");
        graphView.addSeries(series);

        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if(isValueX){
                    return sdf.format(new Date((long)value));
                }else
                return super.formatLabel(value, isValueX);
            }
        });

        graphView.getGridLabelRenderer().setNumHorizontalLabels(5);
        graphView.getGridLabelRenderer().setHumanRounding(false);
        min.setText(String.valueOf(minV));
        max.setText(String.valueOf(maxV));
        avg.setText(String.valueOf(avgV));


    }

    private DataPoint[] getDataPoint() {
        Cursor res = mydb.getAllData();
        DataPoint[] dp = new DataPoint[res.getCount()];
        int sum=0;
        for(int i=0;i<res.getCount();i++){
            res.moveToNext();
            int tmp= res.getInt(3);
            if(tmp<minV) minV=tmp;
            if(tmp>maxV) maxV=tmp;
            sum+=tmp;
            dp[i]=new DataPoint(res.getLong(2),res.getInt(3));
        }
        avgV=sum/res.getCount();

        return dp;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //Log.e("here","akjakdjaskjd");
        int id=item.getItemId();
        //Toast.makeText(this,"SecondActivity",Toast.LENGTH_SHORT).show();
        if (id == R.id.home){
            //Toast.makeText(this,"SecondActivity",Toast.LENGTH_SHORT).show();
            Intent i= new Intent(this, MainActivity.class);
            startActivity(i);
        }
        if (id == R.id.statistics){
            //Toast.makeText(this,"SecondActivity",Toast.LENGTH_SHORT).show();
            Intent i= new Intent(this, statistics.class);
            startActivity(i);
        }
        if(id==R.id.history){
            Intent i= new Intent(this, history.class);
            startActivity(i);
        }
        if(id == R.id.instructions){
            Toast.makeText(this,"Instructions",Toast.LENGTH_SHORT).show();
        }
        if(id==R.id.about){
            Toast.makeText(this,"About",Toast.LENGTH_SHORT).show();
        }
        mdrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mdrawerLayout.closeDrawers();
        return false;
    }





}
