package net.toper.graphics;

import java.util.Random;

import net.toper.Frame;
import net.toper.Input;

public class Generate {

	int size;
	public static int TILE_SIZE = 1024;
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
					tiles[x + y * size] = new GrassTile(x, y);
				} else if (rand == 3) {
					tiles[x + y * size] = new GrassTile(x, y);
				} else {
					tiles[x + y * size] = new GrassTile(x, y);
				}
			}
		}
	}

	float add = 0;
	boolean subtract;

	public void render(Frame f) {
		add -= 0.1f * f.getDelta();
		int xOffset = Input.getScaledMouseX() - (TILE_SIZE * size) / 2;
		int yOffset = Input.getScaledMouseY() - (TILE_SIZE * size) / 2;
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				if ((x * TILE_SIZE + xOffset) > -TILE_SIZE && x * TILE_SIZE + xOffset < f.getScaledWidth()
						&& y * TILE_SIZE + yOffset > -TILE_SIZE && y * TILE_SIZE + yOffset < f.getScaledHeight())
					tiles[x + y * size].render(f, -xOffset, -yOffset);
			}
		}
		TILE_SIZE = (int) (TILE_SIZE + add);
	}

}
