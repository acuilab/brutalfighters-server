package com.brutalfighters.server.matches;

import java.util.Iterator;
import java.util.Map;

/**
 * 打开的游戏比赛（指的是玩家匹配阶段，用于处理匹配结束而比赛开始的逻辑）
 *
 */
public class OpenGameMatch extends GameMatch {
	
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
	public OpenGameMatch(String mapName, String ID) {
		super(mapName, ID);
	}

	/* Updates */
	@Override
	public void updateMatch(Iterator<Map.Entry<String,GameMatch>> iter) {
		if(isFull() && isOpen()) {
			// 游戏比赛满了，并且还是打开的状态
			System.out.println("Yay! an open match is ready to be closed! :-)");
			// 将比赛设置为关闭状态
			close();
			// 设置并开始比赛
			GameMatchManager.closedMatches().getMatch(
					GameMatchManager.closedMatches().setupMatch(getMapName(), getPlayers(), teams)
			).startMatch();
			
			iter.remove();
		}
	}
}