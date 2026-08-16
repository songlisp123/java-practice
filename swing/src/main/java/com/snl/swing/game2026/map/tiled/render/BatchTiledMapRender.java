package com.snl.swing.game2026.map.tiled.render;

import com.snl.swing.game2026.map.MapLayer;
import com.snl.swing.game2026.map.MapLayers;
import com.snl.swing.game2026.map.MapObject;
import com.snl.swing.game2026.map.MapObjects;
import com.snl.swing.game2026.map.tiled.TiledMap;
import com.snl.swing.game2026.map.tiled.TiledMapImageLayer;
import com.snl.swing.game2026.map.tiled.TiledMapTileLayer;
import com.snl.swing.game2026.map.tiled.TiledMapRenderer;
import com.snl.swing.game2026.util.Dispose;

//以lbgdx为基准
public abstract class BatchTiledMapRender implements TiledMapRenderer, Dispose {
    //渲染器？？？要渲染--地图。
    private TiledMap map;
    //在官方文档中，还实现了一个奇怪地接口，Batch接口，这是个什么东西？？？

    //单位缩放
    private float unitScale;

    public BatchTiledMapRender(TiledMap map) {
        this(map,1.0f);
    }

    public BatchTiledMapRender(TiledMap map, float unitScale) {
        this.map = map;
        this.unitScale = unitScale;
    }

    //渲染操作：在libgdx的文档中，一个明显的渲染周期是：渲染前调用begin，渲染后调用end

    @Override
    public void render() {
        //开始渲染
        beginRender();
        //TODO
        //实际渲染操作
        //获取地图的所有图层
        MapLayers layers = map.getLayers();
        for (MapLayer l : layers)
            renderMapLayer(l);
        //结束渲染
        endRender();
    }


    /**
     * 妈的，这个函数定义在<b>{@link TiledMapRenderer}</b>接口中，你只要实现这个方法就行
     */
    private void renderMapLayer(MapLayer l) {
        //如果图层不可见,误操作
        if (!l.isVisible()) return;
        //渲染图层的所有东西
        if (l instanceof TiledMapTileLayer layer)
            this.renderTileLayer(layer);
        else if (l instanceof TiledMapImageLayer imageLayer) {
            this.renderImageLayer(imageLayer);
        }
        else {
            this.renderObjects(l);
        }
    }

    @Override
    public void render(int[] layers) {
        beginRender();
        for (int layIndex : layers) {
            MapLayer mapLayer = map.getLayers().getLayers().get(layIndex);
            this.renderMapLayer(mapLayer);
        }
        endRender();
    }

    private void beginRender() {
        //todo
    }

    private void endRender() {
        //todo
    }

    @Override
    public void dispose() {
        //todo
    }

    @Override
    public void renderObject(MapObject object) {

    }

    /**
     * 这个方法会自动调用{@code renderObject}方法
     * @param layer 要渲染的图层对象
     */
    @Override
    public void renderObjects(MapLayer layer) {
        //获取物体
        MapObjects objects = layer.getObjects();
        for (MapObject object : objects)
            this.renderObject(object);
    }

    @Override
    public void renderImageLayer(TiledMapImageLayer layer) {
        //TODO 渲染图像层
    }
}
