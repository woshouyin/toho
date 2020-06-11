package com.example.app2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.app2.touho.ActivityOp;
import com.example.app2.touho.Natsu;
import com.example.app2.touho.utils.ReplayLogger;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    GLSurfaceView glv;
    Natsu touho;
    Texture tr1 = null;
    Texture tr2 = null;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fain);
        glv = new GLSurfaceView(this);
        glv.setEGLContextClientVersion(2);
        setContentView(glv);
        touho = new Natsu(this, Calendar.getInstance().getTimeInMillis());
        touho.setAco(new ActivityOp() {
            private ReplayLogger replayLogger;
            Handler hd = new Handler(){
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                    if(msg.getData().get("Op").equals("callQuitDialog")){
                        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Quit");
                        builder.setMessage("确认返回主页面?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                touho.quit();
                                touho.setPause(false);
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                touho.setPause(false);
                            }
                        });
                        builder.setOnCancelListener(new DialogInterface.OnCancelListener(){

                            @Override
                            public void onCancel(DialogInterface dialog) {
                                touho.setPause(false);
                            }
                        });
                        builder.create().show();
                    }else if(msg.getData().get("Op").equals("callReplayDialog")){
                        final File[] files = ReplayLogger.getFiles(MainActivity.this.getFilesDir());
                        final String[] items = new String[files.length];
                        for(int i = 0; i < files.length; i++){
                            items[i] = files[i].getName();
                        }

                        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Replay");
                        builder.setItems(items, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int index) {
                                touho.replay(ReplayLogger.loadFromFile(files[index]));
                            }
                        });
                        builder.create().show();
                    }else if(msg.getData().get("Op").equals("callSaveReplayDialog")){
                        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Quit");
                        builder.setMessage("要保存Replay吗?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                File f = MainActivity.this.getBaseContext().getFilesDir();
                                replayLogger.save(f);
                                touho.quit();
                                touho.setPause(false);
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                touho.quit();
                                touho.setPause(false);
                            }
                        });

                        builder.setCancelable(false);
                        builder.create().show();
                    }else if(msg.getData().get("Op").equals("callReplayOverDialog")){
                        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Replay");
                        builder.setMessage("播放完了");
                        builder.setPositiveButton("知道了", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                touho.quit();
                                touho.setPause(false);
                            }
                        });
                        Dialog dialog = builder.create();
                        dialog.setCancelable(false);
                        dialog.show();
                    }if(msg.getData().get("Op").equals("callRestartDialog")){
                        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Game");
                        builder.setMessage("重新开始？");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                touho.restart();
                                touho.setPause(false);
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                touho.setPause(false);
                            }
                        });
                        Dialog dialog = builder.create();
                        dialog.setCancelable(false);
                        dialog.show();
                    }
                }
            };

            @Override
            public void callOptionDialog() {
                send("callOptionDialog");
            }

            @Override
            public void callReplayDialog() {
                send("callReplayDialog");
            }

            @Override
            public void callSaveReplayDialog(ReplayLogger replayLogger) {
                this.replayLogger = replayLogger;
                touho.setPause(true);
                send("callSaveReplayDialog");

            }

            @Override
            public void callQuitDialog() {
                touho.setPause(true);
                send("callQuitDialog");
            }

            @Override
            public void callReplayOverDialog() {
                touho.setPause(true);
                send("callReplayOverDialog");
            }

            @Override
            public void callRestartDialog() {
                touho.setPause(true);
                send("callRestartDialog");
            }

            private void send(String str){
                Message msg = new Message();
                Bundle bd = new Bundle();
                bd.putString("Op", str);
                msg.setData(bd);
                hd.sendMessage(msg);
            }
        });
        glv.setRenderer(touho.getRender());
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                long t = Calendar.getInstance().getTimeInMillis();
                while(true) {
                    long nt = Calendar.getInstance().getTimeInMillis();
                    if(nt - t >= 16 && !touho.isPause()) {
                        touho.tick();
                        if(nt - t > 16*2) {
                            System.out.println("tick:" + (nt - t));
                        }
                        t = nt;
                    }
                }
            }
        });
        t.start();
        glv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touho.touch(event);
                return true;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        glv.onPause();
    }

    @Override
        protected void onResume() {
        super.onResume();
        glv.onResume();
    }

}
