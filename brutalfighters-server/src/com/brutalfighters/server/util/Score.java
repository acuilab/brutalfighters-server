package com.brutalfighters.server.util;

/**
 * 游戏分数
 *
 */
public class Score {
	private int flags[];	// 每队的旗帜数
	private int kills[];	// 每队的杀人数
	
	public Score() {
		setFlags(new int[] {0,0});
		setKills(new int[] {0,0});
	}

	public int[] getFlags() {
		return flags;
	}
	public void setFlags(int[] flags) {
		this.flags = flags;
	}

	public int[] getKills() {
		return kills;
	}
	public void setKills(int[] kills) {
		this.kills = kills;
	}
}
