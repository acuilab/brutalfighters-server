package com.brutalfighters.server.packets;

import com.brutalfighters.server.data.flags.FlagData;
import com.brutalfighters.server.data.players.PlayerData;
import com.brutalfighters.server.data.projectiles.ProjectileData;
import com.brutalfighters.server.util.Score;

/**
 * 玩家数据包
 */
public class Packet2Players extends GameMatchPacket {
	// 客户端玩家数据
	public PlayerData theClient;
	// 玩家数据(排除theClient剩下的玩家数据)
	public PlayerData[] players;
	// 抛射物(队伍抛射物数据)
	public ProjectileData[] projectiles;
	// 旗帜数据(队伍旗帜数据)
	public FlagData[] flags;
	// 分数(队伍旗帜数和杀人数)
	public Score score;
}
