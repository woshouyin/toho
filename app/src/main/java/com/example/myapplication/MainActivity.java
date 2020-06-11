package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private Button buttonIce;
    private Button buttonThunder;
    private Button buttonFire;
    private TextView text;
    private TextView ipText;
    private ImageView[] imgs;
    private ImageView ei;
    private ProgressBar cd;
    private EditText calcInText;
    private TextView calcOutText;
    private int eiColdDown = 120;
    private int sts = 111;
    private int cdTime = 0;
    private ColorMatrix cm = new ColorMatrix();
    private ColorMatrixColorFilter cf;
    private int[] skillNames = {R.string.noSkill, R.string.skill1, R.string.skill2 , R.string.skill3
                            , R.string.skill4, R.string.skill5 , R.string.skill6 , R.string.skill7
                            , R.string.skill8, R.string.skill9, R.string.skill10};
    private int[] balls;
    private Vibrator vibrator;
    private String sip = "45.76.9.97";
    private int sport = 1989;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonIce = findViewById(R.id.buttonIce);
        buttonFire = findViewById(R.id.buttonFire);
        buttonThunder = findViewById(R.id.buttonThunder);
        calcInText = findViewById(R.id.calcInText);
        calcOutText = findViewById(R.id.calcOutText);
        text = findViewById(R.id.text);
        ipText = findViewById(R.id.ipText);
        imgs = new ImageView[]{findViewById(R.id.img1), findViewById(R.id.img2), findViewById(R.id.img3)};
        ei = findViewById(R.id.ei);
        balls = new int[]{R.mipmap.ice, R.mipmap.thunder, R.mipmap.fire};
        cd = findViewById(R.id.coldDown);
        cd.setMax(eiColdDown);
        cm.setSaturation(0);
        cf = new ColorMatrixColorFilter(cm);
        vibrator = (Vibrator)getApplication().getSystemService(Service.VIBRATOR_SERVICE);


        buttonIce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sts = sts * 10 % 1000 + 1;
            }
        });
        buttonThunder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sts = sts * 10 % 1000 + 2;
            }
        });
        buttonFire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sts = sts * 10 % 1000 + 3;
            }
        });
        ei.setOnClickListener(new ImageView.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(cdTime == 0){
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ei.setColorFilter(cf);
                            cdTime = eiColdDown;
                            while (cdTime > 0) {
                                cd.setProgress(cdTime);
                                cdTime--;
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            cd.setProgress(cdTime);
                            ei.setColorFilter(null);
                        }
                    });
                    thread.start();
                }else{
                    vibrator.vibrate(VibrationEffect.createOneShot(200, 100));
                }
            }
        });
    }

}
