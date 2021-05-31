package com.brutalfighters.server.tiled;

import com.brutalfighters.server.data.objects.Collidable;

/**
 * 一个可碰撞的瓦片（包含了一个矩形的包围框）
 *	id、blocked、step、ratio四种属性
 */
public class Tile extends Collidable {
	private int id;
	private String blocked;	// 是否是障碍物：blocked和空
	private String step;	// ice(冰)、rock(岩石)、dirt(泥土)
	private float ratio;	// 1、0.3

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
	public Tile(int id, int y, int x, int height, int width, float ratio, String blocked, String step) {
		this.id = id;
		this.blocked = blocked;
		this.step = step;
		this.ratio = ratio;
		Rectangle(x,y,width,height);
	}
	
	/**
	 * 是否是障碍物
	 * @param value
	 * @return
	 */
	public boolean isBlocked(String value) {
		return blocked.equals(value);
	}
	public String getStep() {
		return step;
	}
	public int getID() {
		return id;
	}
	public float getRatio() {
		return ratio;
	}
}
