package com.example.ankur.anaphee;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.TypefaceProvider;

public class history extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    final String instru= "Instructions";
    final String steps= "Step 1.Take off the safety cap\nStep 2.Hold the injector firmly in your hand.\nStep 3.Press it against your thigh firmly and hold it there.\nStep 4.Wait till the LED on top goes off\nStep 5.Remove the injector and massage the site for 10 seconds.";
    private DrawerLayout mdrawerLayout;
    private ActionBarDrawerToggle mToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypefaceProvider.registerDefaultIconSets();
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
    public void showMessage(String Title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(Title);
        builder.setMessage(Message);
        builder.show();

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
            showMessage(instru,steps);
        }
        if(id==R.id.about){
            Intent i= new Intent(this, About.class);
            startActivity(i);
        }
        mdrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mdrawerLayout.closeDrawers();
        return false;
    }
}
