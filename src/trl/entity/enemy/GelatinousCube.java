package trl.entity.enemy;

import trl.main.Game;
import trl.map.Map;

public class GelatinousCube extends Enemy {
	public GelatinousCube(Map map) {
		super(map);
        init();
    }
    
    public void init() {
    	maxHP = 14;
    	atk = 5;
    	image = Game.getImageManager().gelatinousCube;
        hp = maxHP;
        xpReward = 7;
        level = 7;
    }
}
