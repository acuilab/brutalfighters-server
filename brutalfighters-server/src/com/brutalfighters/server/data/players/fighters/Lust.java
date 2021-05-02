package com.brutalfighters.server.data.players.fighters;

import com.brutalfighters.server.base.GameServer;
import com.brutalfighters.server.data.buffs.Buff;
import com.brutalfighters.server.data.buffs.Buff_AirJump;
import com.brutalfighters.server.data.buffs.Buff_Slow;
import com.brutalfighters.server.data.maps.Base;
import com.brutalfighters.server.data.projectiles.types.RedEnergyBall;
import com.brutalfighters.server.matches.GameMatchManager;
import com.brutalfighters.server.util.AOE;
import com.brutalfighters.server.util.CollisionDetection;
import com.brutalfighters.server.util.Vec2;
import com.esotericsoftware.kryonet.Connection;

/**
 * 战士Lust
 *
 */
public class Lust extends Fighter {
	
	public Lust(Connection connection, int team, Base base, String m_id) {
		/**
		 * 构造函数
		 * @param connection		// 连接
		 * @param team				// 队伍
		 * @param base				// 位置和朝向
		 * @param m_id				// 比赛id
		 * @param name				// 名称
		 * @param maxhp				// 最大hp
		 * @param maxmana			// 最大mp
		 * @param max_size			// 最大尺寸
		 * @param walking_speed		// 行走速度
		 * @param running_speed		// 跑步速度
		 * @param jump_height		// 跳跃高度
		 * @param AA_CD				// 自动攻击cd
		 * @param AA_range			// 自动攻击范围
		 * @param AA_DMG			// 自动攻击伤害
		 * @param manaRegen			// 法力回复
		 * @param skillMana			// 技能消耗法力值
		 * @param max_skillCD		// 技能cd最大值
		 */
		super(connection, 
				team, 		// 队伍
				base, 		// 位置和朝向
				m_id, 		// 比赛id
				"Lust", 	// 名称
				800, 		// 最大hp
				1000, 		// 最大mp
				new Vec2(90,100), // 最大尺寸
				14, 	// 行走速度
				26, 	// 跑步速度
				52, 	// 跳跃高度
				500, 	// 自动攻击cd
				new Vec2(200,50), 	// 自动攻击范围
				75, 	// 自动攻击伤害
				9,		// 法力回复
				new int[] {250,200,400,300}, 	// 技能消耗法力值
				new int[] {600,380,0,450}		// 技能cd最大值
		);
	}
	
	// SKILLS（每人4个技能）

	// ##### Skill 1 #####
	
	// Variables
	public final float S1_DMG = 200, S1_HEIGHT = getPlayer().getSize().getY()*2, S1_WIDTH = 100, S1_JUMP_HEIGHT = 55;
	
	@Override
	public void startSkill1() {
		if(!getPlayer().isCollidingTop() && applySkillMana(0)) {
			getPlayer().getVel().setY(S1_JUMP_HEIGHT);
		} else {
			endSkill1();
		}
	}
	
	@Override
	public void updateSkill1() {
		applyGravity();
		applyWalking();
	}

	@Override
	public void skill1() {
		
		updateSkill1();
		
		if(getPlayer().getSkillCD()[0] > 0 && getPlayer().isMidAir()) {
			if(getPlayer().getSkillCD()[0] == getMaxSkillCD()[0] - GameServer.getDelay()) {
				AOE.dealAOE_enemy(getPlayer().getTeam(), CollisionDetection.getBounds(getPlayer().getFlip(), getPlayer().getPos().getX(), getPlayer().getPos().getY(), S1_WIDTH, S1_HEIGHT), 0, new Buff[] {new Buff_AirJump()});
			} else if(getPlayer().getSkillCD()[0] == getMaxSkillCD()[0] - GameServer.getDelay() * 3) {
				AOE.dealAOE_enemy(getPlayer().getTeam(), CollisionDetection.getBounds(getPlayer().getFlip(), getPlayer().getPos().getX(), getPlayer().getPos().getY(), S1_WIDTH, S1_HEIGHT), -S1_DMG);
			}
			getPlayer().getSkillCD()[0] -= GameServer.getDelay();
		} else {
			endSkill1();
		}
	}
	
	@Override
	public void endSkill1() {
		getPlayer().getSkillCD()[0] = getMaxSkillCD()[0];
		getPlayer().setSkill1(false);
		getPlayer().disableSkilling();
	}
	
	
	
	
	// ##### Skill 2 #####
	@Override
	public void startSkill2() {
		if(applySkillMana(1)) {
			getRunningSpeed().setX(getWalkingSpeed().getX());
		} else {
			endSkill1();
		}
	}
	
	@Override
	public void updateSkill2() {
		applyJump();
		applyGravity();
		applyWalking();
	}
	
	@Override
	public void skill2() {
		
		updateSkill2();
		
		if(getPlayer().getSkillCD()[1] > 0) {
			if(getPlayer().getSkillCD()[1] == getMaxSkillCD()[1] - GameServer.getDelay() * 3) {
				float xstart = getPlayer().getPos().getX() + convertSpeed(10);
				GameMatchManager.getCurrentProjectiles().add(new RedEnergyBall(this, getPlayer().getFlip(), new Vec2(xstart, getPlayer().getPos().getY()-3), 20, 100, new Buff[] {new Buff_Slow(2)}));
			}
			getPlayer().getSkillCD()[1] -= GameServer.getDelay();
		} else {
			endSkill2();
		}
	}
	
	@Override
	public void endSkill2() {
		getPlayer().getSkillCD()[1] = getMaxSkillCD()[1];
		getPlayer().setSkill2(false);
		getPlayer().disableSkilling();
		resetSpeeds();
	}
	
	
	
	// ##### Skill 3 #####
	
	public final int S3_HP = 200;
	
	@Override
	public void startSkill3() {
		if(getPlayer().getHP().getX() < getPlayer().getHP().getY() && applySkillMana(2)) {
			applyHP(S3_HP);
			endSkill3();
		} else {
			endSkill3();
		}
	}
	
	@Override
	public void endSkill3() {
		getPlayer().getSkillCD()[2] = getMaxSkillCD()[2];
		getPlayer().setSkill3(false);
		getPlayer().disableSkilling();
	}
	
	
	
	
	// ##### Skill 4 #####
	
	@Override
	public void startSkill4() {
		if(applySkillMana(3)) {
			getPlayer().setVulnerable(false);
		} else {
			endSkill4();
		}
	}
	
	@Override
	public void updateSkill4() {
		defaultUpdate();
		applyFlip();
	}
	
	@Override
	public void skill4() {
		updateSkill4();
		
		getPlayer().getSkillCD()[3] -= GameServer.getDelay();
		if(getPlayer().getSkillCD()[3] <= 0) {
			endSkill4();
		}
	}
	
	@Override
	public void endSkill4() {
		getPlayer().getSkillCD()[3] = getMaxSkillCD()[3];
		getPlayer().setSkill4(false);
		getPlayer().disableSkilling();
		getPlayer().setVulnerable(true);
	}
}
