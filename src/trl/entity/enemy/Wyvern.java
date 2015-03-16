package trl.entity.enemy;

import trl.main.Game;
import trl.map.Map;

public class Wyvern extends Enemy {
	public Wyvern(Map map) {
		super(map);
        init();
    }
    
    public void init() {
    	maxHP = 22;
    	atk = 5;
    	image = Game.getImageManager().wyvern;
        hp = maxHP;
        xpReward = 11;
        level = 11;
    }
}
