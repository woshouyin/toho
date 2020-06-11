package com.example.app2.touho.controler;

import com.example.app2.touho.elements.AnmElement;
import com.example.app2.touho.elements.Element;
import com.example.app2.touho.elements.StageElement;
import com.example.app2.touho.stage.Stage;

import java.util.HashSet;

public class Controler {
    protected Stage stg = null;
    protected StageElement elm;
    protected long tickTime = 0;
    protected long playTick = -1;
    protected HashSet<Controler> next = new HashSet<>();

    public void tick(){
        tickTime++;
        if(playTick != -1 && tickTime == playTick){
            end();
        }
    }

    public void start(){

    };

    public void addNext(Controler controler){
        next.add(controler);
    }

    public void end(){
        elm.rmControler(this);
        for(Controler c : next){
            elm.addControler(c);
        }
    }

    /**由元素调用*/
    public void bind(StageElement elm){
        this.elm = elm;
        this.stg = elm.getStg();
        start();
    }

    public void setPlayTick(long playTick) {
        this.playTick = playTick;
    }
}
