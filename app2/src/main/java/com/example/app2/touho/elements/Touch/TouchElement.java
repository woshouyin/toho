package com.example.app2.touho.elements.Touch;

import android.view.MotionEvent;

import com.example.app2.touho.Touho;
import com.example.app2.touho.elements.AnmElement;
import com.example.app2.touho.listener.ClickListener;

public abstract class TouchElement extends AnmElement {
    private float touchX;
    private float touchY;
    private boolean pres = false;
    private boolean lastState = false;
    private int finger = -1;

    public TouchElement(Touho touho, int layer, int group) {
        super(touho, layer, group);
    }
    public abstract boolean pressingCheck(float x, float y);
    public abstract void onTouchEvent(float touchX, float touchY, int action);


    @Override
    public void tick() {
        super.tick();
        if(!lastState && pres){
            onTouchEvent(touchX, touchY, MotionEvent.ACTION_DOWN);
        }else if(lastState && !pres){
            onTouchEvent(touchX, touchY, MotionEvent.ACTION_UP);
        }else if(pres){
            onTouchEvent(touchX, touchY, MotionEvent.ACTION_MOVE);
        }
        lastState = pres;
    }

    /**
     * @return 响应成功返回true
     */
    public void touchCheck(float x, float y, int action, int finger){
        //if(action != MotionEvent.ACTION_MOVE) System.out.println(finger + "->" + action);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if(this.finger == -1) {
                    if (pressingCheck(x, y)) {
                        touchX = x;
                        touchY = y;
                        pres = true;
                        this.finger = finger;
                    }
                }else if(finger <= this.finger){
                    this.finger++;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (pres && this.finger == finger) {
                    touchX = x;
                    touchY = y;
                }
                break;
            case MotionEvent.ACTION_UP:
                if(this.finger == finger) {
                    touchX = x;
                    touchY = y;
                    pres = false;
                    this.finger = -1;
                }else if(finger < this.finger){
                    this.finger--;
                }
                break;
            default:
                break;
        }
    }

    public int getFinger() {
        return finger;
    }

    public void setFinger(int finger) {
        this.finger = finger;
    }
}
