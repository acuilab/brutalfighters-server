package com.brutalfighters.server.matches;

import java.util.Iterator;
import java.util.Map;

import com.brutalfighters.server.base.GameServer;
import com.brutalfighters.server.data.flags.Flag;
import com.brutalfighters.server.data.flags.Flags;
import com.brutalfighters.server.data.maps.Base;
import com.brutalfighters.server.data.maps.CTFMap;
import com.brutalfighters.server.data.maps.MapManager;
import com.brutalfighters.server.data.players.PlayerData;
import com.brutalfighters.server.data.players.PlayerMap;
import com.brutalfighters.server.data.players.fighters.Fighter;
import com.brutalfighters.server.data.players.fighters.FighterFactory;
import com.brutalfighters.server.data.projectiles.ProjectileData;
import com.brutalfighters.server.data.projectiles.Projectiles;
import com.brutalfighters.server.packets.Packet;
import com.brutalfighters.server.packets.Packet1Connected;
import com.brutalfighters.server.packets.Packet2MatchFinished;
import com.brutalfighters.server.packets.Packet2MatchOver;
import com.brutalfighters.server.packets.Packet2Players;
import com.brutalfighters.server.util.Counter;
import com.brutalfighters.server.util.MathUtil;
import com.brutalfighters.server.util.Score;
import com.esotericsoftware.kryonet.Connection;

/**
 * 游戏比赛抽象类
 *
 */
abstract public class GameMatch {
	
	/*
	 * 
	 * Warmup: 20sec		热身时间20秒
	 * Players: 6			玩家数量6
	 * Win State: 3 flags	获胜状态3个棋子
	 * Respawn: 6sec		重生6秒
	 * Match Finish: 6sec	比赛结束6秒
	 * 
	 */
	
	/* DON'T TOUCH IT */
	protected static final int TEAM_LENGTH = 2;	// 队伍个数
	protected static final int TEAM1 = 0;		// 第一队
	protected static final int TEAM2 = 1;		// 第二队
	
	/* Configurable */
	protected static final int DEFAULT_PLAYER_LIMIT = 6;	// 默认最大玩家数
	protected static final int WIN_STATE = 3;				// 获胜状态3个棋子

	/* Configurable */
	protected static final int DEFAULT_WARMUP = 20000;		// 默认热身时间20秒
	protected static final int DEFAULT_RESPAWN = 6000;		// 重生时间6秒
	protected static final int DEFAULT_FINISH = 6000;		// 比赛结束时间6秒
			
	protected PlayerMap players;	// 所有玩家
	protected PlayerMap[] teams;	// 所有队伍
	
	protected Flags flags;			// 旗帜
	
	protected Projectiles projectiles;
	
	protected final String mapName;
	
	protected int playerLimit;		// 最大玩家数量
	
	protected boolean isOpen;		// 游戏比赛是否打开
	
	protected String ID;
	
	protected Score score;
	
	protected int respawn;
	
	protected int teamWon;
	
	protected Counter warmup, finish;
	
	/**
	 * 
	 * @param mapName	地图名称
	 * @param ID		比赛id
	 * @param players	玩家
	 * @param teams		队伍
	 */
	public GameMatch(String mapName, String ID, PlayerMap players, PlayerMap[] teams) {
		// 设置地图名称
		this.mapName = mapName;
		
		// 设置比赛id
		changeID(ID);
		
		// 设置分数
		setScore(new Score());
		
		// 设置玩家
		setPlayers(players);
		
		// 设置队伍
		setTeams(teams);
		
		// 设置旗帜(目前只支持两个队伍，每个队伍一个旗帜)
		setFlags(new Flags(new Flag[] { Flag.getFlag(mapName, getTEAM1()), Flag.getFlag(mapName, getTEAM2()) }));
		
		// 设置抛射物
		setProjectiles(new Projectiles());
		
		// 设置比赛最大玩家数
		setPlayerLimit(getDefaultPlayerLimit());
		
		// 初始比赛是打开状态
		open();
		
		// 设置计时器（热身、结束、重生）
		setWarmup(new Counter(getDefaultWarmup()));
		setFinish(new Counter(getDefaultFinish()));
		
		setRespawnTime(getDefaultRespawn());
		
		// 设置获胜队伍
		setTeamWon(-1);
	}
	
	public GameMatch(String mapName, String ID) {
		this(mapName, ID, new PlayerMap(), new PlayerMap[]{new PlayerMap(),new PlayerMap()});
	}
	
	/**
	 * 获得队伍个数
	 * @return
	 */
	public static int getTeamLength() {
		return TEAM_LENGTH;
	}
	/**
	 * 获得比赛默认最大玩家数
	 * @return
	 */
	public static int getDefaultPlayerLimit() {
		return DEFAULT_PLAYER_LIMIT;
	}
	public static int getWinState() {
		return WIN_STATE;
	}
	public static int getDefaultWarmup() {
		return DEFAULT_WARMUP;
	}
	public static int getDefaultFinish() {
		return DEFAULT_FINISH;
	}
	public static int getDefaultRespawn() {
		return DEFAULT_RESPAWN;
	}
	
	public static int getTEAM1() {
		return TEAM1;
	}
	public static int getTEAM2() {
		return TEAM2;
	}
	
	public PlayerMap[] getTeams() {
		return teams;
	}
	public void setTeams(PlayerMap[] teams) {
		this.teams = teams;
	}
	
	public CTFMap getMap() {
		return MapManager.getMap(mapName);
	}
	public String getMapName() {
		return mapName;
	}
	
	// Team Won / Finish Screen
	/**
	 * 获得获胜的队伍
	 * @return
	 */
	public int getTeamWon() {
		return teamWon;
	}
	/**
	 * 设置获胜队伍
	 * @param i
	 */
	public void setTeamWon(int i) {
		this.teamWon = i;
	}
	/**
	 * 重置获胜队伍
	 */
	public void resetTeamWon() {
		this.teamWon = -1;
	}
	/**
	 * 获得旗帜
	 * @return
	 */
	public Flags getFlags() {
		return flags;
	}
	/**
	 * 设置旗帜
	 * @param flags
	 */
	public void setFlags(Flags flags) {
		this.flags = flags;
	}
	
	/**
	 * 获得比赛ID
	 * @return
	 */
	public String getID() {
		return getID();
	}
	/**
	 * 切换比赛ID
	 * @param ID
	 */
	public void changeID(String ID) {
		this.ID = ID;
	}
	
	/**
	 * 比赛是否已经满了（达到玩家限制数）
	 * @return
	 */
	public boolean isFull() {
		return getPlayerLimit() == getPlayers().size();
	}
	/**
	 * 比赛是否打开
	 * @return
	 */
	public boolean isOpen() {
		return isOpen;
	}
	/**
	 * 将比赛置为打开状态
	 */
	public void open() {
		isOpen = true;
	}
	/**
	 * 将比赛打开状态关闭
	 */
	public void close() {
		isOpen = false;
	}
	
	public Score getScore() {
		return score;
	}
	public void setScore(Score score) {
		this.score = score;
	}
	/**
	 * 增加杀人数
	 * @param team
	 */
	public void addKill(int team) {
		getScore().getKills()[team]++;
	}
	/**
	 * 增加旗帜数
	 * @param team
	 */
	public void addFlag(int team) {
		getScore().getFlags()[team]++;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getRespawnTime() {
		return respawn;
	}
	public void setRespawnTime(int RESPAWN) {
		this.respawn = RESPAWN;
	}
	
	public Counter getWarmup() {
		return warmup;
	}
	public void setWarmup(Counter WARMUP) {
		this.warmup = WARMUP;
	}
	public boolean isWarmup() {
		return !getWarmup().isFinished();
	}
	
	public Counter getFinish() {
		return finish;
	}
	public void setFinish(Counter FINISH) {
		this.finish = FINISH;
	}
	
	/**
	 * 获得比赛中的玩家
	 * @return
	 */
	public PlayerMap getPlayers() {
		return players;
	}
	/**
	 * 设置比赛中的玩家
	 * @param players
	 */
	public void setPlayers(PlayerMap players) {
		this.players = players;
	}
	
	/**
	 * 增加玩家
	 * @param connection
	 * @param m_id
	 * @param fighter
	 */
	public void addPlayer(Connection connection, String m_id, String fighter) {
		
		fighter = Character.toUpperCase(fighter.charAt(0)) + fighter.substring(1);
		
		// Checking if the fighter name the client passed does in fact exist
		if(!FighterFactory.contains(fighter)) {
			return;
		}
		
		// Setting up the information needed for getting the fighter
		int team = teams[0].size() < teams[1].size() ? getTEAM1() : getTEAM2();
		Base base = getMap().getBase(team);
		
		// Getting the fighter
		Fighter player = FighterFactory.valueOf(fighter).getNew(connection, team, base, m_id);
		
		// Adding the fighter into the data arrays
		teams[team].put(connection, player);
		getPlayers().put(connection, teams[team].get(connection));
		
	}
	public Fighter getPlayer(Connection connection) {
		return getPlayers().get(connection);
	}
	/**
	 * 移除玩家
	 * @param connection
	 */
	public void removePlayer(Connection connection) {
		getTeam1().remove(connection);
		getTeam2().remove(connection);
		getPlayers().remove(connection);
	}

	public PlayerMap getTeam1() {
		return getTeam(getTEAM1());
	}
	public PlayerMap getTeam2() {
		return getTeam(getTEAM2());
	}
	public PlayerMap getTeam(int team) {
		return teams[team];
	}
	public PlayerMap getTeam(Connection cnct) {
		if(getTeam1().containsKey(cnct)) {
			return getTeam1();
		} else if(getTeam2().containsKey(cnct)) {
			return getTeam2();
		} else {
			return null;
		}
	}
	/**
	 * 获得敌人队伍
	 * @param team
	 * @return
	 */
	public PlayerMap getEnemyTeam(int team) {
		if(team == getTEAM1()) {
			return getTeam2();
		} else if(team == getTEAM2()) {
			return getTeam1();
		} else {
			return null;
		}
	}
	public PlayerMap getEnemyTeam(Connection cnct) {
		if(getTeam1().containsKey(cnct)) {
			return getTeam1();
		} else if(getTeam2().containsKey(cnct)) {
			return getTeam2();
		} else {
			return null;
		}
	}
	public static int getEnemyTeamID(int team) {
		if(team == getTEAM1()) {
			return getTEAM2();
		} else if(team == getTEAM2()) {
			return getTEAM1();
		} else {
			return -1;
		}
	}
	public int getEnemyTeamID(Connection cnct) {
		if(getTeam1().containsKey(cnct)) {
			return getTEAM2();
		} else if(getTeam2().containsKey(cnct)) {
			return getTEAM1();
		} else {
			return -1;
		}
	}
	public int getTeamID(Connection cnct) {
		if(getTeam1().containsKey(cnct)) {
			return getTEAM1();
		} else if(getTeam2().containsKey(cnct)) {
			return getTEAM2();
		} else {
			return -1;
		}
	}
	
	public Projectiles getProjectiles() {
		return projectiles;
	}
	public void setProjectiles(Projectiles projectiles) {
		this.projectiles = projectiles;
	}
	public ProjectileData[] getProjectilesArray() {
		ProjectileData[] array = new ProjectileData[getProjectiles().getAll().size()];
		
		for(int i = 0; i < array.length; i++) {
			array[i] = getProjectiles().get(i).getProjectile();
		}
		
		return array;
	}
	
	public int getPlayerLimit() {
		return playerLimit;
	}
	public void setPlayerLimit(int playerLimit) {
		this.playerLimit = playerLimit;
	}
	
	
	// UPDATES
	public abstract void updateMatch(Iterator<Map.Entry<String,GameMatch>> iter);
	
	/**
	 * 游戏是否结束
	 * @param iter
	 * @return
	 */
	protected boolean gameFinished(Iterator<Map.Entry<String,GameMatch>> iter) {
		// 获胜队伍已经产生
		// 或者比赛已空，玩家已全部离开
		return getTeamWon() != -1 || checkEmpty(iter) || checkGameState();
	}

	/**
	 * 检查游戏状态
	 * @return
	 */
	protected boolean checkGameState() {
		for(int i = 0; i < getScore().getFlags().length; i++) {
			if(getScore().getFlags()[i] >= WIN_STATE) {
				setTeamWon(i);
				resetFinish();
				
				Packet2MatchFinished packet = new Packet2MatchFinished();
				packet.teamWon = getTeamWon();
				updateClients(packet);
				return true;
			}
		}
		return false;
	}
	protected void resetFinish() {
		getFinish().resetCounter();
	}
	
	/**
	 * 检测比赛是否已空(玩家已全部离开)
	 * 	对于已经没有玩家的比赛，会做移除比赛处理
	 * @param iter
	 * @return
	 */
	protected boolean checkEmpty(Iterator<Map.Entry<String,GameMatch>> iter) {
		if(getPlayers().size() <= 0) {
			removeMatch(iter);
			return true;
		}
		return false;
	}
	protected void updateWarmup() {
		getWarmup().subCounter(GameServer.getDelay());
	}
	/**
	 * 更新游戏，需要更新三类对象
	 * 	1 玩家
	 * 	2 抛射物
	 * 	3 旗帜
	 */
	protected void updateGame() {
		// ###1 更新玩家
		updatePlayers();
		// ###2 更新抛射物
		updateProjectiles();
		// ###3 更新旗帜
		updateFlags();
	}
	
	/**
	 * 更新旗帜
	 */
	protected void updateFlags() {
		// 获得旗帜并更新
		getFlags().updateFlags();
	}
	/**
	 * 更新抛射物
	 */
	protected void updateProjectiles() {
		// 获得抛射物并更新
		getProjectiles().update();
	}
	/**
	 * 更新玩家
	 */
	protected void updatePlayers() {
		// 遍历玩家并更新
		for(Map.Entry<Connection, Fighter> entry : getPlayers().entrySet()) {
			entry.getValue().update();
		}
	}
	/**
	 * 更新客户端
	 * 对比赛中的每个玩家，发送Packet2Players数据包
	 */
	protected void updateClients() {
		Packet2Players packet = new Packet2Players();
		
		for(Connection cnct : getPlayers().keySet()) {
			packet = new Packet2Players();
			packet.theClient = getPlayers().get(cnct).getPlayer();
			packet.players = getPlayers().getOtherPlayersData(cnct);
			packet.projectiles = getProjectilesArray();
			packet.flags = getFlags().getData();
			packet.score = getScore();
			cnct.sendUDP(packet);
		}
	}
	
	public void womboCombo(Connection connection) {
		// 遍历比赛中的玩家
		for(Map.Entry<Connection, Fighter> entry : getPlayers().entrySet()) {
			
			Fighter fighter = entry.getValue();
			PlayerData pd = fighter.getPlayer();
			
			switch(pd.getName()) {
				// 火焰
				case "Blaze":
					pd.setLeft(false);
					pd.setRunning( false);
					pd.setAAttack(true);
					pd.enableSkilling();
					pd.setSkill3(true);
					fighter.startSkill3();
				break;
				// 黄昏
				case "Dusk":
					pd.setAAttack(true);
					pd.enableSkilling();
					pd.setSkill1(true);
					fighter.startSkill1();
				break;
				// 芯片
				case "Chip":
					pd.setAAttack(true);
					pd.enableSkilling();
					pd.setSkill2(true);
					fighter.startSkill2();
				break;
				// 波涛
				case "Surge":
					pd.setAAttack(true);
					pd.enableSkilling();
					pd.setSkill4(true);
					fighter.startSkill4();
				break;
				// 欲望
				case "Lust":
					pd.setAAttack(true);
					pd.enableSkilling();
					pd.setSkill1(true);
					fighter.startSkill1();
				break;
			}
		}
	}
	
	public void moveCombo(Connection connection) {
		for(Map.Entry<Connection, Fighter> entry : getPlayers().entrySet()) {

			Fighter fighter = entry.getValue();
			PlayerData pd = fighter.getPlayer();
			
			switch(pd.getName()) {
				case "Blaze": //$NON-NLS-1$
					pd.enableSkilling();
					pd.setSkill1(true);
					fighter.startSkill1();
				break;
				
				case "Dusk": //$NON-NLS-1$
					pd.enableSkilling();
					pd.setSkill4(true);
					fighter.startSkill4();
				break;
				
				case "Chip": //$NON-NLS-1$
					pd.setLeft(true);
					pd.setRunning(true);
					pd.enableSkilling();
					pd.setSkill2(true);
					fighter.startSkill2();
				break;
				
				case "Surge": //$NON-NLS-1$
					pd.enableSkilling();
					pd.setSkill2(true);
					fighter.startSkill2();
				break;
				
				case "Lust": //$NON-NLS-1$
					
				break;
			}
		}
	}
	public void stopCombo() {
		getScore().setKills(new int[] {MathUtil.nextInt(20, 40),MathUtil.nextInt(20, 40)});
		for(Map.Entry<Connection, Fighter> entry : getPlayers().entrySet()) {
			PlayerData pd = entry.getValue().getPlayer();
			pd.maxHP();
			pd.getHP().subX(200);
			pd.setLeft(false);
			pd.setRight(false);
			pd.setRunning(false);
			pd.setAAttack(false);
			pd.setJump(false);
		}
	}
	
	// Approve Resources
	public void startMatch() {
		for(Connection cnct : getPlayers().keySet()) {
			approveResources(cnct);
		}
	}
	protected void approveResources(Connection connection) {
		if(getPlayers().containsKey(connection)) {
			Packet1Connected res = new Packet1Connected();
			res = new Packet1Connected();
			res.map = MapManager.getDefaultMap();
			res.theClient = getPlayers().get(connection).getPlayer();
			res.players = getPlayers().getOtherPlayersData(connection);
			res.flags = getFlags().getData();
			res.warmup = getWarmup().getCounter();
			connection.sendTCP(res);
		}
	}
	
	/**
	 * 向客户端发送udp消息
	 * @param packet
	 */
	protected void updateClients(Packet packet) {
		for(Connection cnct : getPlayers().keySet()) {
			cnct.sendUDP(packet);
		}
	}
	/**
	 * 向客户端发送tcp消息
	 * @param packet
	 */
	protected void updateClientsTCP(Packet packet) {
		for(Connection cnct : getPlayers().keySet()) {
			cnct.sendTCP(packet);
		}
	}
	
	// Remove Match
	protected void removeMatch() {
		updateClientsTCP(new Packet2MatchOver());
		GameMatchManager.removeMatch(getID());
	}
	protected void removeMatch(Iterator<Map.Entry<String,GameMatch>> iter) {
		updateClientsTCP(new Packet2MatchOver());
		System.out.println("MATCH REMOVED"); //$NON-NLS-1$
		iter.remove();
	}
	
	// Map
	
}
