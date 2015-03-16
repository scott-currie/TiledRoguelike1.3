package trl.gamestate;

import java.awt.Color;
import java.awt.Graphics;

import trl.main.Game;

public class LoseGameState extends GameState{
	private int enemiesDefeated;
	private String[] choices;
	public static boolean up = false, down = false, enter = false;
	private int choice;
	
	public LoseGameState(int enemiesDefeated) {
		this.enemiesDefeated = enemiesDefeated;
		init();
	}
	
	public void init() {
		choices = new String[]{"Yes", "No"};
		choice = 0;
	}
	
	public void tick() {
		if (up) {
			if (choice == 0) {
				choice = 1;
			}
			else {
				choice = 0;
			}
			up = false;
		}
		if (down) {
			if (choice == 1) {
				choice = 0;
			}
			else {
				choice = 1;
			}
			down = false;
		}
		if (enter) {
			if (choice == 0) {
				Game.getGameStateManager().removeGameState(0);
				Game.getGameStateManager().addGameState(0, new MenuState());
				Game.getGameStateManager().setGameState(0);
			}
			if (choice == 1) {
				Game.running = false;
			}
			enter = false;
		}
		
	}
	
	public void render(Graphics g) {
		int originX = Game.W_WIDTH / 4;
		int originY = Game.W_HEIGHT / 4;
		int width = Game.W_WIDTH / 2;
		int height = Game.W_HEIGHT / 2;
		g.setColor(Color.BLACK);
		g.fillRect(originX, originY, width, height);
		int linePosition = originY;
		linePosition += 16;
		String exitMessage = "You were defeated after killing " + enemiesDefeated + " enemies";
		g.setColor(Color.WHITE);
//		g.drawString(exitMessage, (Game.W_WIDTH - g.getFontMetrics().stringWidth(exitMessage)) / 2, 0);
		g.drawString(exitMessage, (Game.W_WIDTH - g.getFontMetrics().stringWidth(exitMessage)) / 2, linePosition);
		linePosition += 16;
		g.drawString("Play again?", (Game.W_WIDTH - g.getFontMetrics().stringWidth("Play again?")) / 2, linePosition);
		linePosition += 16;
		for (int i = 0; i < choices.length; i++) {
			if (i == choice) {
				g.setColor(Color.YELLOW);
			}
			else {
				g.setColor(Color.WHITE);
			}
			g.drawString(choices[i], (Game.W_WIDTH - g.getFontMetrics().stringWidth(choices[i])) / 2, linePosition);
			linePosition += 16;
		}
	}
}
