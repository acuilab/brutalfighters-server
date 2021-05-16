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
	 * 增加一个瓦片（解析地图时调用）
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
		 * @param id		id
		 * @param y			像素纵坐标(左下角为原点)
		 * @param x			像素横坐标(左下角为原点)
		 * @param height	像素高度（左下角为原点）
		 * @param width		像素宽度（左下角为原点）
		 * @param ratio		三属性之ratio
		 * @param blocked	三属性之blocked
		 * @param step		三属性之step
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
	 * @param x	逻辑单位
	 * @param y	逻辑单位
	 * @return
	 */
	public Tile getTile(int x, int y) {
		int i = (getHeight()-y) * width + x;
		return i < tiles.size() ? tiles.get(i) : getTile(0,0);
	}
	/**
	 * 宽度(逻辑单位，瓦片个数)
	 * @return
	 */
	public int getWidth() {
		return width;
	}
	/**
	 * 高度(逻辑单位，瓦片个数)
	 * @return
	 */
	public int getHeight() {
		return height;
	}
}
