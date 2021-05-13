package com.brutalfighters.server.data.maps;

import java.util.HashMap;

import com.brutalfighters.server.data.flags.FlagData;
import com.brutalfighters.server.matches.GameMatch;
import com.brutalfighters.server.tiled.TMXMapLoader;
import com.brutalfighters.server.util.Vec2;

/**
 * 地图管理器
 *
 */
public class MapManager {
	// 地图集合和默认地图
	private static HashMap<String, CTFMap> maps = new HashMap<String, CTFMap>();
	private static final String DEFAULT_MAP = "journey_vale";
	
	/**
	 * 注册所有地图
	 */
	public static void registerMaps() {
		// 加载地图
		TMXMapLoader.Load();

		// 实例化默认地图
		CTFMap journeyVale = new CTFMap(TMXMapLoader.readMap("assets/maps/journey_vale/journey_vale.tmx"));
		
		// 设置队伍重生点
		journeyVale.setBase(GameMatch.getTEAM1(), new Vec2(journeyVale.getLeftBoundary() + 100, 600), "right");
		journeyVale.setBase(GameMatch.getTEAM2(), new Vec2(journeyVale.getRightBoundary() - 100, 600), "left");
		
		// 设置队伍旗帜
		journeyVale.setFlag(GameMatch.getTEAM1(), new Vec2(journeyVale.getLeftBoundary() + 400, FlagData.getSize().getY() + 291), "right");
		journeyVale.setFlag(GameMatch.getTEAM2(), new Vec2(journeyVale.getRightBoundary() - 400, FlagData.getSize().getY() + 291), "left");
		
		maps.put("journey_vale", journeyVale);
	}
	
	/**
	 * 根据地图名称获得地图实例
	 * @param map
	 * @return
	 */
	public static CTFMap getMap(String map) {
		return maps.get(map);
	}
	
	/**
	 * 获得地图数量
	 * @return
	 */
	public static int getMapsLength() {
		return maps.size();
	}
	
	/**
	 * 获得默认地图
	 * @return
	 */
	public static String getDefaultMap() {
		return DEFAULT_MAP;
	}
}
