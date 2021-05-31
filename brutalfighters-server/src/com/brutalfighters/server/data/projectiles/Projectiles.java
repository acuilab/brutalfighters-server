package com.brutalfighters.server.data.projectiles;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 抛射物列表
 * 保存了一个Projectile集合，再update方法中对集合中的Projectile进行遍历，并将迭代器iterator作为参数传到update方法中，在update方法中可以对Projectile进行删除
 *	Projectiles/Projectile/ProjectileData
 */
public class Projectiles {
	private ArrayList<Projectile> projectiles;
	
	public Projectiles() {
		projectiles = new ArrayList<Projectile>();
	}
	
	public void add(Projectile p) {
		projectiles.add(p);
		p.initialize();
	}
	
	public ProjectileData getProjectile(int i) {
		return projectiles.get(i).getProjectile();
	}
	public Projectile get(int i) {
		return projectiles.get(i);
	}
	public ArrayList<Projectile> getAll() {
		return projectiles;
	}
	
	public void update() {
		for (Iterator<Projectile> iterator = projectiles.iterator(); iterator.hasNext();) {
			Projectile projectile = iterator.next();
			if(projectile.getProjectile().isExplode()) {
				iterator.remove();
			} else {
				projectile.update(iterator);
			}
		}
	}

	public void remove(int i) {
		projectiles.remove(i);
	}
}
