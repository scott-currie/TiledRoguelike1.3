package trl.entity.enemy;

import trl.main.Game;
import trl.map.Map;

public class Rat extends Enemy {
    public Rat(Map map) {
        super(map);
        init();
    }
    
    public void init() {
    	maxHP = 8;
    	atk = 5;
    	image = Game.getImageManager().rat;
        hp = maxHP;
        level = 3;
        xpReward = 3;
    }
}
