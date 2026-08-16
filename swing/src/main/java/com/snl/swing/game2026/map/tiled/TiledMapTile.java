package com.snl.swing.game2026.map.tiled;

import com.snl.swing.game2026.map.MapObjects;
import com.snl.swing.game2026.map.MapProperties;

//以libgdx的文档来看，这是个瓦片地图类
public interface TiledMapTile {
    //源代码中包含以下字段：
    //id ?? 为什么需要id？

    public enum BlendMode {
        NONE,ALPHA
    }

    int getId();
    void setId(int id);

    //混合模式
    //以libgdx的源代码的形式来看，这个字段包含两个值：none和alpha；（2026年6月25日23:31:08 我以后回来看看的）
    BlendMode getBlendMode();
    void setBlendMode(BlendMode blendMode);

    /**
     * //todo
     * 还有一个字段，{@code TextureRegion} 我不明白什么意思？？
     * 但是我在《java 2d grafic》一书中学习到了一个纹理技术，嗯？？有关系吗？？
     */

    //以下是渲染时操作
     float getOffsetX();
     void setOffsetX(float offsetX);
     float getOffsetY();
     void setOffsetY(float offsetY);

     //以下是属性字段
    MapProperties getProperties();

    //该瓦片对象集合
    MapObjects getObjects();
}
