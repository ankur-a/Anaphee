package com.example.ankur.anaphee;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

public class history extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mdrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mdrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this,mdrawerLayout,R.string.open,R.string.close);
        mdrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationview= findViewById(R.id.nav_view);
        navigationview.bringToFront();
        navigationview.setNavigationItemSelectedListener(this);
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
