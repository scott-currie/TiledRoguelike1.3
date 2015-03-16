package trl.entity.enemy;

import trl.main.Game;
import trl.map.Map;

public class Ant extends Enemy {
	public Ant(Map map) {
		super(map);
        init();
    }
    
    public void init() {
    	maxHP = 8;
    	atk = 5;
    	image = Game.getImageManager().ant;
        hp = maxHP;
        xpReward = 4;
        level = 4;
    }
}
