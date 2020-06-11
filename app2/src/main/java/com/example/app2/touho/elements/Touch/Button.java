package com.example.app2.touho.elements.Touch;

import android.view.MotionEvent;

import com.example.app2.touho.Touho;
import com.example.app2.touho.listener.ClickListener;
import com.example.app2.touho.utils.Frame;
import com.example.app2.touho.utils.ShaderUtil;

public class Button extends TouchElement{
    private float height;
    private float width;
    ClickListener listener = null;

    public Button(Touho touho, int layer, int group, float x, float y, float height, float width) {
        super(touho, layer, group);
        setPositon(x, y);
        this.height = height;
        this.width = width;
    }

    public void setTexture(int textureId, float x1, float y1, float x2, float y2){
        setFrame(Frame.getInstance(touho.getResouseManager().getTexture(textureId), 4
                , ShaderUtil.getRectVtxBuffer(-width / 2, height / 2, width / 2, -height / 2)
                , ShaderUtil.getRectVtxBuffer(x1, y1, x2, y2)));
    }

    public void setListener(ClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onTouchEvent(float touchX, float touchY, int action) {
        switch (action){
            case MotionEvent.ACTION_DOWN:
                setScale(0.8f);
                break;
            case MotionEvent.ACTION_UP:
                setScale(1.0f);
                if(listener != null)
                    listener.onClick();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }
    }

    @Override
    public boolean pressingCheck(float x, float y) {
        return Math.abs(x - this.x) <= width / 2 && Math.abs(y - this.y) <= height / 2;
    }
}
