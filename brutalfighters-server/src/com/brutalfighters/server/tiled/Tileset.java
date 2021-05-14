package com.brutalfighters.server.tiled;

import java.util.HashMap;
import java.util.Map;

/**
 * 瓦片属性集(瓦片属性的集合)
 *
 */
public class Tileset {
	
	// 下面这几个字符串常量都是属性的键值
	private static final String BLOCKED = "blocked";	// 障碍物
	private static final String BLOCKED_TOP = "top";
	private static final String STEP = "step";
	private static final String TELEPORT = "teleport";
	private static final String RATIO = "ratio";
	
	private Map<String, Object> properties;
	
	public Tileset() {
		this.properties = new HashMap<String, Object>();
	}
	
	public static String BLOCKED() {
		return BLOCKED;
	}
	public static String BLOCKED_TOP() {
		return BLOCKED_TOP;
	}
	public static String STEP() {
		return STEP;
	}
	public static String TELEPORT() {
		return TELEPORT;
	}
	public static String RATIO() {
		return RATIO;
	}

	public void setProperty(String key, Object value) {
		properties.put(key, value);
	}
	public Object getProperty(String key) {
		return hasProperty(key) ? properties.get(key) : "none"; //$NON-NLS-1$
	}
	public boolean hasProperty(String key) {
		return properties.containsKey(key);
	}
	
	public boolean isBlocked() {
		return BLOCKED().equalsIgnoreCase(getBlocked());
	}
	public String getBlocked() {
		return getProperty(BLOCKED()).toString();
	}
	public String getStep() {
		return getProperty(STEP()).toString();
	}
	
	/**
	 * 获得ratio属性
	 * @return
	 */
	public float getRatio() {
		String ratio = getProperty(RATIO()).toString();
		return !ratio.equals("none") ? Float.parseFloat(ratio) : 1;
	}
}
