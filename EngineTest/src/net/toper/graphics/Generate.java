package net.toper.graphics;

import java.util.Random;

import net.toper.Frame;
import net.toper.Input;

public class Generate {

	int size;
	public static int TILE_SIZE = 10;
	Tile[] tiles;
	Random r = new Random();

	public Generate(int size) {
		RenderEngine.grass.setScale((float) Generate.TILE_SIZE
				/ RenderEngine.grass.getWidth());
		RenderEngine.water.setScale((float) Generate.TILE_SIZE
				/ RenderEngine.water.getWidth());
		RenderEngine.nullTex.setScale((float) Generate.TILE_SIZE
				/ RenderEngine.nullTex.getWidth());
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
					tiles[x + y * size] = new WaterTile(x, y);
				} else {
					tiles[x + y * size] = new GrassTile(x, y);
				}
			}
		}
	}

	public void render(Frame f) {
		int xOffset = Input.getScaledMouseX() - (TILE_SIZE * size) / 2;
		int yOffset = Input.getScaledMouseY() - (TILE_SIZE * size) / 2;
		int xFarBound = (f.getScaledWidth() / TILE_SIZE)
				- ((xOffset) / TILE_SIZE);
		int yFarBound = (f.getScaledHeight() / TILE_SIZE)
				- ((yOffset) / TILE_SIZE);
		int yCloseBound = yFarBound - (f.getScaledHeight() / TILE_SIZE);
		int xCloseBound = xFarBound - (f.getScaledWidth() / TILE_SIZE);
		for (int y = yCloseBound; y < yFarBound; y++) {
			for (int x = xCloseBound; x < xFarBound; x++) {
				if (x >= 0 && y >= 0 && (x * TILE_SIZE + xOffset) > -TILE_SIZE
						&& x * TILE_SIZE + xOffset < f.getScaledWidth()
						&& y * TILE_SIZE + yOffset > -TILE_SIZE
						&& y * TILE_SIZE + yOffset < f.getScaledHeight()) {
					int index = x + y * size;
					if (index < tiles.length)
						tiles[index].render(f, -xOffset, -yOffset);
				}
			}
		}
	}

}
