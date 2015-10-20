package net.toper.graphics;

import java.util.Random;

import net.toper.Frame;
import net.toper.Input;

public class Generate {

	int size;
	public static final int TILE_SIZE = 32;
	Tile[] tiles;
	Random r = new Random();

	public Generate(int size) {
		this.size = size;
		tiles = new Tile[size * size];
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				int rand = r.nextInt() % 3;
				if (rand == 0) {
					tiles[x + y * size] = new GrassTile(x, y);
				} else if (rand == 2) {
					tiles[x + y * size] = new WaterTile(x, y);
				} else if (rand == 3) {
					tiles[x + y * size] = new GrassTile(x, y);
				} else {
					tiles[x + y * size] = new WaterTile(x, y);
				}
			}
		}
	}

	public void render(Frame f) {
		int xOffset = Input.getScaledMouseX() - 350;
		int yOffset = Input.getScaledMouseY() - 350;
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				if ((x * TILE_SIZE + xOffset) > -TILE_SIZE && x * TILE_SIZE + xOffset < f.getScaledWidth()
						&& y * TILE_SIZE + yOffset > -TILE_SIZE && y * TILE_SIZE + yOffset < f.getScaledHeight())
					tiles[x + y * size].render(f, -xOffset, -yOffset);
			}
		}
	}

}
