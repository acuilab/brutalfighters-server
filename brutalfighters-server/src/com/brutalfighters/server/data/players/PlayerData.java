package com.brutalfighters.server.data.players;

import com.brutalfighters.server.data.buffs.BuffData;
import com.brutalfighters.server.util.Vec2;

public class PlayerData {
	
	// Basic
	// 基础
	private String name;	// 名称
	
	private Vec2 pos;		// 位置(二维矢量)
	
	private Vec2 size;		// 大小(二维矢量)
	
	private Vec2 hp;		// 血量值(二维矢量)：x矢量代表实际血量，y矢量代表最大血量
	
	private Vec2 mana;		// 魔法值（二维矢量）：x矢量代表实际魔法值，y矢量代表最大魔法值
	
	// The new velocity way, post processing position.
	// 新的速度方式，后处理位置。 (二维矢量)
	private Vec2 vel;
	
	// Match Team ID
	// 比赛队伍id
	private int team;
	
	// Death
	// 死亡cd（冷却时间）
	private int DCD;
	
	// Buffs
	// 增益数组
	private BuffData[] buffs;
	
	// Skills
	// 技能cd数组（冷却时间）
	private int[] skillCD;
	
	// Flip
	// 翻转
	private String flip;
	
	// States
	// 状态
	private boolean onGround;		// 是否在地面上
	private boolean isRunning;		// 是否在跑步
	private boolean isAAttack;		// 是否是范围攻击？？
	private boolean isDead;			// 是否死亡
	private boolean isVulnerable;	// 是否虚弱
	private boolean isFlagged;		// 是否扛旗
	private boolean hasControl;		// 拥有控制权
	
	// Skill States
	// 技能状态
	private boolean isSkill1;		// 是否使用技能1
	private boolean isSkill2;		// 是否使用技能2
	private boolean isSkill3;		// 是否使用技能3
	private boolean isSkill4;		// 是否使用技能4
	private boolean isSkilling;		// 是否在使用技能
	
	// Teleport
	private boolean isTeleporting;	// 是否允许瞬移
	
	// Input
	private boolean isLeft;			// 是否面向左
	private boolean isRight;		// 是否面向右
	private boolean isJump;			// 是否在跳跃
	
	// Collision
	private boolean collidesLeft;	// 是否碰左边
	private boolean collidesRight;	// 是否碰右边
	private boolean collidesTop;	// 是否碰上边
	private boolean collidesBot;	// 是否碰下边
	
	// Extrapolation
	private boolean isExtrapolating;	// ??

	/**
	 * 构造函数
	 * @param team			队伍
	 * @param pos			位置
	 * @param flip			是否翻转
	 * @param name			名称
	 * @param maxhp			最大血量值
	 * @param maxmana		最大魔法值
	 * @param size			大小
	 * @param maxSkillCD	最大技能cd
	 * @param DCD			死亡cd
	 */
	public PlayerData(int team, Vec2 pos, String flip, String name, float maxhp, float maxmana, Vec2 size, int[] maxSkillCD, int DCD) {
		
		// Basic
		//setName(Character.toUpperCase(name.charAt(0)) + name.substring(1));
		setName(name);
		setTeam(team);
		setPos(pos);
		setFlip(flip);
		setVel(new Vec2(0,0));
		setSize(size);
		
		// Health and Mana
		setHP(new Vec2(maxhp));
		setMana(new Vec2(maxmana));
		
		// CD
		setDCD(DCD);
		setSkillCD(maxSkillCD.clone());
		
		// Buffs
		setBuffs(new BuffData[0]);
		
		// States
		setRunning(false);
		isOnGround(false);
		setAAttack(false);
		setDead(false);
		setVulnerable(true);
		setFlagged(false);
		setControl(true);
		enableExtrapolating();
		
		// Skill States
		setSkill1(false);		// 技能1未使用
		setSkill2(false);		// 技能2未使用
		setSkill3(false);		// 技能3未使用
		setSkill4(false);		// 技能4未使用
		setSkilling(false);		// 未使用技能
		
		// Teleport
		disableTeleporting();	// 禁止瞬移
		
		// Movement States
		setLeft(false);
		setRight(false);
		setJump(false);
		setRunning(false);
		setDead(false);
		
		// Collisions
		isCollidingLeft(false);
		isCollidingRight(false);
		isCollidingTop(false);
		isCollidingBot(false);
	}
	private PlayerData() {
		this(-1, new Vec2(), "right", "dummy", 10, 10, new Vec2(), new int[0], 10); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	
	public String getName() {
		return name;
	}
	private void setName(String name) {
		this.name = name;
	}

	public Vec2 getPos() {
		return pos;
	}
	public void setPos(Vec2 pos) {
		this.pos = new Vec2(pos);
	}

	public Vec2 getSize() {
		return size;
	}
	public void setSize(Vec2 size) {
		this.size = new Vec2(size);
	}

	/**
	 * 获得血量
	 * @return
	 */
	public Vec2 getHP() {
		return hp;
	}
	/**
	 * 是否已经没有血量
	 * @return
	 */
	public boolean hasNoHP() {
		return hp.getX() <= 0;
	}
	/**
	 * 是否还有血量
	 * @return
	 */
	public boolean hasHP() {
		return !hasNoHP();
	}
	/**
	 * 设置血量
	 * @param hp
	 */
	public void setHP(Vec2 hp) {
		this.hp = new Vec2(hp);
	}
	/**
	 * 非死亡状态，将当前血量设置为最大血量
	 */
	public void maxHP() {
		if(!isDead()) {
			getHP().setX(getHP().getY());
		}
	}
	/**
	 * 将当前血量设置为0
	 */
	public void zeroHP() {
		getHP().resetX();
	}

	/**
	 * 获得魔法值
	 * @return
	 */
	public Vec2 getMana() {
		return mana;
	}
	/**
	 * 是否没有魔法值
	 * @return
	 */
	public boolean hasNoMana() {
		return !hasMana();
	}
	/**
	 * 是否还有魔法值
	 * @return
	 */
	public boolean hasMana() {
		return mana.getX() > 0;
	}
	/**
	 * 设置魔法值
	 * @param mana
	 */
	public void setMana(Vec2 mana) {
		this.mana = new Vec2(mana);
	}
	/**
	 * 非死亡状态，将当前魔法值设置为最大
	 */
	public void maxMana() {
		if(!isDead()) {
			getMana().setX(getMana().getY());
		}
	}
	/**
	 * 将当前魔法值设置为0
	 */
	public void zeroMana() {
		getMana().resetX();
	}

	/**
	 * 获得速度
	 * @return
	 */
	public Vec2 getVel() {
		return vel;
	}
	/**
	 * 设置速度
	 * @param vel
	 */
	public void setVel(Vec2 vel) {
		this.vel = new Vec2(vel);
	}
	/**
	 * 获得队伍
	 * @return
	 */
	public int getTeam() {
		return team;
	}
	/**
	 * 设置队伍
	 * @param team
	 */
	public void setTeam(int team) {
		this.team = team;
	}

	/**
	 * 获得死亡cd
	 * @return
	 */
	public int getDCD() {
		return DCD;
	}
	/**
	 * 是否还在死亡cd中
	 * @return
	 */
	public boolean isDCD() {
		return DCD > 0;
	}
	/**
	 * 减少死亡cd
	 * @param sub
	 */
	public void subDCD(int sub) {
		setDCD(getDCD()-sub);
	}
	/**
	 * 设置死亡cd
	 * @param dCD
	 */
	public void setDCD(int dCD) {
		DCD = dCD;
	}
	/**
	 * 获得所有增益数据（名称和时间）
	 * @return
	 */
	public BuffData[] getBuffs() {
		return buffs;
	}
	/**
	 * 设置增益数据
	 * @param buffs
	 */
	public void setBuffs(BuffData[] buffs) {
		this.buffs = buffs;
	}
	/**
	 * 重置增益数据
	 * @param length
	 */
	public void resetBuffs(int length) {
		this.buffs = new BuffData[length];
	}

	/**
	 * 获得技能cd数组
	 * @return
	 */
	public int[] getSkillCD() {
		return skillCD;
	}
	/**
	 * 设置技能cd
	 * @param skillCD
	 */
	public void setSkillCD(int[] skillCD) {
		this.skillCD = skillCD;
	}

	/**
	 * 获得翻转
	 * @return
	 */
	public String getFlip() {
		return flip;
	}
	/**
	 * 设置翻转
	 * @param flip
	 */
	public void setFlip(String flip) {
		this.flip = flip;
	}
	/**
	 * 右翻转
	 */
	public void flipRight() {
		flip = "right"; //$NON-NLS-1$
	}
	/**
	 * 左翻转
	 */
	public void flipLeft() {
		flip = "left"; //$NON-NLS-1$
	}
	/**
	 * 正面向右
	 * @return
	 */
	public boolean facingRight() {
		return getFlip().equals("right"); //$NON-NLS-1$
	}
	/**
	 * 正面向左
	 * @return
	 */
	public boolean facingLeft() {
		return getFlip().equals("left"); //$NON-NLS-1$
	}
	

	/**
	 * 是否在地面上
	 * @return
	 */
	public boolean onGround() {
		return onGround;
	}
	/**
	 * 设置是否在地面上
	 * @param onGround
	 */
	public void isOnGround(boolean onGround) {
		this.onGround = onGround;
	}

	/**
	 * 是否在跑步
	 * @return
	 */
	public boolean isRunning() {
		return isRunning;
	}
	/**
	 * 设置是否在跑步
	 * @param isRunning
	 */
	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	/**
	 * 是否是范围攻击
	 * @return
	 */
	public boolean isAAttack() {
		return isAAttack;
	}
	/**
	 * 设置是否是范围攻击
	 * @param isAAttack
	 */
	public void setAAttack(boolean isAAttack) {
		this.isAAttack = isAAttack;
	}

	/**
	 * 是否死亡
	 * @return
	 */
	public boolean isDead() {
		return isDead;
	}
	/**
	 * 设置是否死亡
	 * @param isDead
	 */
	private void setDead(boolean isDead) {
		this.isDead = isDead;
	}
	/**
	 * 设置为死亡
	 */
	public void died() {
		this.isDead = true;
	}
	/**
	 * 设置为活着
	 */
	public void alive() {
		this.isDead = false;
	}

	/**
	 * 是否虚弱
	 * @return
	 */
	public boolean isVulnerable() {
		return isVulnerable;
	}
	/**
	 * 设置是否虚弱
	 * @param isVulnerable
	 */
	public void setVulnerable(boolean isVulnerable) {
		this.isVulnerable = isVulnerable;
	}

	/**
	 * 是否在扛旗
	 * @return
	 */
	public boolean isHoldingFlag() {
		return isFlagged;
	}
	/**
	 * 设置是否在扛旗
	 * @param isFlagged
	 */
	private void setFlagged(boolean isFlagged) {
		this.isFlagged = isFlagged;
	}
	/**
	 * 偷旗
	 */
	public void stoleFlag() {
		this.isFlagged = true;
	}
	/**
	 * 放旗
	 */
	public void droppedFlag() {
		this.isFlagged = false;
	}

	/**
	 * 是否拥有控制权
	 * @return
	 */
	public boolean hasControl() {
		return hasControl;
	}
	/**
	 * 设置是否拥有控制权
	 * @param hasControl
	 */
	public void setControl(boolean hasControl) {
		this.hasControl = hasControl;
	}

	public boolean isSkill1() {
		return isSkill1;
	}
	public void setSkill1(boolean isSkill1) {
		this.isSkill1 = isSkill1;
	}

	public boolean isSkill2() {
		return isSkill2;
	}
	public void setSkill2(boolean isSkill2) {
		this.isSkill2 = isSkill2;
	}

	public boolean isSkill3() {
		return isSkill3;
	}
	public void setSkill3(boolean isSkill3) {
		this.isSkill3 = isSkill3;
	}

	public boolean isSkill4() {
		return isSkill4;
	}
	public void setSkill4(boolean isSkill4) {
		this.isSkill4 = isSkill4;
	}

	public boolean isSkilling() {
		return isSkilling;
	}
	private void setSkilling(boolean isSkilling) {
		this.isSkilling = isSkilling;
	}
	public void disableSkilling() {
		setSkilling(false);
	}
	public void enableSkilling() {
		setSkilling(true);
	}

	/**
	 * 是否允许瞬移
	 * @return
	 */
	public boolean isTeleporting() {
		return isTeleporting;
	}
	/**
	 * 允许瞬移
	 */
	public void enableTeleporting() {
		this.isTeleporting = true;
	}
	/**
	 * 禁止瞬移
	 */
	public void disableTeleporting() {
		this.isTeleporting = false;
	}

	public boolean isLeft() {
		return isLeft;
	}
	public void setLeft(boolean isLeft) {
		this.isLeft = isLeft;
	}

	public boolean isRight() {
		return isRight;
	}
	public void setRight(boolean isRight) {
		this.isRight = isRight;
	}

	/**
	 * 是否跳跃
	 * @return
	 */
	public boolean isJump() {
		return isJump;
	}
	/**
	 * 设置是否跳跃
	 * @param isJump
	 */
	public void setJump(boolean isJump) {
		this.isJump = isJump;
	}

	public boolean isCollidingLeft() {
		return collidesLeft;
	}
	public void isCollidingLeft(boolean collidesLeft) {
		this.collidesLeft = collidesLeft;
	}

	public boolean isCollidingRight() {
		return collidesRight;
	}
	public void isCollidingRight(boolean collidesRight) {
		this.collidesRight = collidesRight;
	}

	public boolean isCollidingTop() {
		return collidesTop;
	}
	public void isCollidingTop(boolean collidesTop) {
		this.collidesTop = collidesTop;
	}

	public boolean isCollidingBot() {
		return collidesBot;
	}
	public void isCollidingBot(boolean collidesBot) {
		this.collidesBot = collidesBot;
	}

	public boolean isExtrapolating() {
		return isExtrapolating;
	}
	public void disableExtrapolating() {
		this.isExtrapolating = false;
	}
	public void enableExtrapolating() {
		this.isExtrapolating = true;
	}
	
	public boolean isWalking() {
		return isRight() || isLeft();
	}
	
	public boolean movingX() {
		return getVel().getX() != 0;
	}
	public boolean movingY() {
		return getVel().getY() != 0;
	}
	
	public void applyVelX() {
		getPos().addX(getVel().getX());
	}
	public void applyVelY() {
		getPos().addY(getVel().getY());
	}
	
	public boolean moveLeft() {
		return getVel().getX() < 0 && !collidesLeft;
	}
	public boolean moveRight() {
		return getVel().getX() > 0 && !collidesRight;
	}
	
	public boolean canAAttack() {
		return hasControl && !isSkilling && onGround() && getVel().getY() == 0 && !(isRunning() && getVel().getX() != 0);
	}
	
	/**
	 * 是否在半空中
	 * @return
	 */
	public boolean isMidAir() {
		// 不在地面上且没有撞到下面
		// 或者垂直方向的速度大于0（在向上跳起状态?）
		return (!onGround() && !isCollidingBot()) || getVel().getY() > 0; // It's good! it should be &&!
	}
	
	// Boundary Methods
	public float getLeft() {
		return -getSize().getX()/2;
	}
	public float getRight() {
		return getSize().getX()/2;
	}
	public float getTop() {
		return getSize().getY()/2;
	}
	public float getBot() {
		return -getSize().getY()/2;
	}
	
	/**
	 * 是否面向障碍物
	 * @return
	 */
	public boolean isFacingCollision() {
		// 面向右且右边是障碍物
		// 或者面向左且左边是障碍物
		return (getFlip().equals("right") && isCollidingRight()) || (getFlip().equals("left") && isCollidingLeft()); //$NON-NLS-1$ //$NON-NLS-2$
	}
}