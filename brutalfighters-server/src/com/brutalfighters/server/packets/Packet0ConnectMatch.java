package com.brutalfighters.server.packets;
 
/**
 * 连接比赛数据包
 *
 */
public class Packet0ConnectMatch extends ConnectGameMatch {
	/**
	 * username	玩家名称（貌似未使用）
	 */
	public String username, fighter, gamemode;
}
