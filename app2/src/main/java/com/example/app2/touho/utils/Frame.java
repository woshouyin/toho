package com.example.app2.touho.utils;

import java.nio.FloatBuffer;

public class Frame {
    public int texture;
    public int vtxCount;
    public FloatBuffer texBuffer;
    public FloatBuffer vtxBuffer;

    private Frame(int texture, int vtxCount, FloatBuffer vtxBuffer, FloatBuffer texBuffer){
        this.texture = texture;
        this.vtxCount = vtxCount;
        this.vtxBuffer = vtxBuffer;
        this.texBuffer = texBuffer;
    }

    public static Frame getInstance(){
        return new Frame(-1, 0, null, null);
    }

    public static Frame getInstance(int texture, int vtxCount, FloatBuffer vtxBuffer, FloatBuffer texBuffer){
        return new Frame(texture, vtxCount, vtxBuffer, texBuffer);
    }
}
