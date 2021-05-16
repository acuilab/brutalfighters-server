package com.brutalfighters.server.tiled;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 瓦片地图
 *
 */
public class TiledMap {
	// 地图宽度(逻辑单位，瓦片个数)，地图高度(逻辑单位，瓦片个数)，瓦片宽度（物理单位，像素），瓦片高度（物理单位，像素），瓦片属性集集合和瓦片层集合
	protected int width, height, tileWidth, tileHeight;
	protected HashMap<Integer,Tileset> tilesets = new HashMap<Integer,Tileset>();
	protected List<TiledLayer> tiledlayers = new ArrayList<TiledLayer>();
	
	public TiledMap(int width, int height, int tileWidth, int tileHeight) {
		this.width = width;
		this.height = height;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
	}
	
	//   ##### TILESETS #####
	
	// Add Tileset
	@SuppressWarnings("boxing")
	public void addTileset(int id) {
		tilesets.put(id, new Tileset());
	}
	
	/**
	 * Edit Tileset
	 * @param id
	 * @param key
	 * @param value
	 */
	@SuppressWarnings("boxing")
	public void editTileset(int id, String key, Object value) {
		tilesets.get(id).setProperty(key, value);
	}
	
	/**
	 * Get Tileset
	 * @return
	 */
	public HashMap<Integer,Tileset> getTilesets() {
		return tilesets;
	}
	@SuppressWarnings("boxing")
	public Tileset getTileset(int id) {
		return tilesets.get(id);
	}
	@SuppressWarnings("boxing")
	public boolean isBlocked(int id) {
		return tilesets.get(id).isBlocked();
	}
	@SuppressWarnings("boxing")
	public String getStep(int id) {
		return tilesets.get(id).getStep();
	}
	
	// More Tileset
	public int getTilesetsLength() {
		return tilesets.size();
	}
	
	
	// ##### TILED LAYERS #####
	
	// Add Tiledlayer
	public void addTiledLayer() {
		tiledlayers.add(new TiledLayer());	
	}
	public void addTiledLayer(int width, int height) {
		tiledlayers.add(new TiledLayer(width, height));	
	}
	
	// Get Tiledlayer
	public List<TiledLayer> getTiledLayers() {
		return tiledlayers;
	}
	public TiledLayer getTiledLayer(int i) {
		return tiledlayers.get(i);
	}
	
	// More TiledLayers
	public int getTiledLayersLength() {
		return tiledlayers.size();
	}
	
	
	
	// ##### Tiles #####
	
	// Add Tiles
	/**
	 * 
	 * @param i		瓦片层号
	 * @param id	瓦片id
	 */
	public void addTile(int i, int id) {
		tiledlayers.get(i).addTile(id, 
				getTileWidth(), 
				getTileHeight(), 
				getTileset(id).getRatio(), 
				getTileset(id).getBlocked(), 
				getTileset(id).getStep());
	}
	
	// Get Tiles
	/**
	 * 获得某层指定坐标处的瓦片
	 * @param i	第几层瓦片
	 * @param x	横坐标（逻辑单位，原点位于左上角）
	 * @param y	纵坐标（逻辑单位，原点位于左上角）
	 * @return
	 */
	public Tile getTile(int i, int x, int y) {
		return tiledlayers.get(i).getTile(x,y);
	}
	/**
	 * 获得某层指定坐标处的瓦片id
	 * @param i	第几层瓦片
	 * @param x	横坐标（逻辑单位，原点位于左上角）
	 * @param y	纵坐标（逻辑单位，原点位于左上角）
	 * @return
	 */
	public int getID(int i, int x, int y) {
		return getTile(i,x,y).getID();
	}
	
	// ##### Other map #####
	/**
	 * 地图宽度(逻辑单位，瓦片个数)
	 * @return
	 */
	public int getWidth() {
		return width;
	}
	/**
	 * 地图高度(逻辑单位，瓦片个数)
	 * @return
	 */
	public int getHeight() {
		return height;
	}
	/**
	 * 地图的像素宽度
	 * @return
	 */
	public int getWidthPixels() {
		return width * getTileWidth();
	}
	/**
	 * 地图的像素高度
	 * @return
	 */
	public int getHeightPixels() {
		return height * getTileHeight();
	}
	/**
	 * 获得瓦片的像素宽度
	 * @return
	 */
	public int getTileWidth() {
		return tileWidth;
	}
	/**
	 * 获得瓦片的像素高度
	 * @return
	 */
	public int getTileHeight() {
		return tileHeight;
	}
	
	// ##### Collision Detection #####
	/**
	 * 获得某层指定坐标处的瓦片边框
	 * @param layer	第几层瓦片
	 * @param x	横坐标（逻辑单位，原点位于左上角）
	 * @param y	纵坐标（逻辑单位，原点位于左上角）
	 * @return
	 */
	public Rectangle getBounds(int layer, int x, int y) {
		return getTile(layer,x,y).getBounds();
	}
	/**
	 * 某层指定坐标处的瓦片是否与某个矩形框相交
	 * @param layer		第几层瓦片
	 * @param x			横坐标（逻辑单位，原点位于左上角）
	 * @param y			纵坐标（逻辑单位，原点位于左上角）
	 * @param bounds	某个矩形框
	 * @return
	 */
	public boolean intersect(int layer, int x, int y, Rectangle bounds) {
		return getBounds(layer,x,y).intersects(bounds);
	}
}
