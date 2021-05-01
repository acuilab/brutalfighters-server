package com.brutalfighters.server.data.buffs;

import java.util.Iterator;

import com.brutalfighters.server.base.GameServer;
import com.brutalfighters.server.data.players.fighters.Fighter;

/**
 * 增益
 *
 */
abstract public class Buff {
	
	protected final int MAX_TIME;	// 最大时间
	
	protected BuffData buff;		// 增益数据
	protected boolean isStarted;	// 是否已开始
	
	protected Buff(String name, int MAX_TIME) {
		this.MAX_TIME = MAX_TIME;
		setStarted(false);
		setBuff(new BuffData(name, getMAX_TIME()));
	}
	protected Buff(String name) {
		this.MAX_TIME = 0;
		setBuff(new BuffData(name, getMAX_TIME()));
	}
	
	public BuffData getBuff() {
		return buff;
	}
	public void setBuff(BuffData buff) {
		this.buff = buff;
	}
	
	public boolean isStarted() {
		return isStarted;
	}
	protected void setStarted(boolean isStarted) {
		this.isStarted = isStarted;
	}
	public void started() {
		setStarted(true);
	}
	
	public int getMAX_TIME() {
		return MAX_TIME;
	}
	
	public void tick(Fighter p, Iterator<Buff> iterator) {
		if(isStarted()) {
			update(p, iterator);
		} else {
			start(p, iterator);
			started();
		}
	}

	/**
	 * 增益开始
	 * @param p
	 * @param iterator
	 */
	protected void start(Fighter p, Iterator<Buff> iterator) {
		
	}
	/**
	 * 增益更新
	 * @param p
	 * @param iterator
	 */
	protected void update(Fighter p, Iterator<Buff> iterator) {
		isActive(p, iterator);
	}
	/**
	 * 增益结束
	 * @param p
	 * @param iterator
	 */
	protected void end(Fighter p, Iterator<Buff> iterator) {
		iterator.remove();
	}
	/**
	 * 增益是否在激活状态（同时执行相关逻辑）
	 * 如果尚在激活状态，则更新增益时间；否则结束增益
	 * @param p
	 * @param iterator
	 * @return
	 */
	protected final boolean isActive(Fighter p, Iterator<Buff> iterator) {
		if(getBuff().getTime() > 0) {
			getBuff().setTime(getBuff().getTime()-GameServer.getDelay());
			return true;
		}
		end(p, iterator);
		return false;
	}
	/**
	 * 是否在增益时间（不执行具体逻辑）
	 * @param number
	 * @return
	 */
	protected final boolean isTime(int number) {
		return getBuff().getTime() / GameServer.getDelay() % number == 0;
	}
	/**
	 * 获得新的增益
	 * @return
	 */
	abstract public Buff getNewBuff();
	
}
