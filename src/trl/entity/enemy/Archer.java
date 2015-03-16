package trl.entity.enemy;

import trl.main.Game;
import trl.map.Map;

public class Archer extends Enemy {
	public Archer(Map map) {
		super(map);
        init();
    }
    
    public void init() {
    	maxHP = 5;
    	atk = 5;
    	image = Game.getImageManager().archer;
        hp = maxHP;
        xpReward = 1;
        level = 1;
    }
}
