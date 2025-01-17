package com.brutalfighters.server.data.players.fighters;

import com.brutalfighters.server.data.maps.Base;
import com.esotericsoftware.kryonet.Connection;

/**
 * 战士工厂
 *
 */
public enum FighterFactory {
	Blaze {
		@Override
		public Fighter getNew(Connection connection, int team, Base base, String m_id) {
			return new Blaze(connection, team, base, m_id);
		}
		
	},
	
	Dusk {
		@Override
		public Fighter getNew(Connection connection, int team, Base base, String m_id) {
			return new Dusk(connection, team, base, m_id);
		}
		
	},
	
	Chip {
		@Override
		public Fighter getNew(Connection connection, int team, Base base, String m_id) {
			return new Chip(connection, team, base, m_id);
		}
		
	},
	
	Surge {
		@Override
		public Fighter getNew(Connection connection, int team, Base base, String m_id) {
			return new Surge(connection, team, base, m_id);
		}
		
	},
	
	Lust {
		@Override
		public Fighter getNew(Connection connection, int team, Base base, String m_id) {
			return new Lust(connection, team, base, m_id);
		}
	};
	
	public abstract Fighter getNew(Connection connection, int team, Base base, String m_id);
	
	public static boolean contains(String fighter) {
	    for (FighterFactory c : FighterFactory.values()) {
	        if (c.name().equals(fighter)) {
	            return true;
	        }
	    }

	    return false;
	}
}
