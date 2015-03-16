package trl.entity;

import java.awt.Point;
import java.awt.image.BufferedImage;

import trl.main.Game;
import trl.map.Map;
import trl.map.Node;
import trl.map.Room;

public class Entity {
    protected Node loc;
    protected Map map;   
    protected BufferedImage image;
    protected boolean seenByPlayer;
    protected boolean visibleToPlayer;
    
    public Entity(Map map) {
        this.map = map;
    }
    
    public void setLoc(Node node) {
        this.loc = node;
    }
    
    public Node getLoc() {
        return this.loc;
    }
    
    public BufferedImage getImage() {
        return this.image;
    }
    
    public int getX() {
        return this.loc.getX();
    }
    
    public int getY() {
        return loc.getY();
    }
    
    
    /*Edited this at 21:32 on 1/1/14. This could be part of the issue with enemies 
     * becoming aware of the player when it seems like they should not.
     */
    public boolean inDisplayedNodes() {
    	int x = Map.displayedNodesMinX;
    	int y = Map.displayedNodesMinY;
    	if (getX() >= x && getX() < x + Game.W_COLS && getY() >= y && getY() < y + Game.W_ROWS) {
    		return true;
    	}
    	return false;
    }
    
    public boolean seenByPlayer() {
    	return seenByPlayer;
    }
    
    public void setSeenByPlayer(boolean seen) {
    	this.seenByPlayer = seen;
    }

	public Room getOccupiedRoom(Node node) {
		Room[][] rooms = map.getRooms();
		Point position = new Point(node.getX(), node.getY());
		for (int x = 0; x < rooms.length; x++) {
			for (int y = 0; y < rooms[0].length; y++) {
				if (rooms[x][y].getBoundary().contains(position)) {
					return rooms[x][y];
				}
			}
		}
		return null;
	}
	
	public boolean getSeenByPlayer() {
		return seenByPlayer;
	}
	
	public boolean getVisibleToPlayer() {
		return visibleToPlayer;
	}
	
	public void setVisibleToPlayer(boolean visibleToPlayer) {
		this.visibleToPlayer = visibleToPlayer;
	}
}
