package trl.map;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import trl.entity.Entity;
import trl.map.feature.DoorClosed;
import trl.map.feature.DoorOpen;
import trl.map.feature.Feature;
import trl.map.feature.Floor;
import trl.map.feature.Void;
import trl.map.feature.Wall;

public class Node {

    private int x, y;
    private Node parentNode;
    private int gScore, hScore, fScore;
    private Map map;
    private List<Entity> entities;
    private boolean isWall = false, isFloor = false, isVoid = true, isDoorOpen = false, isDoorClosed = false;
    private BufferedImage image;
    private boolean seenByPlayer;
    private Feature feature;
    public static Feature closedDoor = new DoorClosed();
    public static Feature openDoor = new DoorOpen();
    public static Feature voidNode = new Void();
    public static Feature wall = new Wall();
    public static Feature floor = new Floor();
    public Node(int x, int y, Map map) {
        this.x = x;
        this.y = y;
        init();
        this.map = map;
    }
    
    public Node() {
        
    }
    
    public void init() {
    	seenByPlayer = false;
    	isFloor = false;
    	isWall = false;
        entities = new ArrayList<Entity>();
    }
    
    public boolean isWall() {
    	if (feature instanceof trl.map.feature.Wall) {
    		return true;
    	}
    	return false;
    }

    public boolean isFloor() {  
    	if (feature instanceof trl.map.feature.Floor) {
    		return true;
    	}
    	return false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
    	return y;
    }
    
    public List<Entity> getEntities() {
        if (entities.size() > 0) {
            return entities;
        }
        else {       
            return null;
        }
    }
    
    public boolean nodeContains(Entity entityToFind) {
        if (entities.size() > 0) {
            for (Entity entity : entities) {
                if (entity.equals(entityToFind)) {
                    return true;
                }
            }
        }
        return false;
    }
   
    public void addEntity(Entity entity) {
        if (entity instanceof trl.entity.actor.Actor && entities.size() > 0) {
            entities.add(entities.size() - 1, entity);
        }
        else {
            entities.add(0, entity);
        }
    }
    
    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }
      
    public void setParent(Node node) {
        this.parentNode = node;
    }
    
    public Node getParent() {
        return this.parentNode;
    }
    
    public void setGScore() {
        //Check for diagonal movement. G score = 14.
        if (x != getParent().getX() && y != getParent().getY()) {
            gScore = getParent().getGScore() + 14;
        }
        //Orthogonal movement cost = 10
        else {
            gScore = getParent().getGScore() + 10;
        }
        //Check map node at this node's coords for enemy occupation
        if (map.getNode(x, y) != null) {
	        if (map.getNode(x, y).checkEntityByID((byte) 0)) {
	        	gScore += 70;
	        }
        }
    }
    
    public void setGScore(int g) {
        gScore = g;
    }
    
    public void setHScore(int h) {
        gScore = h;
    }
    
    public void setFScore(int f) {
        fScore = f;
    }
    
    public int getGScore() {
        //Check for diagonal movement. G score = 14.
        if (this.x != this.parentNode.x && this.y != this.parentNode.y) {
            return this.parentNode.gScore + 14;
        }
        else {
            return this.parentNode.gScore + 10;
        } 
    }
    
    public void setHScore(Node endNode) {
        this.hScore = 10 * (Math.abs(x - endNode.getX()) + Math.abs(y - endNode.getY())); 
    }
    
    public int getHScore() {
        return hScore;
    }
    
    public void setFScore() {
        this.fScore = this.gScore + this.hScore;
    }
    
    public int getFScore() {
        return this.fScore;
    }
      
    public Map getMap() {
        return this.map;
    }
    
    public boolean adjacent(Node node) {
        if (this.x == node.x || this.x == node.x - 1 || this.x == node.x + 1) {
            if (this.y == node.y || this.y == node.y - 1 || this.y == node.y + 1) {
                return true;
            }
        }
        return false;
    }
    
    public boolean checkEntityByID(byte entityID) {
    	if (entities == null) {
//    		System.out.println("entities NULL");
    	}
        if (entities != null && entities.size() > 0) {
            for (Entity entity : entities) {
                switch (entityID) {
	                case 0: {
		            	if (entity instanceof trl.entity.enemy.Enemy) {
		                    return true;
		                }
		            	break;
	                }
	                case 1: {
		            	if (entity instanceof trl.entity.item.Potion) {
		                    return true;
		                }
		            	break;
	                }
	                case 2: {
		            	if (entity instanceof trl.entity.item.Hammer) {
		                    return true;
		                }
		            	break;
	                }
	                case 3: {
		            	if (entity instanceof trl.entity.actor.Actor) {
		                    return true;
		                }
		            	break;
	                }
	                case 4: {
		            	if (entity instanceof trl.entity.Entity) {
		                    return true;
		                }
		            	break;
	                }
	                case 5: {
	                	if (entity instanceof trl.entity.item.Key) {
	                		return true;
	                	}
	                	break;
	                }
	                case 6: {
	                	if (entity instanceof trl.entity.item.Lock) {
	                		return true;
	                	}
	                	break;
	                }
	                case 7: {
	                	if (entity instanceof trl.entity.item.Goal) {
	                		return true;
	                	}
	                	break;
	                }
                }
            }
        }
        return false;
    }
    
//    public boolean checkEntityByName(String entityName) {
//    	if (entities == null) {
////    		System.out.println("entities NULL");
//    	}
//        if (entities != null && entities.size() > 0) {
//            for (Entity entity : entities) {
//                switch (entityName) {
//	                case "enemy": {
//		            	if (entity instanceof trl.entity.enemy.Enemy) {
//		                    return true;
//		                }
//		            	break;
//	                }
//	                case "potion": {
//		            	if (entity instanceof trl.entity.item.Potion) {
//		                    return true;
//		                }
//		            	break;
//	                }
//	                case "hammer": {
//		            	if (entity instanceof trl.entity.item.Hammer) {
//		                    return true;
//		                }
//		            	break;
//	                }
//	                case "actor": {
//		            	if (entity instanceof trl.entity.actor.Actor) {
//		                    return true;
//		                }
//		            	break;
//	                }
//	                case "entity": {
//		            	if (entity instanceof trl.entity.Entity) {
//		                    return true;
//		                }
//		            	break;
//	                }	                
//                }
//            }
//        }
//        return false;
//    }
    
    public void removeEntityByID(byte entityID) {
        Iterator<Entity> entity = entities.iterator();
        while (entity.hasNext()) {
        	switch (entityID) {
            case 0: {
            	if (entity.next() instanceof trl.entity.enemy.Enemy) {
                    entity.remove();
                }
            	break;
            }
            case 1: {
            	if (entity.next() instanceof trl.entity.item.Potion) {
                    entity.remove();
                }
            	break;
            }
            case 2: {
            	if (entity.next() instanceof trl.entity.item.Hammer) {
                    entity.remove();
                }
            	break;
            }
            case 3: {
            	if (entity.next() instanceof trl.entity.actor.Actor) {
                    entity.remove();
                }
            	break;
            }
            case 4: {
            	if (entity.next() instanceof trl.entity.Entity) {
                    entity.remove();
                }
            	break;
            }
            case 5: {
            	if (entity.next() instanceof trl.entity.item.Key) {
            		entity.remove();
            	}
            	break;
            }
            case 6: {
            	if (entity.next() instanceof trl.entity.item.Lock) {
//            		System.out.println("Removing lock.");
            		entity.remove();
            	}
            	break;
            }
        }
        }	
    }
    
    public boolean isGoal() {
    	if (feature instanceof trl.map.feature.Goal) {
    		return true;
    	}
    	return false;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
    
    public BufferedImage getImage() {
        return this.image;
    }
    
    public void setSeenByPlayer(boolean seenByPlayer) {
    	this.seenByPlayer = seenByPlayer;
    }
    
    public boolean seenByPlayer() {
    	return this.seenByPlayer;
    }
    
    public Feature getFeature() {
    	return feature;
    }
    
    public void setFeature(Feature feature) {
    	this.feature = feature;
    }
    
    public void makeWall() {
    	feature = wall;
    }
    
    public void makeFloor() {
    	feature = floor;
    }
    
    public void makeOpenDoor() {
    	feature = openDoor;
    }
    
    public void makeClosedDoor() {
    	feature = closedDoor;
    }
    
    public void makeVoid() {
    	feature = new Void();
    }
    
    public boolean isVoid() {
    	if (feature instanceof trl.map.feature.Void) {
    		return true;
    	}
    	return false;
    }
    
    public boolean isClosedDoor() {
    	if (feature instanceof trl.map.feature.DoorClosed) {
    		return true;
    	}
    	return false;
    }
    
    public boolean isOpenDoor() {
    	if (feature instanceof trl.map.feature.DoorOpen) {
    		return true;
    	}
    	return false;
    }
    
    public boolean isStairDown() {
    	if (feature instanceof trl.map.feature.StairDown) {
    		return true;
    	}
    	return false;
    }
    
    public boolean hasEnemy() {
    	if (entities != null && entities.size() > 0) {
    		for (Entity entity : entities) {
    			if (entity instanceof trl.entity.enemy.Enemy) {
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
    @Override
    public boolean equals(Object other) {
        Node node = (Node)other;
        return (this.x == node.x && this.y == node.y);
    }
    @Override
    public int hashCode() {
        return ((this.x + 3) * 31) ^ ((this.y + 7) * 43);
    }   
}

