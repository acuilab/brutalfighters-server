package com.brutalfighters.server.data.maps;

import java.awt.Rectangle;

import com.brutalfighters.server.tiled.Tile;
import com.brutalfighters.server.tiled.TiledMap;
import com.brutalfighters.server.tiled.Tileset;
import com.brutalfighters.server.util.Vec2;

/**
 * 游戏地图
 * 一个带有上校左右边框的TiledMap
 *
 */
public class GameMap extends TiledMap {

	// 上下左右边框
	protected int leftBoundary;
	protected int rightBoundary;
	protected int topBoundary;
	protected int botBoundary;
	
	public GameMap(TiledMap map) {
		super(map.getWidth(), map.getHeight(), map.getTileWidth(), map.getTileHeight());
		
		this.tilesets = map.getTilesets();
		this.tiledlayers = map.getTiledLayers();
		
		this.setLeftBoundary(5);
		this.setRightBoundary(getWidthScaledPixels()-5);
		
		this.setBotBoundary(5);
		this.setTopBoundary(getHeightScaledPixels()-5);
	}	
	
	// ##### Boundaries #####
	public int getRightBoundary() {
		return rightBoundary;
	}
	public void setRightBoundary(int rightBoundary) {
		this.rightBoundary = rightBoundary;
	}
	/**
	 * 获得左边界
	 * @return
	 */
	public int getLeftBoundary() {
		return leftBoundary;
	}
	/**
	 * 设置左边界
	 * @param leftBoundary
	 */
	public void setLeftBoundary(int leftBoundary) {
		this.leftBoundary = leftBoundary;
	}
	
	public int getTopBoundary() {
		return topBoundary;
	}
	public void setTopBoundary(int topBoundary) {
		this.topBoundary = topBoundary;
	}
	public int getBotBoundary() {
		return botBoundary;
	}
	public void setBotBoundary(int botBoundary) {
		this.botBoundary = botBoundary;
	}
	
	/**
	 * 检查给定的x值是否在左右边界内
	 * @param x	像素
	 * @return
	 */
	public boolean checkSideBoundaries(float x) {
		return (x > getLeftBoundary() && x < getRightBoundary());
	}
	/**
	 * 检查给定的y值是否在上下边界内
	 * @param y	像素
	 * @return
	 */
	public boolean checkVerticalBoundaries(float y) {
		return (y > getBotBoundary() && y < getTopBoundary());
	}
	/**
	 * 检查给定的点是否在上下左右边界内
	 * @param pos	像素
	 * @return
	 */
	public boolean checkBoundaries(Vec2 pos) {
		return checkSideBoundaries(pos.getX()) && checkVerticalBoundaries(pos.getY());
	}
	
	// ##### Get Tiles #####
	/**
	 * 获得瓦片
	 * @param i
	 * @param x
	 * @param y
	 * @return
	 */
	@Override
	public Tile getTile(int i, int x, int y) {
		System.out.println("i=" + i + ", x=" + x + ", y=" + y);
		return tiledlayers.get(i).getTile(x,y);
	}
	/**
	 * 获得瓦片
	 * @param i
	 * @param x
	 * @param y
	 * @return
	 */
	public Tile getTile(int i, float x, float y) {
		return getTile(i, toCellX(x), toCellY(y));
	}
	
	public Tileset getTileset(int i, float x, float y) {
		return getTileset(getTile(i,x,y).getID());
	}
	
	private boolean isBlocked(int i, float x, float y) {
		return isBlocked(Tileset.BLOCKED(), getTile(i,x,y));
	}
	private boolean isBlocked(String blocked, int i, float x, float y) {
		return isBlocked(blocked, getTile(i,x,y));
	}
	private static boolean isBlocked(String blocked, Tile tile) {
		return tile.isBlocked(blocked);
	}
	
	public String getKind(int i, float x, float y) {
		return getTile(i,x,y).getStep();
	}
	
	public int getID(int i, float x, float y) {
		return getTile(i,x,y).getID();
	}
	
	// Get Info
	public int toCellX(float x) {
		return (int) (x/getTileWidth());
	}
	public int toCellY(float y) {
		return ((int) (y/getTileHeight()));
	}
	public int toPixelX(float x) {
		return (int) ((x) * getTileWidth() + getTileWidth()/2);
	}
	public int toPixelY(float y) {
		return (int) ((y) * getTileHeight() + getTileHeight()/2);
	}
	
	// Other map
	/**
	 * 整个地图的宽度（像素），多加了一个瓦片的宽度
	 * @return
	 */
	private int getWidthScaledPixels() { // width OF THE WHOLE MAP NOT TILES!!!
		return width * getTileWidth()+getTileWidth();
	}
	/**
	 * 整个地图的高度（像素），多加了一个瓦片的高度
	 * @return
	 */
	private int getHeightScaledPixels() { // height OF THE WHOLE MAP NOT TILES!!!
		return height * getTileHeight()+getTileHeight();
	}
	
	
	// ##### Collision Detection #####
	// ##### 碰撞       检测       #####
	
	/**
	 * @param bounds Rectangle is used for the AABB which is currently disabled on tiles.	矩形用于AABB，目前已在图块上禁用。
	 */
	public boolean intersects(String blocked, float x, float y, Rectangle bounds) {
		
		Tile tile = getTile(0,x,y);
				
		if(isBlocked(blocked, 0, x,y) && (((getTileHeight()-y%getTileHeight()) / getTileHeight()) <= tile.getRatio())) {
			//return intersect(0,toCellX(x),toCellY(y),bounds); //AABB Implemented but not needed :`(
			return true;
		}
		return false;
	}
	public boolean intersectsSurroundX(String blocked, float x, float y, Rectangle bounds) {
		return intersects(blocked, x-bounds.width/2+1,y, bounds) || intersects(blocked, x,y, bounds) || intersects(blocked, x+bounds.width/2-1,y, bounds);
	}
	public boolean intersectsSurroundY(String blocked, float x, float y, Rectangle bounds) {
		return intersects(blocked, x,y-bounds.height/2+1, bounds) || intersects(blocked, x,y, bounds) || intersects(blocked, x,y+bounds.height/2-1, bounds);
	}
	
	/**
	 * @param bounds Rectangle is used for the AABB which is currently disabled on tiles.	矩形用于AABB，目前已在图块上禁用。
	 */
	public boolean intersects(float x, float y, Rectangle bounds) {
		return intersects(Tileset.BLOCKED(), x, y, bounds);
	}
	public boolean intersectsSurroundX(float x, float y, Rectangle bounds) {
		return intersectsSurroundX(Tileset.BLOCKED(), x, y, bounds);
	}
	public boolean intersectsSurroundY(float x, float y, Rectangle bounds) {
		return intersectsSurroundY(Tileset.BLOCKED(), x, y, bounds);
	}
	
	public boolean intersectsSurroundXBoth(String blocked, float x, float y, Rectangle bounds) {
		return intersectsSurroundX(Tileset.BLOCKED(), x, y, bounds) || intersectsSurroundX(blocked, x, y, bounds);
	}
	public boolean intersectsSurroundYBoth(String blocked, float x, float y, Rectangle bounds) {
		return intersectsSurroundY(Tileset.BLOCKED(), x, y, bounds) || intersectsSurroundY(blocked, x, y, bounds);
	}
	
}
