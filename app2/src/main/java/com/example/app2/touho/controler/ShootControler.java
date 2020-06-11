package com.example.app2.touho.controler;

import com.example.app2.touho.stage.Stage;

public class ShootControler extends Controler{
    private float v;
    private float a;
    private float rad;
    private int t1;


    public ShootControler(float rad, float v0, float a, int t1) {
        super();
        this.v = v0;
        this.a = a;
        this.rad = rad;
        this.t1 = t1;
    }

    @Override
    public void tick() {
        super.tick();
        if(tickTime < t1){
            v += a;
        }
        elm.setPositon(elm.getX()+v*(float)Math.cos(rad), elm.getY()+v*(float)Math.sin(rad));
    }
}
