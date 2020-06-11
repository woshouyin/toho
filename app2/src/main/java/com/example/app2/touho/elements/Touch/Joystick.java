package com.example.app2.touho.elements.Touch;

import android.view.MotionEvent;

import com.example.app2.R;
import com.example.app2.Texture;
import com.example.app2.touho.Touho;
import com.example.app2.touho.utils.Frame;
import com.example.app2.touho.utils.MathUtil;
import com.example.app2.touho.utils.ShaderUtil;

import javax.microedition.khronos.opengles.GL10;

public class Joystick extends TouchElement {
    private float radius = 0;
    private float smallX = 0;
    private float smallY = 0;
    private float[] sColor = new float[]{1, 1, 1, 1};
    private int action = 0; //操纵杆当前位置 0：中心 1~8 上～左上
    private float rad = 0;
    private float checkTop;
    private float checkLeft;
    private float checkRight;
    private float checkBottom;
    private boolean staticMode = true;

    public Joystick(Touho touho, int layer, int group, float radius, float x, float y) {
        super(touho, layer, group);
        setPositon(x, y);
        this.radius = radius;
        checkTop = y + radius;
        checkLeft = x - radius;
        checkBottom = y - radius;
        checkRight = x + radius;
    }

    public void setCheckBound(float checkTop, float checkLeft, float checkRight, float checkBottom){
        this.checkRight = checkRight;
        this.checkLeft = checkLeft;
        this.checkTop = checkTop;
        this.checkBottom = checkBottom;
    }

    public void setTexture(int textureId, float x1, float y1, float x2, float y2){
        setFrame(Frame.getInstance(touho.getResouseManager().getTexture(textureId)
                ,4
                , ShaderUtil.getRectVtxBuffer(-radius, radius, radius, -radius)
                ,ShaderUtil.getRectVtxBuffer(x1, y1, x2, y2)));
    }

    @Override
    public void onTouchEvent(float touchX, float touchY, int action) {
        switch (action){
            case MotionEvent.ACTION_DOWN:
                if(!staticMode) {
                    x = touchX;
                    y = touchY;
                }
                setAlpha(0.6f);
                break;
            case MotionEvent.ACTION_MOVE:
                rad = (float) Math.atan2(touchY - y, touchX - x);
                float r = (float) ((1 - Math.exp(-MathUtil.distance(touchX, touchY, x , y) * 2))* radius);
                if(r < radius * 0.0001){
                    this.action = 0;
                }else {
                    if(rad > 3/8f*Math.PI && rad<= 5/8f*Math.PI){
                        this.action = 1;
                    }else if(rad > 5/8f*Math.PI && rad<= 7/8f*Math.PI){
                        this.action = 8;
                    }else if(rad > 7/8f*Math.PI || rad <= -7/8*Math.PI){
                        this.action = 7;
                    }else if(rad > -7/8f*Math.PI && rad <= -5/8f*Math.PI){
                        this.action = 6;
                    }else if(rad > -5/8f*Math.PI && rad <= -3/8f*Math.PI){
                        this.action = 5;
                    }else if(rad > -3/8f*Math.PI && rad<= -1/8f*Math.PI){
                        this.action = 4;
                    }else if(rad > -1/8f*Math.PI && rad<= 1/8f*Math.PI){
                        this.action = 3;
                    }else if(rad > 1/8f*Math.PI && rad<= 3/8f*Math.PI) {
                        this.action = 2;
                    }
                }
                if(action != 0) {
                    smallX = (float) (r * Math.cos(rad));
                    smallY = (float) (r * Math.sin(rad));
                }else {
                    smallX = 0;
                    smallY = 0;
                }
                break;
            case MotionEvent.ACTION_UP:
                setAlpha(1.0f);
                smallX = 0;
                smallY = 0;
                this.action = 0;
                break;
        }
    }

    @Override
    public void draw(GL10 gl, float[] mvpMatrix) {
        super.draw(gl, mvpMatrix);
        drawFrame(frames[playingFrame], x + smallX, y + smallY, roatate, 0.3f, 0.3f, sColor, mvpMatrix);
    }

    @Override
    public boolean pressingCheck(float x, float y) {
        if(staticMode) {
            return MathUtil.distance(this.x, this.y, x, y) <= radius;
        }else {
            return x > checkLeft && x < checkRight && y > checkBottom && y < checkTop;
        }
    }

    public float getRad(){
        return rad;
    }
    public int getAction(){
        return action;
    }

    public boolean isStaticMode() {
        return staticMode;
    }

    public void setStaticMode(boolean staticMode) {
        this.staticMode = staticMode;
    }
}
