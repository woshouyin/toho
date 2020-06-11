package com.example.app2.touho.resouse;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Measure;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.example.app2.R;
import com.example.app2.touho.utils.ShaderUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;

public class ResouseManager {
    private Context context;
    private MediaPlayer bgmPlayer;
    private float bgmVolum;
    private boolean bgmFlag;
    private HashMap<Integer, Object> resouse = new HashMap<>();
    private HashMap<Integer, Integer> textures = new HashMap<>();
    private HashMap<Integer, Integer> programs = new HashMap<>();
    private HashMap<Integer, Integer> soundId = new HashMap<>();
    private SoundPool soundPool;

    public ResouseManager(Context context){
        this.context = context;
        soundPool = new SoundPool.Builder().setMaxStreams(1000).build();
    }

    public void loadBitmap(int id){
        if(resouse.containsKey(id)) return;
        resouse.put(id, BitmapFactory.decodeResource(context.getResources(), id));
    }

    public void loadString(int id){
        if(resouse.containsKey(id)) return;
        InputStream is = context.getResources().openRawResource(id);
        try {
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            resouse.put(id, new String(buffer));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void loadTextrues(int ids[]){
        int ts[] = new int[ids.length];
        Bitmap ms = null;
        GLES20.glGenTextures(ids.length, ts, 0);
        for(int i = 0; i < ids.length; i++){
            if(!resouse.containsKey(ids[i])){
                loadBitmap(ids[i]);
            }
            ms = getBitmap(ids[i]);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, ts[i]);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, ms, 0);
            textures.put(ids[i], ts[i]);
        }
    }

    public void loadSound(int id){
        soundId.put(id, soundPool.load(context, id, 1));
    }

    /**
     * @return streamID
     */
    public int playSound(int id, float volums){
        return playSound(id, volums, 0, 1);
    }

    /**
     * @return streamID
     */
    public int playSound(int id, float volum, int loop, float rate){
        return soundPool.play(soundId.get(id), volum, volum, 1, loop, rate);
    }

    public void stopSound(int streamId){
        soundPool.stop(streamId);
    }

    public void playBgm(int id, final long inTime, final float volum){
        if(bgmPlayer != null) stopBgm(0);
        bgmPlayer = MediaPlayer.create(context, id);
        if(inTime == 0){
            bgmVolum = volum;
            bgmPlayer.setVolume(bgmVolum, bgmVolum);
            bgmPlayer.start();
        }else {
            bgmVolum = 0;
            bgmPlayer.setVolume(bgmVolum, bgmVolum);
            bgmPlayer.start();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    bgmFlag = true;
                    float incVolum = volum / inTime;
                    for (int i = 0; i < inTime && bgmFlag; i++) {
                        bgmVolum += incVolum;
                        bgmPlayer.setVolume(bgmVolum, bgmVolum);
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    bgmPlayer.setVolume(volum, volum);
                }
            });
            thread.start();
        }
    }

    public void stopBgm(final long outTime){
        if(bgmPlayer != null) {
            if(bgmPlayer.isPlaying()) {
                if (outTime == 0) {
                    bgmPlayer.stop();
                    bgmPlayer.release();
                    bgmPlayer = null;
                } else {
                    bgmFlag = false;
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            float decVolum = bgmVolum / outTime;
                            for (int i = 0; i < outTime; i++) {
                                bgmVolum -= decVolum;
                                bgmPlayer.setVolume(bgmVolum, bgmVolum);
                                try {
                                    Thread.sleep(1);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            bgmPlayer.stop();
                            bgmPlayer.release();
                            bgmPlayer = null;
                        }
                    });
                    thread.start();
                }
            }else{
                bgmPlayer.release();
                bgmPlayer = null;
            }
        }
    }

    /**
     * 加载Shader程序，以vtxCode作为索引
     * */
    public void loadProgram(int vtxCode, int frgCode){
        if(!resouse.containsKey(vtxCode)){
            loadString(vtxCode);
        }
        if(!resouse.containsKey(frgCode)){
            loadString(frgCode);
        }
        String vc = getString(vtxCode);
        String fc = getString(frgCode);
        int prog = ShaderUtil.genProgram(vc, fc);
        programs.put(vtxCode, prog);
    }

    public Bitmap getBitmap(int id){
        return (Bitmap) resouse.get(id);
    }

    public String getString(int id){
        return (String) resouse.get(id);
    }

    public int getTexture(int id){
        return textures.get(id);
    }

    public int getProgram(int id){
        return programs.get(id);
    }

}
