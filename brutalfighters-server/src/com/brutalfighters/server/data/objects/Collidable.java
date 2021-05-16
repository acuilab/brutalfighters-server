package com.brutalfighters.server.data.objects;

import java.awt.Rectangle;

/**
 * 可碰撞的（包含了一个矩形的包围框）
 *
 */
public class Collidable {
	protected Rectangle bounds;
	
	/**
	 * 
	 * @param x			像素横坐标(左下角为原点)
	 * @param y			像素纵坐标(左下角为原点)
	 * @param width		像素宽度(左下角为原点)
	 * @param height	像素高度(左下角为原点)
	 */
	public void Rectangle(int x, int y, int width, int height) {
		Rectangle(new Rectangle(x,y,width,height));
	}
	public void Rectangle(Rectangle rectangle) {
		bounds = new Rectangle(rectangle);
	}
	
	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds.setBounds(bounds);
	}
	/**
	 * 
	 * @param x			像素横坐标(左下角为原点)
	 * @param y			像素纵坐标(左下角为原点)
	 * @param width		像素宽度(左下角为原点)
	 * @param height	像素高度(左下角为原点)
	 */
	public void setBounds(int x, int y, int width, int height) {
		setBounds(new Rectangle(x, y, width, height));
	}
	
	/**
	 * 检查是否与某个矩形相交
	 * @param bounds
	 * @return
	 */
	public boolean intersects(Rectangle bounds) {
		return getBounds().intersects(bounds);
	}
}
