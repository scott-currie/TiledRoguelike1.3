package trl.map;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import trl.map.feature.Feature;

public class Room {
	private int width, height, row, column, x, y;
	private Map map;
	private boolean connected;
	private List<Room> connectedTo;
	private Rectangle boundary;
	
	public Room (Map map, int row, int column) {
		this.row = row;
		this.column = column;
		this.map = map;
		this.connected = false;
		init();
	}
	
	public void init() {
		Random r = new Random();
		this.width = (int)(r.nextDouble() * Map.MAX_ROOM_WIDTH);
		this.height = (int)(r.nextDouble() * Map.MAX_ROOM_HEIGHT);
		if (width < Map.MIN_ROOM_WIDTH) {
			width = Map.MIN_ROOM_WIDTH;
		}
		if (height < Map.MIN_ROOM_HEIGHT) {
			height = Map.MIN_ROOM_HEIGHT;
		}
		x = (column * Map.MAX_ROOM_WIDTH) + (Map.MAX_ROOM_WIDTH - width) / 2;
		y = (row * Map.MAX_ROOM_HEIGHT) + (Map.MAX_ROOM_HEIGHT - height) / 2;
		boundary = new Rectangle(x, y, width, height);
		connectedTo = new ArrayList<Room>();
	}

	public void connect(Room room) {

		String relationship = getRelationship(room);
//		System.out.println("Room relationship = " + relationship);
		List<Node> connection = new ArrayList<Node>();
		if (!relationship.equals("") && !this.connectedTo(room)) {
//			System.out.println("Connecting rooms.");
			/*startX, startY should be the node in the outer wall of the starting room. We should
			 * handle this node (set feature to door or floor) explicitly to prevent a line drawn through
			 * from this point cutting through more than one wall node. We will start pathfinding from
			 * the next adjacent node in the direction we want to go.
			 */
			
			int startX = 0, startY = 0, endX = 0, endY = 0;			
//			switch(re/ationship) {
				if (relationship.equals("above")) {
					startY = this.y;
					endY = room.y + room.height - 1;
					startX = this.getCenterX();
					endX = room.getCenterX();
					map.getNode(startX, startY).setFeature(rollDoor());
					map.getNode(endX, endY).setFeature(rollDoor());
					startY -= 1;
					endY += 1;
				}
				if (relationship.equals("below")) {
					startY = this.y + this.height - 1;
					endY = room.y;
					startX = this.getCenterX();
					endX = room.getCenterX();
					map.getNode(startX, startY).setFeature(rollDoor());
					map.getNode(endX, endY).setFeature(rollDoor());
					startY += 1;
					endY -= 1;
				}
				if (relationship.equals("left")) {
					startX = this.x + this.width - 1;
					endX = room.x;
					startY = this.getCenterY();
					endY = room.getCenterY();
					map.getNode(startX, startY).setFeature(rollDoor());
					map.getNode(endX, endY).setFeature(rollDoor());
					startX += 1;
					endX -= 1;
				}
				if (relationship.equals("right")) {
					startX = this.x;
					endX = room.x + room.width - 1;
					startY = this.getCenterY();
					endY = room.getCenterY();
					map.getNode(startX, startY).setFeature(rollDoor());
					map.getNode(endX, endY).setFeature(rollDoor());
					startX -= 1;
					endX += 1;
				}
//			}
			Node start = new Node(startX, startY, map);
			map.createNode(start);
			Node end = new Node(endX, endY, map);
			map.createNode(end);
			
		//If the connection between nodes is longer than a single node, find connection	
		if (!start.equals(end)) {
//			System.out.println("Connecting rooms with path > 1");
			start = new Node(startX, startY, map);
			map.createNode(start);
			start.makeFloor();
			end = new Node(endX, endY, map);
			map.createNode(end);
			end.makeFloor();
			connection = map.findRoomConnection(start, end);
		}
		//Connection start and end nodes are the same.
		else {
			map.createNode(startX, startY);
			map.getNode(startX, startY).makeFloor();
			connection.add(map.getNode(startX, startY));
		}
//		System.out.println("Connection list size = " + connection.size());
//			System.out.println("Connecting " + startX + "," + startY + " to " + endX + "," + endY + " via:");
				for (Node node: connection) {
					map.createNode(node);
					node.makeFloor();
				}
//			System.out.println("Connected " + this.column + "," + this.row + " to " + room.column + "," + room.row + " path size " + connection.size());
			connectedTo.add(room);
			room.connectedTo.add(this);
		}
	}	
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getCenterX() {
		return x + (width / 2);
	}
	
	public int getCenterY() {
		return y + (height / 2);
		
	}
	public int getWidth() {
		return width;
	}

	public boolean isConnected() {
		return connected;
	}
	
	public boolean connectedTo(Room room) {
		if (connectedTo != null && connectedTo.size() > 0) {
			if (connectedTo.contains(room)) {
//				System.out.println(this.toString() + " connected to " + room.toString());
				return true;
			}
		}
		return false;
	}
	
	public String getRelationship(Room room) {
		if (this.column == room.column && this.row == room.row + 1) {
			return "above";
		}
		if (this.column == room.column && this.row == room.row - 1) {
			return "below";
		}
		if (this.row == room.row && this.column == room.column + 1) {
			return "right";
		}
		if (this.row == room.row && this.column == room.column - 1) {
			return "left";
		}
		return "";
	}
	
	public Feature rollDoor() {
		Random r = new Random();
		double doorRoll = r.nextDouble();
		Feature door;
		if (doorRoll > .90d) {

			door = Node.closedDoor;
		}
		else {
			door = Node.openDoor;
		}
		return door;
	}
	
	public Room getOccupiedRoom(Node node) {
		Room[][] rooms = map.getRooms();
		Point position = new Point(node.getX(), node.getY());
		for (int x = 0; x < rooms.length; x++) {
			for (int y = 0; y < rooms[0].length; y++) {
				if (rooms[x][y].boundary.contains(position)) {
					return rooms[x][y];
				}
			}
		}
		return null;
	}
	
	public Room getRandomConnectedRoom() {
		Random random = new Random();
		int randomRoomIndex = (int)(connectedTo.size() * random.nextDouble());
//		System.out.println("connected rooms size = " + connectedTo.size());
//		System.out.println("connected rooms index = " + randomRoomIndex);
		Room connectedRoom = connectedTo.get(randomRoomIndex);
//		System.out.println("getRandomConnectedRoom: current room = " + this.toString() + ", next = " + connectedRoom.toString());
		return connectedTo.get(randomRoomIndex);
	}
	
	public Rectangle getBoundary() {
		return boundary;
	}
	
	public Node getRandomNodeInRoom() {
		Random random = new Random();
//		int x = (int)(random.nextDouble() * ((boundary.getMinX() + 1) + boundary.getMaxX()));
		int minX = 0, maxX = 0, minY = 0, maxY = 0, x = 0, y = 0;
		boolean nodePicked = false;
		while (!nodePicked) {
			minX = (int)boundary.getMinX() + 1;
			maxX = (int)boundary.getMaxX();
			minY = (int)boundary.getMinY() + 1;
			maxY = (int)boundary.getMaxY();
			x = minX + (int)(random.nextDouble() * (maxX - minX));
			y = minY + (int)(random.nextDouble() * (maxY - minY));
			if (map.getNode(x, y).isFloor()) {
				nodePicked = true;
				break;
			}
		}
		return map.getNode(x, y);
	}
	
	public List<Room> getConnectedTo() {
		return connectedTo;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
}
