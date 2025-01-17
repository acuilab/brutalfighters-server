package com.brutalfighters.server.data.projectiles;

import com.brutalfighters.server.util.Vec2;

/**
 * 抛射物数据
 * 	位置、速度、大小、名称、方向、模式、时间
 * 
 *
 */
public class ProjectileData {
	
	private static final String INIT = "init";			// 模式：初始
	private static final String EXPLODE = "explode";	// 模式：爆炸
	
	private Vec2 pos, vel;	// 位置和速度
	private Vec2 size;		// 大小
	private String name;	// 名称
	private String flip;	// 朝向
	private String mode;	// 模式
	private int time;		// 生存时间
	
	public ProjectileData(String name, String flip, Vec2 pos, Vec2 size) {
		setPos(new Vec2(pos));
		setSize(new Vec2(size));
		setVel(new Vec2());
		setName(name);
		setFlip(flip);
		setInit();
	}
	private ProjectileData() {
		this("dummy", "right", new Vec2(), new Vec2()); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public static String getInit() {
		return INIT;
	}
	public static String getExplode() {
		return EXPLODE;
	}
	public void setInit() {
		setMode(getInit());
	}
	public void setExplode() {
		setMode(getExplode());
	}

	public Vec2 getPos() {
		return pos;
	}
	public void setPos(Vec2 pos) {
		this.pos = new Vec2(pos);
	}
	
	public Vec2 getVel() {
		return vel;
	}
	public void setVel(Vec2 vel) {
		this.vel = new Vec2(vel);
	}
	
	public Vec2 getSize() {
		return size;
	}
	public void setSize(Vec2 size) {
		this.size = new Vec2(size);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getFlip() {
		return flip;
	}
	public boolean isRight() {
		return flip.equals("right");
	}
	public boolean isLeft() {
		return flip.equals("left");
	}
	public void setFlip(String flip) {
		this.flip = flip;
	}
	public void setRight() {
		setFlip("right");
	}
	public void setLeft() {
		setFlip("left");
	}
	
	public String getMode() {
		return mode;
	}
	public boolean isExplode() {
		return getMode().equals(getExplode());
	}
	public boolean isInit() {
		return getMode().equals(getInit());
	}
	private void setMode(String mode) {
		this.mode = mode;
	}
	
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public void addTime(int time) {
		setTime(getTime() + time);
	}
}
