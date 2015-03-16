package trl.entity.enemy;

import trl.main.Game;
import trl.map.Map;

public class Wolf extends Enemy {
	public Wolf(Map map) {
		super(map);
        init();
    }
    
    public void init() {
    	maxHP = 20;
    	atk = 5;
    	image = Game.getImageManager().wolf;
        hp = maxHP;
        xpReward = 10;
        level = 10;
    }
}
