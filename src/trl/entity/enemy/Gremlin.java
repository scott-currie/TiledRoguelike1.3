package trl.entity.enemy;

import trl.main.Game;
import trl.map.Map;

public class Gremlin extends Enemy {
	public Gremlin(Map map) {
		super(map);
        init();
    }
    
    public void init() {
    	maxHP = 24;
    	atk = 5;
    	image = Game.getImageManager().gremlin;
        hp = maxHP;
        xpReward = 12;
        level = 12;
    }
}
