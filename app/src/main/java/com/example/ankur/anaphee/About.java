package com.example.ankur.anaphee;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class About extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    final String instru= "Instructions";
    final String steps= "Step 1.Take off the safety cap\nStep 2.Hold the injector firmly in your hand.\nStep 3.Press it against your thigh firmly and hold it there.\nStep 4.Wait till the LED on top goes off\nStep 5.Remove the injector and massage the site for 10 seconds.";
    private DrawerLayout mdrawerLayout;
    private ActionBarDrawerToggle mToggle;
    TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        mdrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this,mdrawerLayout,R.string.open,R.string.close);
        mdrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        content =(TextView)findViewById(R.id.content);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationview= findViewById(R.id.nav_view);
        navigationview.bringToFront();
        navigationview.setNavigationItemSelectedListener(this);
        content.setText("•Real time heart beat\n\n•Notification on onset of attack\n\n•Notifying hospital about the attack via mail.\n\n•Track of heart beat stats.\n\n•Step by step help in using the \ninjector");

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
