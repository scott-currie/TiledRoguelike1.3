package trl.entity.item;

import trl.main.Game;
import trl.map.Map;

public class Hammer extends Item {
    public Hammer(Map map) {
        super(map);
        init();
    }
	
    public void init() {
        this.image = Game.getImageManager().hammer;
        this.loc = map.placeEntity(this, map.getRandomNodeInRoom());
    }    
}
