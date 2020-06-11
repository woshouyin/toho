package com.example.app2.touho.controler;

import com.example.app2.touho.elements.Element;
import com.example.app2.touho.elements.StageElement;
import com.example.app2.touho.stage.Stage;

public class BindControler extends Controler{
    private StageElement felm;

    public BindControler(StageElement felm) {
        super();
        this.felm = felm;
    }

    @Override
    public void tick() {
        super.tick();
        elm.setPositon(felm.getX(), felm.getY());
    }
}
