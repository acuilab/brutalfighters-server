package com.brutalfighters.server.data.projectiles;

import java.awt.Rectangle;
import java.util.Iterator;

import com.brutalfighters.server.base.GameServer;
import com.brutalfighters.server.data.buffs.Buff;
import com.brutalfighters.server.data.objects.Collidable;
import com.brutalfighters.server.data.players.fighters.Fighter;
import com.brutalfighters.server.matches.GameMatchManager;
import com.brutalfighters.server.util.AOE;
import com.brutalfighters.server.util.CollisionDetection;
import com.brutalfighters.server.util.Vec2;
import com.esotericsoftware.kryonet.Connection;

/**
 * 抛射物抽象类
 * Projectile/ProjectileData
 * ProjectileData是否送给客户端的
 * 
 *
 */
abstract public class Projectile extends Collidable {
	
	protected Fighter fighter;	// 所属战士
	protected int team;			// 所属队伍
	
	protected float dmg;		// 伤害
	protected Buff[] buffs;		// 增益效果
	protected float speed;		// 速度
	
	protected ProjectileData projectile;	// 抛射物数据
	
	protected Projectile(String name, Fighter fighter, String flip, Vec2 pos, Vec2 size, float speed, float dmg, Buff[] buffs) {
		setProjectile(new ProjectileData(name, flip, pos, size));
		setFighter(fighter);
		setTeam(fighter.getPlayer().getTeam());
		setSpeed(speed);
		setDMG(dmg);
		setBuffs(buffs);
		Rectangle(CollisionDetection.getBounds("both", getProjectile().getPos().getX(), getProjectile().getPos().getY(), getProjectile().getSize().getX(), getProjectile().getSize().getY())); //$NON-NLS-1$
	}
	
	public ProjectileData getProjectile() {
		return projectile;
	}
	public void setProjectile(ProjectileData projectile) {
		this.projectile = projectile;
	}

	public Fighter getFighter() {
		return fighter;
	}
	public void setFighter(Fighter fighter) {
		this.fighter = fighter;
	}

	public int getTeam() {
		return team;
	}
	public void setTeam(int team) {
		this.team = team;
	}

	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getDMG() {
		return dmg;
	}
	public void setDMG(float dmg) {
		this.dmg = dmg;
	}

	public Buff[] getBuffs() {
		return buffs;
	}
	public void setBuffs(Buff[] buffs) {
		this.buffs = buffs;
	}
	
	@Override
	public Rectangle getBounds() {
		setBounds();
		return bounds;
	}
	
	/**
	 * 根据抛射物的位置和大小为抛射物设置碰撞边框
	 */
	public void setBounds() {
		CollisionDetection.setBounds(bounds, 
				"both", 
				getProjectile().getPos().getX(), 
				getProjectile().getPos().getY(), 
				getProjectile().getSize().getX(), 
				getProjectile().getSize().getY());
	}
	
	/**
	 * 某个连接是否是抛射物的所有人
	 * @param cnct
	 * @return
	 */
	public boolean isOwner(Connection cnct) {
		return getFighter().getConnection().equals(cnct);
	}
	/**
	 * 某个战士是否是抛射物的所有人
	 * @param fighter
	 * @return
	 */
	public boolean isOwner(Fighter fighter) {
		return getFighter().equals(fighter);
	}

	/**
	 * 初始化（子类通常需要覆写update和initialize方法）
	 */
	public void initialize() {
		if(isColliding()) {
			getProjectile().setExplode();
		} else {
			getProjectile().getVel().setX(convertSpeed(speed));
			getProjectile().getVel().setX(convertSpeed(speed));
		}
	}
	/**
	 * 更新（子类通常需要覆写update和initialize方法）
	 * @param iterator
	 */
	public void update(Iterator<Projectile> iterator) {
		getProjectile().addTime(GameServer.getDelay());
		if(!getProjectile().isExplode()) {
			if(dealDamage()) {
				getProjectile().setExplode();
			} else if(!isColliding()) {
				getProjectile().getPos().addX(getProjectile().getVel().getX());
			} else {
				getProjectile().setExplode();
				return;
			}
		} else {
			iterator.remove();
		}
	}
	
	/**
	 * 抛射物是否正在发生碰撞
	 * 我们可能会对其进行修改，因此它不是静态的。
	 * @return
	 */
	public boolean isColliding() { // We may modify it, so it's not static.
		// 抛射物位于上下左右边界之外
		return !GameMatchManager.getCurrentMap().checkBoundaries(getProjectile().getPos()) || 
				GameMatchManager.getCurrentMap().intersects(getProjectile().getPos().getX(), getProjectile().getPos().getY(), getBounds());
	}
	
	public boolean dealDamage() {
		return AOE.dealAOE_enemy(getTeam(), getBounds(), -getDMG(), getBuffs());
	}
	
	public float convertSpeed(float speed) {
		return getProjectile().isRight() ? speed : -speed;
	}
}
