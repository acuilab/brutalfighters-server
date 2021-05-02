package com.brutalfighters.server.data.maps;

import com.brutalfighters.server.util.Vec2;

/**
 * 包含位置和朝向的一个基类
 *
 */
public class Base {
	private Vec2 pos;		// 位置
	private String flip;	// 朝向

	/**
	 * 构造函数
	 * @param pos
	 * @param flip
	 */
	public Base(Vec2 pos, String flip) {
		setPos(pos);
		setFlip(flip);
	}
	
	/**
	 * 构造函数
	 * @param posx
	 * @param posy
	 * @param flip
	 */
	public Base(int posx, int posy, String flip) {
		this(new Vec2(posx, posy), flip);
	}
	
	/**
	 * 获得横坐标
	 * @return
	 */
	public float getX() {
		return getPos().getX();
	}
	/**
	 * 获得纵坐标
	 * @return
	 */
	public float getY() {
		return getPos().getY();
	}
	/**
	 * 获得位置
	 * @return
	 */
	public Vec2 getPos() {
		return pos;
	}
	/**
	 * 设置位置
	 * @param pos
	 */
	private void setPos(Vec2 pos) {
		this.pos = new Vec2(pos);
	}

	/**
	 * 获得朝向
	 * @return
	 */
	public String getFlip() {
		return flip;
	}
	/**
	 * 设置朝向
	 * @param flip
	 */
	private void setFlip(String flip) {
		this.flip = flip;
	}
}
