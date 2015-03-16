package trl.entity.enemy;

import trl.main.Game;
import trl.map.Map;

public class Scorpion extends Enemy {
	public Scorpion(Map map) {
		super(map);
        init();
    }
    
    public void init() {
    	maxHP = 12;
    	atk = 5;
    	image = Game.getImageManager().scorpion;
        hp = maxHP;
        xpReward = 6;
        level = 6;
    }
}
