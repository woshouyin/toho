package com.example.ex_5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
    ImageView img;
    ProgressBar pr;
    Button bt1,bt2,bt3,bt4;
    int index=1;
    private  int[] imgs=new int[]{R.raw.aa, R.raw.loading02, R.raw.pl00, R.raw.player1, R.raw.title01
            , R.raw.etama2, R.raw.stg2bg, R.raw.etama, R.raw.enemy};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex2);
        img=findViewById(R.id.img);
        img.setImageResource(imgs[index]);
        pr=findViewById(R.id.pr);
        bt1=findViewById(R.id.bt1);
        bt2=findViewById(R.id.bt2);
        bt1.setOnClickListener(new mClick());
        bt2.setOnClickListener(new mClick());
        bt3=findViewById(R.id.bt3);
        bt4=findViewById(R.id.bt4);
        bt3.setOnClickListener(new mClick());
        bt4.setOnClickListener(new mClick());
        pr.setMax(imgs.length);
    }
    class mClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(v==bt1){
                index = (index + imgs.length - 1) % imgs.length;
                img.setImageResource(imgs[index]);
                pr.setProgress(index + 1);
            }
            if(v==bt2){

                index = (index + 1) % imgs.length;
                img.setImageResource(imgs[index]);
                pr.setProgress(index + 1);
            }
            if(v==bt3){
                pr.incrementProgressBy(1);
            }
            if(v==bt4){
                pr.incrementProgressBy(-1);
            }


        }
    }
}