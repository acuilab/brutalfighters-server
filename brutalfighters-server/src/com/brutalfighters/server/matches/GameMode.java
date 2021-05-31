package com.brutalfighters.server.matches;

// 游戏模式
//	常规赛：人满了才进入比赛
//	自由赛：不必等人满就可以进入比赛
public enum GameMode {
	MATCH, FREESTYLE;
	
	public static boolean contains(String gamemode) {
		for (GameMode gm : GameMode.values()) {
			if (gm.name().equals(gamemode)) {
				return true;
			}
		}
		return false;
	}
}
