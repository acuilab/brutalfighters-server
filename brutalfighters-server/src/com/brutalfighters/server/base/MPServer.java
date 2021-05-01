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
		server.bind(54777, 54666);
		
		kryo = server.getKryo();
		registerPackets();
		
		server.addListener(listener);
	}
	
	private void registerPackets() {
		kryo.register(GameMatchPacket.class);
	}
	
	public Kryo getKryo() {
		return kryo;
	}
	
	public Server getServer() {
		return server;
	}
}
