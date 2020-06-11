package com.example.app2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.charset.CoderResult;

public class Texture {
    private static int mProgram = -1;
    private final String vertexShaderCode=
                    "attribute vec4 av_Position;\n" +
                            "attribute vec2 af_Position;\n" +
                            "uniform mat4 uMVPMatrix;\n" +
                            "varying vec2 v_texPo;\n" +
                            "void main() {\n" +
                            "    v_texPo = af_Position;\n" +
                            "    gl_Position = uMVPMatrix*av_Position;\n" +
                            "}";
    private final String fragmentShaderCode=
                        "precision mediump float;\n" +
                                "varying vec2 v_texPo;\n" +
                                "uniform sampler2D sTexture;\n" +
                                "void main() {\n" +
                                "    gl_FragColor=texture2D(sTexture, v_texPo);\n" +
                                "}";
    private static int avPosition;
    private static int afPosition;
    private static int mMVPMatrixHandle;
    private Bitmap texture;
    private FloatBuffer vertexBuffer;
    private FloatBuffer textureBuffer;
    private FloatBuffer mposBuffer;
    static final int COORDS_PER_VERTEX = 3;
    static float vertexData[] = {   // in counterclockwise order:
            -0.4f, -0.5f, 0.0f, // bottom left
            0.4f, -0.5f, 0.0f, // bottom right
            -0.4f, 0.5f, 0.0f, // top left
            0.4f, 0.5f, 0.0f  // top right
    };

    //纹理坐标  对应顶点坐标  与之映射
    static float textureData[] = {   // in counterclockwise order:
            0f, 0.18f, 0.0f, // bottom left
            0.13f, 0.18f, 0.0f, // bottom right
            0f, 0f, 0.0f, // top left
            0.13f, 0f, 0.0f  // top right
    };

    private final int vertexCount = vertexData.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4;
    static int[] textureIds = new int[1];
    private int id = 0;

    public Texture(Bitmap texture, int id){
        this.id = id;
        vertexBuffer = ByteBuffer.allocateDirect(vertexData.length * 4)
                        .order((ByteOrder.nativeOrder()))
                        .asFloatBuffer()
                        .put(vertexData);
        vertexBuffer.position(0);
        this.texture = texture;
        textureBuffer = ByteBuffer.allocateDirect(textureData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(textureData);
        textureBuffer.position(0);
        if(mProgram == -1) {
            int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
            int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
            mProgram = GLES20.glCreateProgram();
            GLES20.glAttachShader(mProgram, vertexShader);
            GLES20.glAttachShader(mProgram, fragmentShader);
            GLES20.glLinkProgram(mProgram);
            GLES20.glGenTextures(1, textureIds, 0);

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[0]);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, texture, 0);

        }
        //环绕（超出纹理坐标范围）  （s==x t==y GL_REPEAT 重复）
        if (id == 0){
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        }else{
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        }
    }

    public  boolean show = false;
    public void draw(float[] mMVPMatrix){
        if(show) {

            avPosition = GLES20.glGetAttribLocation(mProgram, "av_Position");
            //获取纹理坐标字段
            afPosition = GLES20.glGetAttribLocation(mProgram, "af_Position");
            mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
            GLES20.glUseProgram(mProgram);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
            //过滤（纹理像素映射到坐标点）  （缩小、放大：GL_LINEAR线性）
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[0]);
            GLES20.glEnableVertexAttribArray(avPosition);
            GLES20.glEnableVertexAttribArray(afPosition);
            GLES20.glVertexAttribPointer(avPosition, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);
            GLES20.glVertexAttribPointer(afPosition, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, textureBuffer);
            GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vertexCount);
            GLES20.glDisableVertexAttribArray(avPosition);
            GLES20.glDisableVertexAttribArray(afPosition);
        }
    }

    public static int loadShader(int type, String shaderCode){
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
}
