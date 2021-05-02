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
		TMXMapLoader.Load();

		CTFMap journeyVale = new CTFMap(TMXMapLoader.readMap("assets/maps/journey_vale/journey_vale.tmx"));
		
		journeyVale.setBase(GameMatch.getTEAM1(), new Vec2(journeyVale.getLeftBoundary() + 100, 600), "right");
		journeyVale.setBase(GameMatch.getTEAM2(), new Vec2(journeyVale.getRightBoundary() - 100, 600), "left");
		
		journeyVale.setFlag(GameMatch.getTEAM1(), new Vec2(journeyVale.getLeftBoundary() + 400, FlagData.getSize().getY() + 291), "right");
		journeyVale.setFlag(GameMatch.getTEAM2(), new Vec2(journeyVale.getRightBoundary() - 400, FlagData.getSize().getY() + 291), "left");
		
		maps.put("journey_vale", journeyVale);
	}
	
	public static CTFMap getMap(String map) {
		return maps.get(map);
	}
	
	public static int getMapsLength() {
		return maps.size();
	}
	
	public static String getDefaultMap() {
		return DEFAULT_MAP;
	}
}
