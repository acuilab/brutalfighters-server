package com.brutalfighters.server.base;

import java.io.IOException;

import com.brutalfighters.server.packets.GameMatchPacket;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

/**
 * MPServer代表multiplayer server?
 * MPServer是对kryonet和kryo的封装
 *
 */
public class MPServer {
	
	private Server server;
	private Kryo kryo;
	
	public MPServer(Listener listener) throws IOException {
		server = new Server(33668, 4096);
	    server.start();
		server.bind(54777, 54666);	// 绑定tcp和udp端口
		
		kryo = server.getKryo();	// 获得序列化对象
		registerPackets();			// 注册数据包
		
		server.addListener(listener);	// 增加监听器
	}
	
	private void registerPackets() {
		kryo.register(GameMatchPacket.class);
	}
	
	/**
	 * 获得kryo序列化对象
	 * @return
	 */
	public Kryo getKryo() {
		return kryo;
	}
	
	/**
	 * 获得server对象
	 * @return
	 */
	public Server getServer() {
		return server;
	}
}
