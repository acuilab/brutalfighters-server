package com.brutalfighters.server.data.maps;

import com.brutalfighters.server.data.flags.Flag;
import com.brutalfighters.server.data.flags.FlagData;
import com.brutalfighters.server.matches.GameMatch;
import com.brutalfighters.server.tiled.TiledMap;
import com.brutalfighters.server.util.Vec2;

/**
 * CTF地图
 *
 */
public class CTFMap extends GameMap {

	private Base[] bases;	// 重生点（每队一个）
	private Flag[] flags;	// 旗帜（每队一个）
	
	public CTFMap(TiledMap map) {
		super(map);

		this.bases = new Base[GameMatch.getTeamLength()];
		this.flags = new Flag[GameMatch.getTeamLength()];
		
		// 设置重生点
		this.setBase(GameMatch.getTEAM1(), new Base(new Vec2(leftBoundary+100, 384), "right"));
		this.setBase(GameMatch.getTEAM2(), new Base(new Vec2(rightBoundary-100, 384), "left"));

		// 设置旗帜
		this.setFlag(GameMatch.getTEAM1(), new Vec2(leftBoundary+400, FlagData.getSize().getY() + 291), "right");
		this.setFlag(GameMatch.getTEAM2(), new Vec2(rightBoundary-400, FlagData.getSize().getY() + 291), "left");
	}
	
	// It's not the main constructor because the default base and flags values
	// refer to the boundaries values, and we can't include them in the parameters, try for yourself and see.
	/**
	 * 它不是主要的构造函数，因为默认的base和flags值引用边界值，并且我们不能将它们包含在参数中，请自己尝试看看。
	 * @param map
	 * @param base1
	 * @param base2
	 * @param flag1
	 * @param flag2
	 */
	public CTFMap(TiledMap map, Base base1, Base base2, Flag flag1, Flag flag2) {
		this(map);
		
		this.setBase(0, base1);
		this.setBase(1, base2);
		this.setFlag(0, flag1); 
		this.setFlag(1, flag2);
	}
	
	// BASES
	public Base getBase(int index) {
		return bases[index];
	}
	public void setBase(int index, Vec2 pos, String flip) {
		bases[index] = new Base(pos, flip);
	}
	public void setBase(int index, Base base) {
		bases[index] = base;
	}
	
	
	// FLAGS
	public Flag getFlag(int index) {
		Flag flag = flags[index];
		return new Flag(new Vec2(flag.getFlag().getPos().getX(), flag.getFlag().getPos().getY()), flag.getFlag().getFlip());
	}
	public void setFlag(int index, Flag flag) {
		flags[index] = new Flag(new Vec2(flag.getFlag().getPos().getX(), flag.getFlag().getPos().getY()), flag.getFlag().getFlip());
	}
	public void setFlag(int index, Vec2 pos, String flip) {
		flags[index] = new Flag(pos, flip);
	}
}
