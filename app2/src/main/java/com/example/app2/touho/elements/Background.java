package com.example.app2.touho.elements;


import com.example.app2.Texture;
import com.example.app2.touho.Touho;
import com.example.app2.touho.utils.Frame;
import com.example.app2.touho.utils.ShaderUtil;

public class Background extends AnmElement{
    public static final int BG_FULL = 0;
    public static final int BG_WIND = 1;
    private int texture;
    private int type;
    public Background(Touho touho, int group, int textureId, int type) {
        super(touho, 0, group);
        this.texture = touho.getResouseManager().getTexture(textureId);
        this.type = type;
    }

    @Override
    public void load() {
        super.load();
        if(type == BG_WIND) {
            setFrame(Frame.getInstance(texture, 4
                    , ShaderUtil.getRectVtxBuffer(touho.getLeft(), touho.getTop(), touho.getRight(), touho.getBottom())
                    , ShaderUtil.getRectVtxBuffer(0, 0, 1, 1)));
        }else if(type == BG_FULL){
            setFrame(Frame.getInstance(texture, 4
                    , ShaderUtil.getRectVtxBuffer(touho.getvLeft(), touho.getvTop(), touho.getvRight(), touho.getvBottom())
                    , ShaderUtil.getRectVtxBuffer(0, 0, 1, 1)));
        }
    }
}
