package com.example.app2.touho;

import android.content.Context;
import android.content.SyncStatusObserver;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.view.MotionEvent;

import com.example.app2.touho.elements.Element;
import com.example.app2.touho.elements.Touch.TouchElement;
import com.example.app2.touho.resouse.ResouseManager;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Touho {
    protected Context context;
    private Renderer render;
    private ResouseManager resouseManager;
    private int group = 0;
    ConcurrentHashMap<Long, Element> elems = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, ConcurrentHashMap<Long, Element>> layers = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Long, TouchElement> touchElems = new ConcurrentHashMap<>();
    private float ratio = 0.618f; //宽长比
    private int screenWidth;
    private int screenHeight;
    private float vTop;
    private float vBottom;
    private float vLeft;
    private float vRight;
    private float vWidth;
    private float vHeight;
    private long ticktime = 0;
    private long id = 0;
    private boolean pause = false;
    ActivityOp aco = null;
    private Random rand;

    public Touho(Context context, long seed) {
        this.context = context;
        this.render = new TouhoRender();
        this.resouseManager = new ResouseManager(context);
        this.rand = new Random(seed);
        loadResouse();
    }


    protected void loadResouse(){}

    protected void loadGlResouce(){}

    public void start(){}

    public void tick(){
        if(!pause){
            ticktime++;
            for (Object obj : elems.values().toArray()) {
                Element e = (Element) obj;
                e.doCheckAlive();//保证生命周期结束后还有一个tick用于执行死亡指令
                if(e.getGroup() == group) {
                    e.tick();
                }
            }
        }
    }

    public void touch(MotionEvent event) {

        int ac = -1;
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                ac = MotionEvent.ACTION_DOWN;
                break;
            case MotionEvent.ACTION_MOVE:
                ac = MotionEvent.ACTION_MOVE;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_OUTSIDE:
                ac = MotionEvent.ACTION_UP;
                break;
        }

        if(ac == MotionEvent.ACTION_DOWN || ac == MotionEvent.ACTION_UP){
            for(TouchElement e: touchElems.values()){
                if(e.getGroup() == group) {
                    int i = event.getActionIndex();
                    e.touchCheck(vLeft + event.getX(i) / screenWidth * vWidth, vTop - event.getY(i) / screenHeight * vHeight, ac, i);
                }
            }
        }else if(ac == MotionEvent.ACTION_MOVE){
            for(int i = 0; i < event.getPointerCount(); i++){
                for(TouchElement e: touchElems.values()){
                    if(e.getGroup() == group) {
                        e.touchCheck(vLeft + event.getX(i) / screenWidth * vWidth, vTop - event.getY(i) / screenHeight * vHeight, ac, i);
                    }
                }
            }
        }


    }

    public void flushElementLayer(Element e, int lastLayer) {
        layers.get(lastLayer).remove(e.getId());
        ConcurrentHashMap<Long, Element> l = layers.get(e.getLayer());
        if (l == null) {
            l = new ConcurrentHashMap<>();
            l.put(e.getId(), e);
            layers.put(e.getLayer(), l);
        } else {
            l.put(e.getId(), e);
        }
    }
    public void addElem(Element e){
        if(!elems.contains(e.getId())) {
            elems.put(e.getId(), e);

            ConcurrentHashMap<Long, Element> l = layers.get(e.getLayer());
            if (l == null) {
                l = new ConcurrentHashMap<>();
                l.put(e.getId(), e);
                layers.put(e.getLayer(), l);
            } else {
                l.put(e.getId(), e);
            }

            if (e instanceof TouchElement) {
                touchElems.put(e.getId(), (TouchElement) e);
            }
        }
    }

    public void delElem(Element e){
        ConcurrentHashMap<Long, Element> l = layers.get(e.getLayer());
        if (l == null || !l.remove(e.getId(), e) || !elems.remove(e.getId(), e)) {
            //throw new RuntimeException("Unknown element");
        }
        if(e instanceof TouchElement){
            touchElems.remove(e);
        }
    }

    public ResouseManager getResouseManager(){
        return resouseManager;
    }

    public Renderer getRender(){
        return render;
    }


    public float getTop(){
        return 1;
    }

    public float getBottom(){
        return -1;
    }

    public float getLeft(){
        return -ratio;
    }

    public float getRight(){
        return ratio;
    }

    public float getvTop() {return vTop;}
    public float getvBottom() {return vBottom;}
    public float getvLeft(){return vLeft;}
    public float getvRight(){return vRight;}

    public float getWidth(){
        return 2 * ratio;
    }

    public float getHeight(){
        return 2;
    }

    public void setGroup(int group){
        this.group = group;
    }

    public long getTicktime() {
        return ticktime;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public Random getRand() {
        return rand;
    }

    class TouhoRender implements Renderer {
        /**视窗矩阵*/
        private final float[] projectionMatrix = new float[16];
        private final float[] lookatMatrix = new float[16];
        private final float[] mvpMatrix = new float[16];

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            gl.glEnable(GL10.GL_BLEND);
            gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);//开颜色混合
            gl.glClearColor(0.6f, 0.36f, 0.48f, 1f);
            loadGlResouce();
            start();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            gl.glViewport(0, 0, width, height);
            screenWidth = width;
            screenHeight = height;
            float sratio = (float) width / height;
            float sw = 0;
            float sh = 0;
            if(sratio >= ratio){//屏幕宽于目标
                vTop = 1.0f;
                vBottom = -1.0f;
                vLeft = -sratio;
                vRight = sratio;
            }else{//屏幕窄于目标
                vTop = 1.0f;
                vBottom = 1 - 2.0f * ratio/sratio;
                vLeft = -ratio;
                vRight = ratio;
            }
            vWidth = vRight - vLeft;
            vHeight = vTop - vBottom;
            Matrix.frustumM(projectionMatrix, 0, 0.5f*vLeft, 0.5f * vRight, 0.5f * vBottom , 0.5f * vTop, 1f, 7);//截锥体矩阵
            Matrix.setLookAtM(lookatMatrix, 0
                    , 0, 0, 2.0f
                    ,0, 0,0
                    ,0,1,0);
            Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, lookatMatrix, 0);
            for(Element e:elems.values()){
                e.onSurfaceChanged();
            }
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
            for (int i : layers.keySet()) {
                for (Element e : layers.get(i).values()) {
                    if (e.isDrawable() && e.getGroup() == group) {
                        if (!e.isLoaded()) {
                            e.load();
                        }
                        e.draw(gl, mvpMatrix);
                    }
                }
            }
        }
    }



    public void setAco(ActivityOp aco) {
        this.aco = aco;
    }

    public ActivityOp getAco() {
        return aco;
    }
}
