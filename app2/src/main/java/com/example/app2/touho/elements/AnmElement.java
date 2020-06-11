package com.example.app2.touho.elements;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.example.app2.R;
import com.example.app2.touho.Touho;
import com.example.app2.touho.controler.Controler;
import com.example.app2.touho.utils.Frame;
import com.example.app2.touho.utils.ShaderUtil;

import java.nio.FloatBuffer;
import java.util.HashSet;

import javax.microedition.khronos.opengles.GL10;

public class AnmElement extends Element{
    protected static int program = -1;
    protected static int vPosition;
    protected static int fPosition;
    protected static int mvpMatrixHandle;
    protected static int colorHandle;
    private static float rtMatrix[] = new float[16];
    private static float detMatrix[] = new float[16];
    protected float x = 0;
    protected float y = 0;
    protected float roatate = 0;
    protected float scaleX = 1;
    protected float scaleY = 1;
    protected float color[];        //渲染颜色
    protected Frame[] frames = null;
    protected int playingFrame = 0;
    private int insMode = GLES20.GL_LINEAR;
    protected boolean destroyOutScreen = true;     //移出屏幕时销毁
    protected float drawSizeX = 0;
    protected float drawSizeY = 0;

    public AnmElement(Touho touho, int layer, int group) {
        super(touho, layer, group);
        color = new float[]{1, 1, 1, 1};
    }


    @Override
    public void tick() {
        super.tick();
        if(destroyOutScreen && isOutScreen()){
            die();
        }
    }

    @Override
    public void draw(GL10 gl, float[] mvpMatrix) {
        super.draw(gl, mvpMatrix);
        if(frames != null){
            drawFrame(frames[playingFrame], x, y, roatate, scaleX, scaleY, color, mvpMatrix, insMode);
        }
    }

    public static void drawFrame(Frame frame, float x, float y, float rotate, float scaleX, float scaleY, float color[], float[] mvpMatrix){
        drawFrame(frame, x, y, rotate, 0, 0, 1, scaleX, scaleY, color, mvpMatrix, GLES20.GL_LINEAR);
    }

    public static void drawFrame(Frame frame, float x, float y, float rotate, float scaleX, float scaleY, float color[], float[] mvpMatrix, int insMode){
        drawFrame(frame, x, y, rotate, 0, 0, 1, scaleX, scaleY, color, mvpMatrix, insMode);
    }


    public static void drawFrame(Frame frame, float x, float y, float rotate, float rx, float ry, float rz, float scaleX, float scaleY, float color[], float[] mvpMatrix, int insMode){
        if(frame != null) {
            rotate = (float) (rotate / Math.PI * 180);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, frame.texture);
            ShaderUtil.setTextureParm(GLES20.GL_REPEAT, insMode);
            GLES20.glUseProgram(program);
            GLES20.glEnableVertexAttribArray(vPosition);
            GLES20.glEnableVertexAttribArray(fPosition);
            GLES20.glUniform4fv(colorHandle, 1, color, 0);
            GLES20.glVertexAttribPointer(vPosition, 3, GLES20.GL_FLOAT, false, 3 * frame.vtxCount, frame.vtxBuffer);
            GLES20.glVertexAttribPointer(fPosition, 3, GLES20.GL_FLOAT, false, 3 * frame.vtxCount, frame.texBuffer);
            Matrix.setIdentityM(rtMatrix, 0);
            Matrix.translateM(rtMatrix, 0, x, y, 0);
            Matrix.rotateM(rtMatrix, 0, rotate, rx, ry, rz);
            Matrix.scaleM(rtMatrix, 0, scaleX, scaleY, 1);
            Matrix.multiplyMM(detMatrix, 0, mvpMatrix, 0, rtMatrix, 0);
            GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, detMatrix, 0);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, frame.vtxCount);
            GLES20.glDisableVertexAttribArray(vPosition);
            GLES20.glDisableVertexAttribArray(fPosition);
        }
    }

    @Override
    public void load() {
        super.load();
        if(program == -1) {
            program = touho.getResouseManager().getProgram(R.raw.shdcd_texture_vtx);
            vPosition = GLES20.glGetAttribLocation(program, "av_Position");
            //获取纹理坐标字段
            fPosition = GLES20.glGetAttribLocation(program, "af_Position");
            mvpMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");
            colorHandle = GLES20.glGetUniformLocation(program, "color");
        }
    }

    public float getX(){return x;}
    public float getY(){return y;}
    public float getRoatate(){return roatate;}
    public void setPositon(float x, float y){
        this.x = x;
        this.y = y;
    }
    public void setRotate(float rotate){this.roatate = rotate;}
    public void setScaleX(float scaleX){this.scaleX = scaleX;}
    public void setScaleY(float scaleY){this.scaleY = scaleY;}
    public void setScale(float scale){this.scaleX = scale; this.scaleY = scale;}
    public void setScale(float scaleX, float scaleY){this.scaleX = scaleX; this.scaleY = scaleY;}

    public float getScaleX() {
        return scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setColor(float r, float g, float b, float a){
        color[0] = r;
        color[1] = g;
        color[2] = b;
        color[3] = a;
    }
    public void setAlpha(float alpha){
        color[3] = alpha;
    }
    public void setColor(float r, float g, float b){
        color[0] = r;
        color[1] = g;
        color[2] = b;
    }
    public void setFrames(Frame[] frames){
        this.frames = frames;
    }
    public void setFrame(Frame frame){
        this.frames = new Frame[]{frame};
        playingFrame = 0;
    }

    public float getAlpha(){
        return color[3];
    }

    public void setPlayingFrame(int playingFrame){
        this.playingFrame = playingFrame;
    }
    public int getPlayingFrame(){
        return playingFrame;
    }

    public boolean isDestroyOutScreen() {
        return destroyOutScreen;
    }

    public void setDestroyOutScreen(boolean destroyOutScreen) {
        this.destroyOutScreen = destroyOutScreen;
    }


    public void setInsMode(int insMode) {
        this.insMode = insMode;
    }

    public boolean isOutScreen(){
        return x < touho.getLeft() - drawSizeX*scaleX || x >touho.getRight() + drawSizeX*scaleX
                || y > touho.getTop() + drawSizeY*scaleY || y < touho.getBottom() - drawSizeY*scaleY;
    }

    public void setDrawSize(float drawSizeX, float drawSizeY) {
        this.drawSizeX = drawSizeX;
        this.drawSizeY = drawSizeY;
    }
}
