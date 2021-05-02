package com.brutalfighters.server.data.players.fighters;

import com.brutalfighters.server.base.GameServer;
import com.brutalfighters.server.data.buffs.Buff;
import com.brutalfighters.server.data.buffs.Buff_Burn;
import com.brutalfighters.server.data.buffs.Buff_Heal;
import com.brutalfighters.server.data.buffs.Buff_Slow;
import com.brutalfighters.server.data.maps.Base;
import com.brutalfighters.server.data.projectiles.types.BloodBall;
import com.brutalfighters.server.data.projectiles.types.Pheonix;
import com.brutalfighters.server.data.projectiles.types.SkullFire;
import com.brutalfighters.server.matches.GameMatchManager;
import com.brutalfighters.server.util.AOE;
import com.brutalfighters.server.util.CollisionDetection;
import com.brutalfighters.server.util.MathUtil;
import com.brutalfighters.server.util.Vec2;
import com.esotericsoftware.kryonet.Connection;

/**
 * 战士Blaze
 *
 */
public class Blaze extends Fighter {
	
	public Blaze(Connection connection, int team, Base base, String m_id) {
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
				"Blaze", 	// 名称
				1000, 		// 最大hp
				1000, 		// 最大mp
				new Vec2(90,100), // 最大尺寸
				10,		// 行走速度
				18, 	// 跑步速度
				52, 	// 跳跃高度
				500, 	// 自动攻击cd
				new Vec2(200,50), 	// 自动攻击范围
				75, 	// 自动攻击伤害
				9,		// 法力回复
				new int[] {400,200,950,500}, 	// 技能消耗法力值
				new int[] {980,580,820,820}		// 技能cd最大值
		);
	}
	
	// SKILLS（每人4个技能）
	
	// ##### SKILL 1 #####
	int s1_HEIGHT = 40, s1_WIDTH = 500, dmg = 40; // DEALS AROUND 4 times
	
	@Override
	public void skill1() {
		updateSkill1();
		
		if(getPlayer().getSkillCD()[0] > 0) {
			if(getPlayer().getSkillCD()[0] <= getMaxSkillCD()[0] - GameServer.getDelay() * 12 && getPlayer().getSkillCD()[0] >= getMaxSkillCD()[0] - GameServer.getDelay() * 15) {
				AOE.dealAOE_enemy(getPlayer().getTeam(), CollisionDetection.getBounds(getPlayer().getFlip(), getPlayer().getPos().getX() + convertSpeed(20), getPlayer().getPos().getY(), s1_WIDTH, s1_HEIGHT), -dmg, new Buff[] {new Buff_Slow(5)});
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
	
	// ##### SKILL 2 #####
	@Override
	public void skill2() {
		updateSkill2();
		
		if(getPlayer().getSkillCD()[1] > 0) {
			if(getPlayer().getSkillCD()[1] == getMaxSkillCD()[1] - GameServer.getDelay() * 7) {
				float xstart = getPlayer().getPos().getX() + convertSpeed(50);
				GameMatchManager.getCurrentProjectiles().add(new BloodBall(this, getPlayer().getFlip(), new Vec2(xstart, getPlayer().getPos().getY()+5), 20, 155, new Buff[0])); 
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
	private final int self_hp = 550;
	
	@Override
	public void skill3() {
		updateSkill3();
		
		if(getPlayer().getSkillCD()[2] > 0) {
			if(getPlayer().getSkillCD()[2] == getMaxSkillCD()[2] - GameServer.getDelay() * 12) {
				GameMatchManager.getCurrentProjectiles().add(new Pheonix(this, new Vec2(getPlayer().getPos().getX(), getPlayer().getPos().getY()+getPlayer().getSize().getY()*2))); 
				applyHP(self_hp);
				AOE.dealAOE_players(getPlayer().getTeam(), CollisionDetection.getBounds("both", getPlayer().getPos().getX(), getPlayer().getPos().getY(), 400, 400), 0, new Buff[] {new Buff_Heal(10)}); //$NON-NLS-1$
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
	
	
	// ##### SKILL 4 #####
	@Override
	public void startSkill4() {
		if(applySkillMana(3)) {
			getRunningSpeed().setX(getWalkingSpeed().getX());
		} else {
			endSkill4();
		}
	}
	
	@Override
	public void updateSkill4() {
		applyJump();
		applyGravity();
		applyWalking();
	}
	
	@Override
	public void skill4() {
		updateSkill4();
		
		if(getPlayer().getSkillCD()[3] > 0) {
			if(getPlayer().getSkillCD()[3] / GameServer.getDelay() % 2 == 0) {
				float xstart = getPlayer().getPos().getX() + convertSpeed(MathUtil.nextInt(20,80));
				GameMatchManager.getCurrentProjectiles().add(new SkullFire(this, getPlayer().getFlip(), new Vec2(xstart, getPlayer().getPos().getY()+MathUtil.nextInt(-50, 10)), 30, 0, new Buff[] {new Buff_Burn(2)}));
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
		resetSpeeds();
	}
}
