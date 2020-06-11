package com.example.app2.touho;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.example.app2.MainActivity;
import com.example.app2.R;
import com.example.app2.touho.elements.Background;
import com.example.app2.touho.elements.Touch.Button;
import com.example.app2.touho.elements.Touch.Joystick;
import com.example.app2.touho.listener.ClickListener;
import com.example.app2.touho.stage.Stage;
import com.example.app2.touho.stage.Stage1;
import com.example.app2.touho.utils.ReplayLogger;

import java.util.Calendar;


public class Natsu extends Touho{
    Stage stage = null;

    public Natsu(Context context, long seed) {
        super(context, seed);
    }

    @Override
    protected void loadResouse(){
        super.loadResouse();
        //getResouseManager().playBgm(R.raw.th06_04, 10);
        getResouseManager().loadSound(R.raw.se_cancel00);
        getResouseManager().loadSound(R.raw.se_plst00);
        getResouseManager().loadSound(R.raw.se_pldead00);
        getResouseManager().loadSound(R.raw.se_damage00);
        getResouseManager().loadSound(R.raw.se_tan01);
    }


    @Override
    protected void loadGlResouce(){
        super.loadGlResouce();
        int ids[] = new int[]{R.raw.aa, R.raw.loading02, R.raw.pl00, R.raw.player1, R.raw.title01
                            , R.raw.etama2, R.raw.stg2bg, R.raw.etama, R.raw.enemy};
        getResouseManager().loadTextrues(ids);
        getResouseManager().loadProgram(R.raw.shdcd_texture_vtx, R.raw.shdcd_texture_frg);
    }

    @Override
    public void start() {
        super.start();
        Background bg = new Background(this, 0, R.raw.loading02, Background.BG_WIND);
        //开始按钮
        Button startButton = new Button(this,1,0,0,0.6f,0.3f,0.5f * 1.55f*2/3f);
        startButton.setTexture(R.raw.title01, 0, 0, 155.0f/512, 30.0f/512);
        startButton.setListener(new ClickListener() {
            @Override
            public void onClick() {
                Stage stage1 = new Stage1(Natsu.this, 0, 1, Calendar.getInstance().getTimeInMillis());
                addElem(stage1);
                stage = stage1;
                setGroup(1);
                stage1.start();
            }
        });
        //选项按钮
        Button optionButton = new Button(this, 1, 0, 0, 0.3f, 0.2f, 0.5f*2/3f);
        optionButton.setTexture(R.raw.title01, 0, 224/512f, 100/512f, 256/512f);
        optionButton.setListener(new ClickListener() {
            @Override
            public void onClick() {
                aco.callReplayDialog();;
            }
        });
        addElem(bg);
        addElem(startButton);
        addElem(optionButton);
        //重播按钮
        Button replayButton = new Button(this, 1, 0, 0, 0.1f, 0.2f, 0.5f*2/3f);
        replayButton.setTexture(R.raw.title01, 0, 96/512f, 100/512f, 128/512f);
        replayButton.setListener(new ClickListener() {
            @Override
            public void onClick() {
                aco.callReplayDialog();;
            }
        });
        addElem(bg);
        addElem(startButton);
        addElem(optionButton);
        addElem(replayButton);
        getResouseManager().playBgm(R.raw.th06_04, 1000, 1);
    }

    public void replay(ReplayLogger rl){
        Stage stage1 = new Stage1(Natsu.this, 0, 1, rl);
        addElem(stage1);
        stage = stage1;
        setGroup(1);
        stage1.start();

    }

    public void restart(){
        stage.die();
        Stage stage1 = new Stage1(Natsu.this, 0, 1, Calendar.getInstance().getTimeInMillis());
        addElem(stage1);
        stage = stage1;
        setGroup(1);
        stage1.start();
    }

    public void quit(){
        setGroup(0);
        if(stage != null){
            stage.die();
            stage = null;
        }
    }
}
