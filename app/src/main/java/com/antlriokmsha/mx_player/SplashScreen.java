package com.antlriokmsha.mx_player;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class SplashScreen extends Activity {
    TextView madeby,antriksh;
    ImageView player_icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        madeby=findViewById(R.id.made_by);
        antriksh=findViewById(R.id.antriksh);
        player_icon=findViewById(R.id.player_icon);
        Animation made_by= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);
        Animation antrikshani= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);
        Animation playericon= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce);
        player_icon.startAnimation(playericon);
        madeby.startAnimation(made_by);
        antriksh.startAnimation(antrikshani);

     new Handler().postDelayed(new Runnable() {
         @Override
         public void run() {
             startActivity(new Intent(SplashScreen.this,FoldersActivity.class));
             Animatoo.animateSplit(SplashScreen.this);
             finish();
         }
     },4000);



    }
}