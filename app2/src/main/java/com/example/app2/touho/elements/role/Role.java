package com.example.app2.touho.elements.role;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.example.app2.touho.Touho;
import com.example.app2.touho.elements.AnmElement;
import com.example.app2.touho.elements.StageElement;
import com.example.app2.touho.stage.Stage;
import com.example.app2.touho.utils.ShaderUtil;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Role extends StageElement {
    public final static int ACTION_STOP = 0;
    public final static int ACTION_LEFT = 1;
    public final static int ACTION_RIGHT = 2;
    public final static int ID_SR = 3;
    public final static int ID_SL = 4;


    protected int anms[][] = null;
    protected int playingAnmFrame = 0; //正在播出的帧
    protected int playStep = 3; //单帧tick数
    protected int playCount = 0;
    private int lr = 0;
    private int action;

    public Role(Stage stg, int layer, int group) {
        super(stg, layer, group);
    }

    @Override
    public void tick() {
        super.tick();
        if(anms != null && frames != null) {
            updateFrame();
        }
    }

    private void updateFrame(){
        boolean flag = false;
        switch (action){
            case ACTION_STOP:
                flag = lr == 0;
                if(!flag){
                    if (lr > 0) {
                        lr--;
                    } else {
                        lr++;
                    }
                }
                break;
            case ACTION_LEFT:
                flag = lr == -anms[ID_SL].length;
                if(!flag) lr--;
                break;
            case ACTION_RIGHT:
                flag = lr == anms[ID_SR].length;
                if(!flag) lr++;
                break;
        }
        if(flag) {
            if (playCount++ == playStep) {
                playingAnmFrame = (playingAnmFrame + 1) % anms[action].length;
                playingFrame = anms[action][playingAnmFrame];
                playCount = 0;
            }
        }else {
            if(lr > 0) {
                playingFrame = anms[ID_SR][lr - 1];
            }else if(lr < 0){
                playingFrame = anms[ID_SL][-lr - 1];
            }else {
                playingFrame = anms[ACTION_STOP][0];
            }
        }
    }

    public void setAction(int action){
        this.action = action;
    }

    /**
     * @param anms 动画组stop left right stop2right stop2left
     */
    public void setAnms(int anms[][]){
        this.anms = anms;
    }

    public void setPlayStep(int step){
        this.playStep = step;
    }

    @Override
    public void load() {
        super.load();
    }


}
