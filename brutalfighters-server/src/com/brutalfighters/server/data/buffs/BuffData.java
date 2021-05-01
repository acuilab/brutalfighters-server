package com.brutalfighters.server.data.buffs;

/**
 * 增益数据（名称和时间属性）
 *
 */
public class BuffData {
	private String name;	// 名称
	private int time;		// 时间
	
	public BuffData(String name, int time) {
		setName(name);
		setTime(time);
	}
	private BuffData() {
		this("dummy", 0); //$NON-NLS-1$
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
}
