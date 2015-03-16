package trl.gamestate;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import trl.main.Game;

public class MenuState extends GameState{
	private String[] menuOptions;
	public static boolean up = false, down = false, enter = false;
	private int classChoice;
	private static final int FONT_SIZE = 24;
	public MenuState() {
		init();
	}
	
	public void init() {
		menuOptions = new String[]{"Barbarian", "Thief", "Wizard", "Ranger"};			
	}
	
	public void tick() {
		if (down) {
			if (classChoice < menuOptions.length - 1) {
				classChoice += 1;
			}
			else {
				classChoice = 0;
			}
			down = false;
		}
		if (up) {
			if (classChoice > 0) {
				classChoice -= 1;
			}
			else {
				classChoice = menuOptions.length - 1;
			}
			up = false;
		}
		if (enter) {
			Game.getGameStateManager().addGameState(1, new GameplayState(classChoice));
			Game.getGameStateManager().setGameState(1);
			enter = false;
		}
//		Game.classChoice = classChoice;
	}
	
	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Game.W_WIDTH, Game.W_HEIGHT);
		g.fillRect(0, Game.W_HEIGHT, Game.W_WIDTH, Game.MSG_HEIGHT);
		g.setFont(new Font("Fixed", Font.PLAIN, FONT_SIZE));
		FontMetrics fm = g.getFontMetrics();
		g.setColor(Color.WHITE);
		g.drawString("Choose Your Class", (Game.W_WIDTH - fm.stringWidth("Choose Your Class")) / 2, FONT_SIZE);
		for (int i = 0; i < menuOptions.length; i++) {
			if (i == classChoice) {
				g.setColor(Color.YELLOW);
			}
			else {
				g.setColor(Color.WHITE);
			}
			g.drawString(menuOptions[i], (Game.W_WIDTH - fm.stringWidth(menuOptions[i])) / 2, i * FONT_SIZE + 2 * FONT_SIZE);
		}
		BufferedImage classImage = null;
		g.setColor(Color.WHITE);
		int linePosition = Game.W_HEIGHT - Game.W_HEIGHT / 4;
		switch(classChoice) {
			case 0: {
				classImage = Game.getImageManager().barbarian;
				
				g.drawString("Highest max HP and attack. Regenerates health", 0, linePosition);
				linePosition += g.getFont().getSize();
				g.drawString("when not in combat. Can shout to lure enemies", 0, linePosition);
				linePosition += g.getFont().getSize();
				g.drawString("to his current position.", 0, linePosition);
				break;
			}
			case 1: {
				classImage = Game.getImageManager().thief;
				g.drawString("Medium max HP and attack. Has full awareness", 0, linePosition);
				linePosition += g.getFont().getSize();
				g.drawString("of all map features and entities. (Pretty awesome.)", 0, linePosition);
				break;
			}
			case 2: {
				classImage = Game.getImageManager().wizard;;
				g.drawString("Physically weak, but has a powerful magic attack ", 0, linePosition);
				linePosition += g.getFont().getSize();
				g.drawString("that deals damage to all enemies within a radius of", 0, linePosition);
				linePosition += g.getFont().getSize();
				g.drawString("two tiles. Can also blink (random teleport) and", 0, linePosition);
				linePosition += g.getFont().getSize();
				g.drawString("quicken to ready AoE spell by sacrificing 25% of", 0, linePosition);
				linePosition += g.getFont().getSize();
				g.drawString("current health.", 0, linePosition);
				break;
			}
			case 3: {
				classImage = Game.getImageManager().ranger;
				g.drawString("Medium max HP and attack, but makes it up with", 0, linePosition);
				linePosition += g.getFont().getSize();
				g.drawString("[experimental] ranged attack. Press (f) to enter ", 0, linePosition);
				linePosition += g.getFont().getSize();
				g.drawString("target mode. If more than one enemy is visible,", 0, linePosition);
				linePosition += g.getFont().getSize();
				g.drawString("cycle through enemies with L/R arrows, then (f)", 0, linePosition);
				linePosition += g.getFont().getSize();
				g.drawString("again to fire. Not well tested, so expect weirdness.", 0, linePosition);
			}
		}
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect((Game.W_WIDTH / 2) - ((Game.TILE_SIZE * 4 + Game.TILE_SIZE) / 2), 200, Game.TILE_SIZE * 4 + Game.TILE_SIZE, Game.TILE_SIZE * 4 + Game.TILE_SIZE);
		g.drawImage(classImage, (Game.W_WIDTH / 2) - ((Game.TILE_SIZE * 4 + Game.TILE_SIZE) / 2) + (Game.TILE_SIZE / 2), 200 + (Game.TILE_SIZE / 2), Game.TILE_SIZE * 4, Game.TILE_SIZE * 4, null);
		
	
	}
	
	
}
