package trl.entity.enemy;

import trl.main.Game;
import trl.map.Map;

public class FrogKnight extends Enemy{
    public FrogKnight(Map map) {
        super(map);
        init();
    }
    
    public void init() {
    	maxHP = 10;
    	atk = 8;
    	image = Game.getImageManager().frogKnight;        
        hp = maxHP;
        level = 1;
        xpReward = 1;
    }
}
