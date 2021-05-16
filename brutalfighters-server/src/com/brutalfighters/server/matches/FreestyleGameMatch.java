package com.brutalfighters.server.matches;

import java.util.Iterator;
import java.util.Map;

import com.brutalfighters.server.data.players.PlayerMap;
import com.brutalfighters.server.util.Counter;
import com.esotericsoftware.kryonet.Connection;

public class FreestyleGameMatch extends GameMatch {
	
	/*
	 * 
	 * Warmup: 20sec
	 * Players: 6
	 * Win State: 3 flags
	 * Respawn: 6sec
	 * Match Finish: 6sec
	 * 
	 */
	
	private static final int DEFAULT_PLAYER_LIMIT = 50;
	private static final int DEFAULT_WARMUP = 0;
	
	/* ##### Constructors ##### */
	/**
	 * 
	 * @param mapName	地图名称
	 * @param ID		比赛id
	 * @param players	玩家
	 * @param teams		队伍
	 */
	public FreestyleGameMatch(String mapName, String ID, PlayerMap players, PlayerMap[] teams) {
		super(mapName, ID, players, teams);
		setupFreestyle();
	}
	/**
	 * 
	 * @param mapName	地图名称
	 * @param ID		比赛id
	 */
	public FreestyleGameMatch(String mapName, String ID) {
		super(mapName, ID);
		setupFreestyle();
	}
	public void setupFreestyle() {
		this.warmup = new Counter(getDefaultWarmup());
		this.playerLimit = getDefaultPlayerLimit();
	}
	
	
	public static int getDefaultPlayerLimit() {
		return DEFAULT_PLAYER_LIMIT;
	}
	public static int getDefaultWarmup() {
		return DEFAULT_WARMUP;
	}
	
	/* Updates */
	@Override
	public void updateMatch(Iterator<Map.Entry<String,GameMatch>> iter) {
		// 只要游戏没有结束，就更新游戏和客户端
		if(!gameFinished(iter)) {
			// 更新游戏
			updateGame();
			// 更新客户端
			updateClients();
		}
	}
	
	/**
	 * 游戏是否已完成
	 */
	@Override
	protected boolean gameFinished(Iterator<Map.Entry<String,GameMatch>> iter) {
		return checkEmpty(iter);
	}
	
	// Players / players Control
	@Override
	public void addPlayer(Connection connection, String m_id, String fighter) {

		super.addPlayer(connection, m_id, fighter);
		
		// In freestyle we send resources immediately
		approveResources(connection);
	}
}