package com.brutalfighters.server.main;

import java.io.IOException;

import com.brutalfighters.server.base.GameServer;
/**
 * 程序入口，主函数
 *
 */
public class Main {

	public static void main(String[] args) throws IOException {
		// 加载游戏服务器
		GameServer.Load();
	}

}
