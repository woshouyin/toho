package com.example.app2.touho.utils;

import android.opengl.GLES20;

import com.example.app2.touho.resouse.ResouseManager;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class ShaderUtil {
    public static int genProgram(String vertexShaderCode, String fragmentShaderCode){
        int vs = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        int fs = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        int prog = GLES20.glCreateProgram();
        GLES20.glShaderSource(vs, vertexShaderCode);
        GLES20.glShaderSource(fs, fragmentShaderCode);
        GLES20.glCompileShader(vs);
        GLES20.glCompileShader(fs);
        GLES20.glAttachShader(prog, vs);
        GLES20.glAttachShader(prog, fs);
        GLES20.glLinkProgram(prog);
        return prog;
    }

    public static void setTextureParm(int warp, int fliter){
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, warp);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, warp);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, fliter);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, fliter);
    }

    public static FloatBuffer vtxChange(float[] vertexData){
        FloatBuffer vertexBuffer = ByteBuffer.allocateDirect(vertexData.length * 4)
                .order((ByteOrder.nativeOrder()))
                .asFloatBuffer()
                .put(vertexData);
        vertexBuffer.position(0);
        return vertexBuffer;
    }

    public static FloatBuffer getRectVtxBuffer(float x1, float y1, float x2, float y2){
        return vtxChange(new float[]{
                x1, y2, 0,
                x2, y2, 0,
                x1, y1, 0,
                x2, y1, 0
        });
    }

    public static FloatBuffer getXFlipRectVtxBuffer(float x1, float y1, float x2, float y2){
        return vtxChange(new float[]{
                x2, y2, 0,
                x1, y2, 0,
                x2, y1, 0,
                x1, y1, 0
        });
    }

}
