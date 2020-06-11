package com.example.app2.touho.elements.Bullet;

import android.opengl.GLES20;

import com.example.app2.R;
import com.example.app2.touho.Touho;
import com.example.app2.touho.elements.AnmElement;
import com.example.app2.touho.elements.StageElement;
import com.example.app2.touho.stage.Stage;

public abstract class Bullet extends StageElement {
    protected boolean destroyClear = true;         //清屏时销毁
    protected boolean destroyCrash = true;                //中弹时销毁
    public Bullet(Stage stage, int layer, int group) {
        super(stage, layer, group);
    }

    @Override
    public void tick() {
        super.tick();
    }

    public boolean isDestroyClear() {
        return destroyClear;
    }

    public void setDestroyClear(boolean destroyClear) {
        this.destroyClear = destroyClear;
    }

    public boolean isDestroyCrash() {
        return destroyCrash;
    }

    public void setDestroyCrash(boolean crash) {
        this.destroyCrash = crash;
    }
}
