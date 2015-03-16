package trl.graphics;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import trl.main.Game;


public class ImageManager {
    public BufferedImage barbarian, stoneTile, stoneWall, boundaryWall, demon, corpse, bang,
    	frogKnight, archer, miss, potion, hammer, thief, eye, wizard, swords, fire, rat, bat, 
    	spider, snake, voidNode, openDoor, closedDoor, wolf, ant, stairDown, wyvern, wasp,
    	imp, gelatinousCube, ogre, panther, worm, scorpion, goblin, gremlin, gargoyle, zombie,
    	goal, stairDownShadow, goalShadow, key, lock, lockOpen, keyShadow, lockShadow,
    	lockOpenShadow, ranger, arrows;
    
    public BufferedImage closedDoorShadow, openDoorShadow, stoneTile1Shadow, stoneWallShadow,potionShadow,hammerShadow;
    
    public ImageManager(SpriteSheet ss) {
    	//Row 1
        barbarian = ss.crop(0, 0, Game.TILE_SIZE, Game.TILE_SIZE);
        thief = ss.crop(1, 0, Game.TILE_SIZE, Game.TILE_SIZE);
        wizard = ss.crop(2, 0, Game.TILE_SIZE, Game.TILE_SIZE);
        ranger = ss.crop(3, 0, Game.TILE_SIZE, Game.TILE_SIZE);
        //Row 2
        rat = ss.crop(0, 1, Game.TILE_SIZE, Game.TILE_SIZE);
        spider = ss.crop(1, 1, Game.TILE_SIZE, Game.TILE_SIZE);
        bat = ss.crop(2, 1, Game.TILE_SIZE, Game.TILE_SIZE);
        snake = ss.crop(3, 1, Game.TILE_SIZE, Game.TILE_SIZE);
        wolf = ss.crop(4, 1, Game.TILE_SIZE, Game.TILE_SIZE);
        wyvern = ss.crop(5, 1, Game.TILE_SIZE, Game.TILE_SIZE);        
        //Row 3
        ant = ss.crop(0, 2, Game.TILE_SIZE, Game.TILE_SIZE);
        wasp = ss.crop(1, 2, Game.TILE_SIZE, Game.TILE_SIZE);
        imp = ss.crop(2, 2, Game.TILE_SIZE, Game.TILE_SIZE);
        frogKnight = ss.crop(3, 2, Game.TILE_SIZE, Game.TILE_SIZE);
        archer = ss.crop(4, 2, Game.TILE_SIZE, Game.TILE_SIZE);
        gelatinousCube = ss.crop(5, 2, Game.TILE_SIZE, Game.TILE_SIZE);
        //Row 4
        worm = ss.crop(0, 3, Game.TILE_SIZE, Game.TILE_SIZE);
        scorpion = ss.crop(1, 3, Game.TILE_SIZE, Game.TILE_SIZE);
        goblin = ss.crop(2, 3, Game.TILE_SIZE, Game.TILE_SIZE);
        gremlin = ss.crop(3, 3, Game.TILE_SIZE, Game.TILE_SIZE);
        gargoyle = ss.crop(4, 3, Game.TILE_SIZE, Game.TILE_SIZE);
        zombie = ss.crop(5, 3, Game.TILE_SIZE, Game.TILE_SIZE);
        //Row 5
        ogre = ss.crop(0, 4, Game.TILE_SIZE, Game.TILE_SIZE);
        panther = ss.crop(1, 4, Game.TILE_SIZE, Game.TILE_SIZE);
        //Row 6
        
        //Row 7        
        stoneTile = ss.crop(0, 6, Game.TILE_SIZE, Game.TILE_SIZE);
        stoneWall = ss.crop(1, 6, Game.TILE_SIZE, Game.TILE_SIZE);
        closedDoor = ss.crop(2, 6, Game.TILE_SIZE, Game.TILE_SIZE);
        openDoor = ss.crop(3, 6, Game.TILE_SIZE, Game.TILE_SIZE);
        stairDown = ss.crop(4, 6, Game.TILE_SIZE, Game.TILE_SIZE);
        goal = ss.crop(5, 6, Game.TILE_SIZE, Game.TILE_SIZE);
        //Row 8
        potion = ss.crop(0, 7, Game.TILE_SIZE, Game.TILE_SIZE);
        hammer = ss.crop(1, 7, Game.TILE_SIZE, Game.TILE_SIZE);
        key = ss.crop(2, 7, Game.TILE_SIZE, Game.TILE_SIZE);
        lock = ss.crop(3, 7, Game.TILE_SIZE, Game.TILE_SIZE);
        lockOpen = ss.crop(4,  7, Game.TILE_SIZE, Game.TILE_SIZE);
        //Row 9
        bang = ss.crop(0, 8, Game.TILE_SIZE, Game.TILE_SIZE);      
        corpse = ss.crop(1, 8,Game.TILE_SIZE, Game.TILE_SIZE );
        swords = ss.crop(2, 8, Game.TILE_SIZE, Game.TILE_SIZE);
        fire = ss.crop(3, 8, Game.TILE_SIZE, Game.TILE_SIZE);
        arrows = ss.crop(4, 8, Game.TILE_SIZE, Game.TILE_SIZE);

        //Shadow transforms
        stoneWallShadow = transformAlpha(stoneWall);
        stoneTile1Shadow = transformAlpha(stoneTile);
        openDoorShadow = transformAlpha(openDoor);
        closedDoorShadow = transformAlpha(closedDoor);
        potionShadow = transformAlpha(potion);
        hammerShadow = transformAlpha(hammer);
        stairDownShadow = transformAlpha(stairDown);
        goalShadow = transformAlpha(goal);
        keyShadow = transformAlpha(key);
        lockShadow = transformAlpha(lock);
    }
 
    public BufferedImage transformAlpha(BufferedImage image) {
    	BufferedImage destImage = null;
    	float[] transparency = {.7f};
    	Kernel kernel = new Kernel(1, 1, transparency);
    	ConvolveOp co = new ConvolveOp(kernel);
    	destImage = co.filter(image, null);
    	return destImage;
    }
}
