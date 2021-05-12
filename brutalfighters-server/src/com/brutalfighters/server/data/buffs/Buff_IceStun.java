package com.brutalfighters.server.data.buffs;

import java.util.Iterator;

import com.brutalfighters.server.data.players.PlayerData;
import com.brutalfighters.server.data.players.fighters.Fighter;

/**
 * 冰眩增益
 *
 */
public class Buff_IceStun extends Buff {

	public Buff_IceStun(int MAX_TIME) {
		super("IceStun", MAX_TIME);
	}
	public Buff_IceStun() {
		this(3000);
	}
	
	/**
	 * 增益开始：
	 * 	速度，行走速度，跑步速度置为0；
	 * 	不可控制
	 * 	非虚弱
	 */
	@Override
	public void start(Fighter p, Iterator<Buff> iterator) {
		PlayerData player = p.getPlayer();
		player.getVel().resetX();
		player.getVel().resetY();
		p.getWalkingSpeed().resetX();
		p.getRunningSpeed().resetX();
		player.setControl(false);
		player.setVulnerable(false);
	}
	
	@Override
	public void update(Fighter p, Iterator<Buff> iterator) {
		// 若增益在激活状态，应用重力和行走
		if(isActive(p, iterator)) {
			p.applyGravity();
			p.applyWalking();
		}
	}
	
	@Override
	public void end(Fighter p, Iterator<Buff> iterator) {
		p.getPlayer().setControl(true);
		p.getPlayer().setVulnerable(true);
		
		p.resetSpeeds();
		
		iterator.remove();
	}
	
	/**
	 * 工厂方法
	 */
	@Override
	public Buff getNewBuff() {
		return new Buff_IceStun(getMAX_TIME());
	}
	
}
