package com.example.app2.touho.controler;

import com.example.app2.touho.elements.StageElement;
import com.example.app2.touho.stage.Stage;

public class LineControler extends Controler{
    private float[] start = new float[]{0, 0};          //开始点
    private float[] end = new float[]{0, 0};            //结束点
    private float t1 = 0;               //加速开始点
    private float t2 = 1;               //减速开始点
    private float t;                    //时间
    private float u;                    //位置
    private float v;                    //最大速度
    private float v0;                   //初速度
    private float v1;                   //末速度

    public LineControler(float startX, float startStgY, float endX, float endStgY, float v0, float v1, float t1, float t2, int playTick) {
        super();
        start[0] = startX;
        start[1] = startStgY;
        end[0] = endX;
        end[1] = endStgY;
        this.t1 = t1;
        this.t2 = t2;
        this.v0 = v0;
        this.v1 = v1;
        setPlayTick(playTick);
        v = (2 - v0*t1 + v1*t2 - v1)/(t2 - t1 + 1);
    }

    @Override
    public void bind(StageElement elm) {
        super.bind(elm);
        start[1] += stg.getCenterY();
        end[1] += stg.getCenterY();
    }

    @Override
    public void tick() {
        super.tick();
        t = (float)tickTime / playTick;
        if(t > 1){
            end();
        }else if(t < t1){
            u = v0 * t + (t / t1) * (v - v0) * t / 2;
        }else if(t <= t2){
            u = u + v;
            u = (v0 + v) * t1 / 2 + v * (t - t1);
        }else {
            u = 1 - (1-t)/(1-t2)*v*(1-t)/2 ;
        }
        elm.setPositon(start[0] * (1 - u) + end[0] * u, start[1] * (1 - u) + end[1] * u);
        if(tickTime == playTick){
            end();
        }
    }
}
