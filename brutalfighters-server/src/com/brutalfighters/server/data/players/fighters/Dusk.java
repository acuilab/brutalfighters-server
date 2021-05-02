package com.brutalfighters.server.data.players.fighters;

import com.brutalfighters.server.base.GameServer;
import com.brutalfighters.server.data.buffs.Buff;
import com.brutalfighters.server.data.buffs.Buff_RedBats;
import com.brutalfighters.server.data.maps.Base;
import com.brutalfighters.server.data.projectiles.types.BigBats;
import com.brutalfighters.server.data.projectiles.types.PurpleBat;
import com.brutalfighters.server.data.projectiles.types.PurpleLaser;
import com.brutalfighters.server.data.projectiles.types.SmallBats;
import com.brutalfighters.server.matches.GameMatchManager;
import com.brutalfighters.server.util.MathUtil;
import com.brutalfighters.server.util.Vec2;
import com.esotericsoftware.kryonet.Connection;

/**
 * 战士Dusk
 *
 */
public class Dusk extends Fighter {
	
	public Dusk(Connection connection, int team, Base base, String m_id) {
		/**
		 * 构造函数
		 * @param connection		// 连接
		 * @param team				// 队伍
		 * @param base				// 位置和朝向
		 * @param m_id				// 比赛id
		 * @param name				// 名称
		 * @param maxhp				// 最大hp
		 * @param maxmana			// 最大魔法值
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
				m_id, 		// 比赛id
				"Dusk", 	// 名称
				900, 		// 最大hp
				1000, 		// 最大mp
				new Vec2(90,100), // 最大尺寸
				10,		// 行走速度
				21, 	// 跑步速度
				52, 	// 跳跃高度
				500, 	// 自动攻击cd
				new Vec2(200,50), 	// 自动攻击范围
				68, 	// 自动攻击伤害
				9,		// 法力回复
				new int[] {300,300,300,500}, 	// 技能消耗法力值
				new int[] {740,820,580,500}		// 技能cd最大值
		);
	}
	
	// SKILLS（每人4个技能）
	
	// ##### Skill 1 #####
	@Override
	public void updateSkill1() {
		defaultUpdate();
		applyFlip();
	}
	
	@Override
	public void skill1() {
		
		updateSkill1();
		
		if(getPlayer().getSkillCD()[0] > 0) {
				if(getPlayer().getSkillCD()[0] == getMaxSkillCD()[0] - GameServer.getDelay() * 12) {
					float xstart = getPlayer().getPos().getX() + convertSpeed(getPlayer().getSize().getX()*2);
					GameMatchManager.getCurrentProjectiles().add(new BigBats(this, getPlayer().getFlip(), new Vec2(xstart, getPlayer().getPos().getY()), 17, 0, new Buff[] {new Buff_RedBats()})); 
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
			//getRunningSpeed().setX(getWalkingSpeed().getX());
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
			if(getPlayer().getSkillCD()[1] == getMaxSkillCD()[1] - GameServer.getDelay() * 13) {
				float xstart = getPlayer().getPos().getX() + convertSpeed(getPlayer().getSize().getX());
				GameMatchManager.getCurrentProjectiles().add(new SmallBats(this, getPlayer().getFlip(), new Vec2(xstart, getPlayer().getPos().getY()), 45, 100, new Buff[0])); 
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
	@Override
	public void skill3() {
		updateSkill3();
		
		if(getPlayer().getSkillCD()[2] > 0) {
			if(getPlayer().getSkillCD()[2] == getMaxSkillCD()[2] - GameServer.getDelay() * 10) {
				float xstart = getPlayer().getPos().getX() + convertSpeed(getPlayer().getSize().getX()*2);
				GameMatchManager.getCurrentProjectiles().add(new PurpleLaser(this, getPlayer().getFlip(), new Vec2(xstart, getPlayer().getPos().getY()+8), 30, 110, new Buff[0])); 
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
	}
	
	// ##### Skill 4 #####
	@Override
	public void skill4() {
		updateSkill4();
		
		if(getPlayer().getSkillCD()[3] > 0) {
			float xstart = getPlayer().getPos().getX() + convertSpeed(MathUtil.nextInt(20,80));
			GameMatchManager.getCurrentProjectiles().add(new PurpleBat(this, getPlayer().getFlip(), new Vec2(xstart, getPlayer().getPos().getY()+MathUtil.nextInt(-40, +30)), 30, 25, new Buff[0])); 
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
