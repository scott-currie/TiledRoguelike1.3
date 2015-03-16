package trl.entity.item;

import trl.main.Game;
import trl.map.Map;

public class Goal extends Item {
    public Goal(Map map) {
        super(map);
        init();
    }
	
    public void init() {
        this.image = Game.getImageManager().goal;
        this.loc = map.placeEntity(this, map.getRandomNodeInRoom());
    }    
}
