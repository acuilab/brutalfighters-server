package com.brutalfighters.server.matches;

import java.util.Iterator;
import java.util.Map;

import com.brutalfighters.server.base.GameServer;
import com.brutalfighters.server.data.players.PlayerMap;

/**
 * 已关闭的游戏比赛
 * @author chia1
 *
 */
public class ClosedGameMatch extends GameMatch {
	
	/*
	 * 
	 * Warmup: 20sec
	 * Players: 6
	 * Win State: 3 flags
	 * Respawn: 6sec
	 * Match Finish: 6sec
	 * 
	 */
	
	/* Constructors */
	/**
	 * 构造函数
	 * @param mapName	地图名称
	 * @param ID		比赛id
	 * @param players	玩家
	 * @param teams		队伍
	 */
	public ClosedGameMatch(String mapName, String ID, PlayerMap players, PlayerMap[] teams) {
		super(mapName, ID, players, teams);
	}

	/* Updates */
	@Override
	public void updateMatch(Iterator<Map.Entry<String,GameMatch>> iter) {
		// 我们可以将此方法分为2种方法，以提高安全性。
		if(!gameFinished(iter)) { // We can split this method into 2 methods, for more security.
			// ###1 游戏没有结束
			if(warmup.isFinished()) {
				// 热身已结束
				updateGame();
				updateClients();
			} else {
				// 热身尚未结束
				// Although we update the warmup first and therefore skip the first second of update,
				// the client has the warmup already loaded.
				// 尽管我们首先更新了热身，因此跳过了更新的第一秒，但是客户端已经加载了更新。
				updateWarmup();
			}
		} else if(finish.getCounter() > 0) {
			// ###2 游戏正在结束倒计时
			finish.subCounter(GameServer.getDelay());
		} else {
			// ###3 游戏已结束，移除比赛
			removeMatch(iter);
		}
	}
}