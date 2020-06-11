package com.example.app2.touho.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Deque;
import java.util.HashSet;
import java.util.Stack;

public class ReplayLogger {
    private long seed;
    private ArrayDeque<Byte> ops = new ArrayDeque<>();
    private byte b;
    private boolean replayMode;

    public ReplayLogger(boolean replayMode){
        this.replayMode = replayMode;
    }

    //mode ulti   drct
    public void push(int mode, int ulti, int drct){
        b = (byte) (drct | (mode << 6) | (ulti << 5));
        ops.addLast(b);
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public long getSeed() {
        return seed;
    }

    public boolean next(){
        if(ops.isEmpty()){
            return false;
        }else {
            b = ops.removeFirst();
            return true;
        }
    }

    public int getMode(){
        return (b & 0x40) >> 6;
    }

    public int getUlti(){
        return (b & 0x20) >> 5;
    }

    public int getDirection(){
        return b & 0x0F;
    }

    public void write(OutputStream os) throws IOException {
        os.write(longToBytes(seed));
        int length = ops.size();
        byte[] buf = new byte[1];
        while(!ops.isEmpty()){
            buf[0] = ops.removeFirst();
            os.write(buf);
        }
        os.flush();
    }

    public void load(InputStream is) throws IOException {
        byte[] buff = new byte[8];
        is.read(buff);
        seed = bytesToLong(buff);
        buff = new byte[1];
        while(is.read(buff) != -1) ops.addLast(buff[0]);
    }

    public void save(File baseFile){
        File fd = new File(baseFile, "replay");
        if(!fd.exists()){
            fd.mkdir();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd-hh:mm:ss");
        File f = new File(fd, sdf.format(Calendar.getInstance().getTime()));
        try {
            OutputStream os = new FileOutputStream(f);
            write(os);
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ReplayLogger loadFromFile(File f){
        ReplayLogger rep = null;
        try {
            rep = new ReplayLogger(true);
            InputStream ins = new FileInputStream(f);
            rep.load(ins);
            ins.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rep;
    }

    public static File[] getFiles(File baseFile){
        File fd = new File(baseFile, "replay");
        if(fd.exists()){
            return fd.listFiles();
        }else {
            return new File[0];
        }
    }

    public boolean isReplayMode() {
        return replayMode;
    }

    public static byte[] longToBytes(long num){
        ByteBuffer bf = ByteBuffer.allocate(Long.BYTES);
        bf.putLong(num);
        return bf.array();
    }

    public static long bytesToLong(byte[] bts) {
        ByteBuffer bf = ByteBuffer.allocate(Long.BYTES);
        bf.put(bts);
        bf.flip();
        return bf.getLong();
    }
}
