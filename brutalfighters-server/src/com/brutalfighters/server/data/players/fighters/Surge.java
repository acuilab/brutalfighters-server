package com.brutalfighters.server.data.players.fighters;

import com.brutalfighters.server.base.GameServer;
import com.brutalfighters.server.data.buffs.Buff;
import com.brutalfighters.server.data.buffs.Buff_IceStun;
import com.brutalfighters.server.data.buffs.Buff_Slow;
import com.brutalfighters.server.data.maps.Base;
import com.brutalfighters.server.data.projectiles.types.BlueDashBall;
import com.brutalfighters.server.data.projectiles.types.BlueEnergyWave;
import com.brutalfighters.server.matches.GameMatchManager;
import com.brutalfighters.server.util.AOE;
import com.brutalfighters.server.util.CollisionDetection;
import com.brutalfighters.server.util.Vec2;
import com.esotericsoftware.kryonet.Connection;

/**
 * 战士Surge
 *
 */
public class Surge extends Fighter {

	public Surge(Connection connection, int team, Base base, String m_id) {
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
		 * @param max_skillCD		// 最大技能cd
		 */
		super(connection, 
				team, 		// 队伍
				base, 		// 位置和朝向
				m_id,		// 比赛id
				"Surge",	// 名称
				1000, 		// 最大hp
				1000, 		// 最大mp
				new Vec2(90,100), // 最大尺寸
				8, 		// 行走速度
				16, 	// 跑步速度
				52, 	// 跳跃高度
				500, 	// 自动攻击cd
				new Vec2(200,50), 	// 自动攻击范围
				68, 	// 自动攻击伤害
				9,		// 法力回复
				new int[] {250,400,200,650}, 	// 技能消耗法力值
				new int[] {500,900,1200,1300}	// 技能cd最大值
		);
	}
	
	// SKILLS（每人4个技能）
	
	// ##### Skill 1 #####
	final int DMG = 20;
	
	@Override
	public void updateSkill1() {
		applyJump();
		applyGravity();
		applyWalking();
	}
	
	@Override
	public void skill1() {
		
		updateSkill1();
		
		if(getPlayer().getSkillCD()[0] > 0) {
				if(getPlayer().getSkillCD()[0] == getMaxSkillCD()[0] - GameServer.getDelay() * 5) {
					float xstart = getPlayer().getPos().getX() + convertSpeed(5);
					GameMatchManager.getCurrentProjectiles().add(new BlueDashBall(this, getPlayer().getFlip(), new Vec2(xstart, getPlayer().getPos().getY()), 30, 100, new Buff[0])); 
				}
				if(getPlayer().getSkillCD()[0] > getMaxSkillCD()[0] - GameServer.getDelay() * 5) {
					AOE.dealAOE_enemy(getPlayer().getTeam(), CollisionDetection.getBounds(getPlayer().getFlip(), getPlayer().getPos().getX(), getPlayer().getPos().getY(), 150, 30), -DMG, new Buff[] {(new Buff_Slow(2))});
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
	public void updateSkill2() {
		defaultUpdate();
	}
	
	@Override
	public void skill2() {
		
		updateSkill2();
		
		if(getPlayer().getSkillCD()[1] > 0) {
				if(getPlayer().getSkillCD()[1] == getMaxSkillCD()[1] - GameServer.getDelay() * 17) {
					float xstart = getPlayer().getPos().getX() + convertSpeed(getPlayer().getSize().getX()*2);
					GameMatchManager.getCurrentProjectiles().add(new BlueEnergyWave(this, getPlayer().getFlip(), new Vec2(xstart, getPlayer().getPos().getY()), 17, 200, new Buff[] {(new Buff_IceStun())}));
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
	}
	
	// ##### Skill 3 #####
	
	@Override
	public void updateSkill3() {
		defaultUpdate();
		applyFlip();
	}
	
	@Override
	public void skill3() {
		updateSkill3();
		
		if(getPlayer().getSkillCD()[2] > 0) {
			if(getPlayer().getSkillCD()[2] == getMaxSkillCD()[2] - GameServer.getDelay() * 12) {
				getPlayer().setVulnerable(false);
			}
			getPlayer().getSkillCD()[2] -= GameServer.getDelay();
		} else {
			endSkill3();
		}
	}
	
	@Override
	public void endSkill3() {
		getPlayer().getSkillCD()[2] = getMaxSkillCD()[2];
		getPlayer().setSkill3(false);
		getPlayer().disableSkilling();
		getPlayer().setVulnerable(true);
	}
	
	// ##### Skill 4 #####
	
	final int S4_dmg = 300, S4_X_RANGE = 150, S4_Y_RANGE = 350, DISTANCE = 230;
	
	@Override
	public void skill4() {
		updateSkill4();
		
		if(getPlayer().getSkillCD()[3] > 0) {
			if(getPlayer().getSkillCD()[3] == getMaxSkillCD()[3] - GameServer.getDelay() * 14) {
//						dealAOE_enemy(getPlayer().getTeam(),"both", getPlayer().getPos().getX()-getPlayer().width-DISTANCE+S4_X_RANGE/2, getPlayer().getPos().getY()-getPlayer().height/2+S4_Y_RANGE, S4_X_RANGE, S4_Y_RANGE, -S4_dmg);
				AOE.dealAOE_enemy(getPlayer().getTeam(), CollisionDetection.getBounds("both", getPlayer().getPos().getX()-getPlayer().getSize().getX()-DISTANCE, getPlayer().getPos().getY(), S4_X_RANGE, S4_Y_RANGE), -S4_dmg); //$NON-NLS-1$
//						dealAOE_enemy(getPlayer().getTeam(),"both", getPlayer().getPos().getX()+getPlayer().width+DISTANCE-S4_X_RANGE/2, getPlayer().getPos().getY()-getPlayer().height/2+S4_Y_RANGE, S4_X_RANGE, S4_Y_RANGE, -S4_dmg);
				AOE.dealAOE_enemy(getPlayer().getTeam(), CollisionDetection.getBounds("both", getPlayer().getPos().getX()+getPlayer().getSize().getX()+DISTANCE, getPlayer().getPos().getY(), S4_X_RANGE, S4_Y_RANGE), -S4_dmg); //$NON-NLS-1$
			}
			getPlayer().getSkillCD()[3] -= GameServer.getDelay();
		} else {
			endSkill4();
		}
		
	}
	
	@Override
	public void endSkill4() {
		getPlayer().getSkillCD()[3] = getMaxSkillCD()[3];
		getPlayer().setSkill4(false);
		getPlayer().disableSkilling();
	}
}
