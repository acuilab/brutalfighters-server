package com.brutalfighters.server.util;

import java.awt.Rectangle;

/**
 * 工具类，用于碰撞检测
 *
 */
public class CollisionDetection {
	
	/**
	 * 
	 * @param flip
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 */
	public static Rectangle getBounds(String flip, int x, int y, int width, int height) {
		if(flip.equals("left")) {
			// 方向左
			return new Rectangle(x-width,y-height/2,width,height);
		} else if(flip.equals("right")) {
			return new Rectangle(x,y-height/2,width,height);
		} else if(flip.equals("both")) {
			return new Rectangle(x-width/2,y-height/2,width,height);
		} else {
			return null;
		}
	}
	
	public static Rectangle getBounds(String flip, float x, float y, float width, float height) {
		return getBounds(flip, (int)x, (int)y, (int)width, (int)height);
	}
	
	public static void setBounds(Rectangle rectangle, 
			String flip, 
			int x, 
			int y, 
			int width, 
			int height) {
		if(flip.equals("left")) {
			// 朝左边抛射
			rectangle.setBounds(x-width,y-height/2,width,height);
		} else if(flip.equals("right")) {
			// 朝右边抛射
			rectangle.setBounds(x,y-height/2,width,height);
		} else if(flip.equals("both")) {
			// 朝左右两边抛射
			rectangle.setBounds(x-width/2,y-height/2,width,height);
		}
	}
	public static void setBounds(Rectangle rectangle, String flip, float x, float y, float width, float height) {
		setBounds(rectangle, 
				flip, 
				(int)x, 
				(int)y, 
				(int)width, 
				(int)height);
	}
}
