package com.brutalfighters.server.tiled;

import java.util.ArrayList;
import java.util.List;

/**
 * 瓦片层
 *
 */
public class TiledLayer {
	// 宽度(逻辑单位，瓦片个数)、高度(逻辑单位，瓦片个数)及包含的瓦片列表
	private int width, height;
	private List<Tile> tiles = new ArrayList<Tile>();
	
	public TiledLayer() {
		this(0,0);
	}
	public TiledLayer(int width, int height) {
		this.width = width;
		this.height = height-1;
	}
	
	/**
	 * 增加一个瓦片
	 * @param id			id
	 * @param tileWidth		瓦片宽度
	 * @param tileHeight	瓦片高度
	 * @param ratio			三属性之ratio
	 * @param blocked		三属性之blocked
	 * @param step			三属性之step
	 */
	public void addTile(int id, 
			int tileWidth, 
			int tileHeight, 
			float ratio, 
			String blocked, 
			String step) {
		/**
		 * 构造函数
		 * @param id
		 * @param y
		 * @param x
		 * @param height
		 * @param width
		 * @param ratio
		 * @param blocked
		 * @param step
		 */
		tiles.add(new Tile(id, 
				(getHeight() - tiles.size() / getWidth()) * tileHeight, // 像素纵坐标(左下角为原点)
				tiles.size() % getWidth() * tileWidth, 					// 像素横坐标(左下角为原点)
				tileHeight, 
				tileWidth, 
				ratio, 
				blocked, 
				step));
		
	}
	/**
	 * 获得本层指定坐标处的瓦片(原点位于左上角)
	 * @param x
	 * @param y
	 * @return
	 */
	public Tile getTile(int x, int y) {
		int i = (getHeight()-y) * width + x;
		return i < tiles.size() ? tiles.get(i) : getTile(0,0);
	}
	
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
}
