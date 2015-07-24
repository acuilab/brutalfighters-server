package com.brutalfighters.server.base;

import com.brutalfighters.server.data.players.Champion;
import com.brutalfighters.server.data.players.PlayerData;
import com.brutalfighters.server.matches.GameMatchManager;
import com.brutalfighters.server.matches.GameMode;
import com.brutalfighters.server.packets.GameMatchPacket;
import com.brutalfighters.server.packets.ConnectGameMatch;
import com.brutalfighters.server.packets.Packet;
import com.brutalfighters.server.packets.Packet0ConnectMatch;
import com.brutalfighters.server.packets.Packet3InputAAttack;
import com.brutalfighters.server.packets.Packet3InputJump;
import com.brutalfighters.server.packets.Packet3InputLeft;
import com.brutalfighters.server.packets.Packet3InputRight;
import com.brutalfighters.server.packets.Packet3InputRun;
import com.brutalfighters.server.packets.Packet3InputSkill1;
import com.brutalfighters.server.packets.Packet3InputSkill2;
import com.brutalfighters.server.packets.Packet3InputSkill3;
import com.brutalfighters.server.packets.Packet3InputSkill4;
import com.brutalfighters.server.packets.Packet3InputTeleport;
import com.brutalfighters.server.packets.Packet4ReleaseAAttack;
import com.brutalfighters.server.packets.Packet4ReleaseJump;
import com.brutalfighters.server.packets.Packet4ReleaseLeft;
import com.brutalfighters.server.packets.Packet4ReleaseRight;
import com.brutalfighters.server.packets.Packet4ReleaseRun;
import com.brutalfighters.server.packets.Packet5EscapeMatch;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class NetworkListener extends Listener {
	@Override
	public void received(Connection connection, Object object) {
		if (object instanceof Packet) {

			if(object instanceof Packet5EscapeMatch) {
				System.out.println("ESCAPED"); //$NON-NLS-1$
				GameMatchManager.escapePlayer(connection);
				return;
			}
			
			if(object instanceof ConnectGameMatch) {
				if (object instanceof Packet0ConnectMatch) {
					System.out.println("HEY! new match player"); //$NON-NLS-1$
					Packet0ConnectMatch packet = ((Packet0ConnectMatch) object);
					if(GameMode.contains(packet.gamemode)) {
						GameMatchManager.connectPlayer(GameMode.valueOf(packet.gamemode), ((Packet0ConnectMatch) object).fighter, connection);
					}
					return;
				}
			}

			if(object instanceof GameMatchPacket) {
				PlayerData player = GameMatchManager.checkPlayer(connection);
				
				if(player != null) {
					if(!player.isDead) {
						if (object instanceof Packet3InputLeft) {
							player.isLeft = true;
							return;
						} else if (object instanceof Packet3InputRight) {
							player.isRight = true;
							return;
						} else if(object instanceof Packet3InputTeleport && player.hasControl && !player.isSkilling) {
							player.isTeleporting = true;
						}
						if(player.onGround) {
							if (object instanceof Packet3InputJump) {
								player.isJump = true;
								return;
							} else if (object instanceof Packet3InputRun) {
								player.isRunning = true;
								return;
							} else if (object instanceof Packet3InputAAttack) {
								player.isAAttack = true;
								return;
							}
							if(player.hasControl && !player.isSkilling) {
								
								 if(!player.isFlagged) { //!isFlagged
									
									Champion fighter = Champion.valueOf(player.name);
									if(object instanceof Packet3InputSkill1) {
										player.isSkilling = true;
										player.isSkill1 = true;
										fighter.startSkill1(player, connection);
//										GameMatchManager.getClosedMatch(connection).womboCombo(connection);
										return;
									} else if(object instanceof Packet3InputSkill2) {
										player.isSkilling = true;
										player.isSkill2 = true;
										fighter.startSkill2(player, connection);
//										GameMatchManager.getClosedMatch(connection).moveCombo(connection);
										return;
									} else if(object instanceof Packet3InputSkill3) {
										player.isSkilling = true;
										player.isSkill3 = true;
										fighter.startSkill3(player, connection);
//										GameMatchManager.getClosedMatch(connection).stopCombo(connection);
										return;
									} else if(object instanceof Packet3InputSkill4) {
										player.isSkilling = true;
										player.isSkill4 = true;
										fighter.startSkill4(player, connection);
//										GameMatchManager.getClosedMatch(connection).womboCombo(connection);
										return;
									}
								}
							}
						}
					}
		
					if (object instanceof Packet4ReleaseLeft) {
						player.isLeft = false;
						//player.velx = 0;
						return;
					} else if (object instanceof Packet4ReleaseRight) {
						player.isRight = false;
						//player.velx = 0;
						return;
					} else if (object instanceof Packet4ReleaseJump) {
						player.isJump = false;
						return;
					} else if (object instanceof Packet4ReleaseRun) {
						player.isRunning = false;
						return;
					} else if (object instanceof Packet4ReleaseAAttack) {
						player.isAAttack = false;
						return;
					}
				}
			}
		}
	}

	@Override
	public void connected(Connection connection) {

	}

	@Override
	public void disconnected(Connection connection) {
		GameMatchManager.disconnectPlayer(connection); // When a player is disconnected he won't be removed from the match, because he may reconnect.
	}

}
