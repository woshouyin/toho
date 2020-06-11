package com.example.app2.touho.elements;

import com.example.app2.touho.Touho;
import com.example.app2.touho.stage.Stage;

import java.util.HashSet;

import javax.microedition.khronos.opengles.GL10;

public class Element {
    private static Long ID_COUNT = 0L;

    protected Touho touho = null;
    protected Element parrent = null;
    private int layer;
    private int group;
    protected HashSet<Element> subElems = null;
    protected boolean alive = true;
    private boolean drawable = true;
    private boolean bindSubElms = false; //在销毁同时清除所有子元素（包括隔代的子元素）
    protected boolean loaded = false;
    protected boolean started = false;
    private Long id = 0L;



    public Element(Touho touho, int layer, int group){
        this.touho = touho;
        this.layer = layer;
        this.group = group;
        id = ID_COUNT++;
    }

    public void draw(GL10 gl, float[] prjMatrix) {
    }
    /**
     * 加载绘图有关的资源
     */
    public void load(){
        loaded = true;
    }

    public void onCreate(){ }

    public void tick(){
        if(!started){
            onCreate();
            started = true;
        }
    }

    public void onSurfaceChanged(){

    }

    public int getGroup() {
        return  group;
    }

    public int getLayer(){
        return layer;
    }


    public void addSubElement(Element e){
        if(subElems == null) subElems = new HashSet<>();
        e.parrent = this;
        subElems.add(e);
        touho.addElem(e);
    }

    private void removeSubElement(Element e){
        if(subElems == null) throw new RuntimeException("Have no subElements");
        if(!subElems.remove(e)) throw new RuntimeException("No such subElement");
    }

    public boolean isDrawable(){
        return drawable;
    }

    public void setDrawable(boolean drawable){
        this.drawable = drawable;
    }

    public boolean isBindSubElms() {
        return bindSubElms;
    }

    public long getId() {
        return id;
    }

    public Touho getTouho(){
        return touho;
    }

    public void setLayer(int layer) {
        int l = getLayer();
        this.layer = layer;
        touho.flushElementLayer(this, l);
    }

    /**
     * 用于判断是否需要调用load方法
     */
    public boolean isLoaded(){
        return loaded;
    }

    public void setBindSubElms(boolean bindSubElms){
        this.bindSubElms = bindSubElms;
    }

    /**递归清除*/
    private void clearDeep(){
        if(subElems != null){
            for(Element e:subElems){
                e.clearDeep();
                touho.delElem(e);
            }
        }
    }

    protected void onDestroy(){
        if(parrent != null) parrent.removeSubElement(this);
        if(bindSubElms){
            clearDeep();
        }
        touho.delElem(this);
    }

    public void die(){
        alive = false;
    }

    public void doCheckAlive(){
        if(!alive) this.onDestroy();
    }

    public boolean isAlive() {
        return alive;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
