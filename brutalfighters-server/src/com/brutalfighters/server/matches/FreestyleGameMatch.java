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
	
	/* Constructors */
	public FreestyleGameMatch(String mapName, String ID, PlayerMap players, PlayerMap[] teams) {
		super(mapName, ID, players, teams);
		setupFreestyle();
	}
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
		if(!gameFinished(iter)) {
			updateGame();
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