package com.brutalfighters.server.matches;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import com.brutalfighters.server.data.maps.MapManager;
import com.brutalfighters.server.data.players.PlayerMap;
import com.brutalfighters.server.data.players.fighters.Fighter;
import com.esotericsoftware.kryonet.Connection;

/**
 * 保存了某类游戏比赛和该类比赛中的玩家
 * 目前有三类比赛：closedMatches、openMatches、freestyleMatches
 *
 * @param <T>
 */
public class GameMatches<T extends GameMatch> {
	private Class<T> gameMatch;						// 这是一个class对象，通过反射调用其构造函数实例化一个比赛
	private HashMap <String, T> matches;
	private HashMap <Connection, String> players;	// 保存所有玩家（连接，ID)
	
	public GameMatches(Class<T> gameMatch) {
		this.gameMatch = gameMatch;
		matches = new HashMap<String, T>();
		players = new HashMap<Connection, String>();
	}
	
	/* Getters and Setters */
	public HashMap<String, T> getMatches() {
		return matches;
	}
	public HashMap<Connection, String> getPlayers() {
		return players;
	}
	
	/**
	 * 获得比赛
	 * @param id
	 * @return
	 */
	public T getMatch(String id) {
		if(matches.containsKey(id)) {
			return matches.get(id);
		}
		return null;
	}
	/**
	 * 获得比赛
	 * @param cnct
	 * @return
	 */
	public T getMatch(Connection cnct) {
		return getMatch(players.get(cnct));
	}
	
	/**
	 * 增加比赛（它将玩家和比赛添加到HashMaps中。）
	 * @param ID
	 * @param match
	 */
	public void addMatch(String ID, T match) { // It adds the players and the match into the HashMaps.
		for(Entry<Connection, Fighter> entry : match.getPlayers().entrySet()) {
		    players.put(entry.getKey(), ID);
		}

		matches.put(ID, match);
	}
	
	/**
	 * 使用一个SKID(Secure Key ID)设置比赛
	 * @param mapName	地图名称
	 * @param players	玩家
	 * @param teams		队伍
	 * @return
	 */
	public String setupMatch(String mapName, PlayerMap players, PlayerMap[] teams) { // It setups a match, with a SKID and stuff.
		try {
			// 生成skid
			String ID = GameMatchManager.uniqueSecureKeyID();
			// 调用构造函数实例化一个比赛
			T match = gameMatch.getConstructor(String.class, String.class, PlayerMap.class, PlayerMap[].class).newInstance(mapName, ID, players, teams);
			// 切换id
			match.changeID(ID);
			// 增加比赛（它将玩家和比赛添加到HashMaps中。）
			addMatch(ID, match);
			
			// 返回skid
			return ID;
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	public String setupMatch(String mapName) { // It setups a match, with a SKID and stuff.
		try {
			String ID = GameMatchManager.uniqueSecureKeyID();
			T match = gameMatch.getConstructor(String.class, String.class).newInstance(mapName, ID);
			match.changeID(ID);
			addMatch(ID, match);
			
			return ID;
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	/**
	 * 使用默认地图设置一个新的比赛
	 * @return
	 */
	public String setupMatch() {
		return setupMatch(MapManager.getDefaultMap());
	}
	
	/**
	 * 移除比赛，与addMatch相对应（它将玩家和比赛从HashMaps中移除。）
	 * @param id
	 */
	public void removeMatch(String id) {
		matches.remove(id);
		players.values().removeAll(Collections.singleton(id));
	}
	
	public void connectPlayer(Connection cnct, String id, String fighter) {
		System.out.println("Connecting a new player to the SKID: " + id); //$NON-NLS-1$
		
		getMatch(id).addPlayer(cnct, id, fighter);
		players.put(cnct, id);
	}
	public void connectPlayer(Connection cnct, String fighter) {
		connectPlayer(cnct, getAvailableMatch(), fighter);
	}
	/**
	 * 移除玩家
	 * @param cnct
	 */
	public void removePlayer(Connection cnct) {
		System.out.println("Player Removed!"); //$NON-NLS-1$
		if(getMatch(cnct) != null) {
			getMatch(cnct).removePlayer(cnct);
			players.remove(cnct);
		}
	}
	
	/**
	 * 获得地图名称
	 * @param id
	 * @return
	 */
	public String getMapName(String id) {
		return getMatch(id).getMapName();
	}
	
	public Fighter getPlayer(Connection cnct) {
		if(getMatch(cnct) != null) {
			return getMatch(cnct).getPlayer(cnct);
		}
		return null;
	}
	
	/**
	 * 获得比赛数量
	 * @return
	 */
	public int getMatchesLength() {
		return matches.size();
	}
	/**
	 * 获得比赛中的玩家
	 * @return
	 */
	public int getPlayersPlaying() {
		return players.size();
	}
	
	/* Search Matches */
	public String getAvailableMatch() {
		/**
		 * 遍历查找可用的比赛
		 */
		System.out.println("Searching for an available match..");
		for (Entry<String, T> entry : getMatches().entrySet()) {
		    if(!entry.getValue().isFull() && entry.getValue().isOpen()) {
		    	// 比赛未满且比赛是开放的
		    	System.out.println("Found a match!");
		    	return entry.getKey();
		    }
		}
		
		// 未找到，设置一个新的比赛
		System.out.println("Didn't find, going to setup one..");
		return setupMatch();
	}

	/* Secure Match ID Key */
	public boolean isKey(String ID) {
		System.out.println("at least we are in isKey?"); //$NON-NLS-1$
		for (Entry<String, T> entry : matches.entrySet()) {
		    if(entry.getKey().equals(ID)) {
		    	return true;
		    }
		}
    	System.out.println("and we are returning false i suppose??");
		return false;
	}
}
