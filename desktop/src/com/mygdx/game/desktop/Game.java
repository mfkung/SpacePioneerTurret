package com.mygdx.game.desktop;

import static helpers.Art.QuickLoad;
import static helpers.Art.DrawQuadTex;
import static helpers.Clock.*;

import org.lwjgl.input.Mouse;

import UserInterface.Button;
import UserInterface.UI;
import helpers.StateManager;

public class Game {
	
	private TileGrid grid;
	private Player player;
	private float time;
	private WaveManager waveManager;

	private UI PickerUI;
	private Enemy[] enemyTypes;
	public static final int TILE_SIZE = 64;

	public Game(int[][] map) {
		grid = new TileGrid(map);
		Enemy[] enemyTypes = new Enemy[3];
		enemyTypes[0] = new EnemySpaceShip(19, 4, grid);
		enemyTypes[1] = new Asteroids(0, 6, grid);
		enemyTypes[2] = new EnemySpaceShip2(19, 8, grid);
		waveManager = new WaveManager(enemyTypes, 1, 10);

		player = new Player(grid, waveManager);
		player.setup();
		setupUI();
	}
	
	private void setupUI() {
		PickerUI = new UI();
		//PickerUI.addButton("TurrentBlack", "rocket3", 0, 0);
		//PickerUI.addButton("TurrentSlow", "rocket4", 64, 0);
		PickerUI.createMenu("TurrentPicker", 24, 792, 10, 2);
		PickerUI.getMenu("TurrentPicker").addButton(new Button("TurrentBlack", QuickLoad("rocket3"), 0, 0));
		PickerUI.getMenu("TurrentPicker").addButton(new Button("TurrentSlow", QuickLoad("rocket4"), 0, 0));

		
	}
	
	private void updateUI() {
		PickerUI.draw();
		PickerUI.drawString(940, 740, "Lives: " + Player.Lives);
		PickerUI.drawString(1100, 740, "Metal Scrap: " + Player.Scrap);
		PickerUI.drawString(0, 0, StateManager.framesInLastSecond + " fps");
		PickerUI.drawString(750, 740, "Wave:" + waveManager.getWaveNumber());
		
		if (Mouse.next()) {
			boolean mouseClicked = Mouse.isButtonDown(0);
			if (mouseClicked) {
				if(PickerUI.getMenu("TurrentPicker").isButtonClicked("TurrentBlack"))
					player.pickTurrent(new TurretBlack(TowerType.TurrentBlack, grid.GetTile(0, 0), waveManager.getCurrentWave().getEnemyList()));
				else if(PickerUI.getMenu("TurrentPicker").isButtonClicked("TurrentSlow"))
					player.pickTurrent(new TurretSlow(TowerType.TurrentSlow, grid.GetTile(0, 0), waveManager.getCurrentWave().getEnemyList()));
			}
		}
	}
	
	public void update() {
		time += Delta();
		grid.Draw();
		waveManager.update();
		player.update();
		PickerUI.draw();
		updateUI();
	}
}
