package trl.entity.enemy;

import trl.main.Game;
import trl.map.Map;

public class Bat extends Enemy {
    public Bat(Map map) {
        super(map);
        init();
    }
    
    public void init() {
    	maxHP = 6;
    	atk = 5;
    	image = Game.getImageManager().bat;
        hp = maxHP;
        level = 1;
        xpReward = 1;
    }
}
