package net.toper.graphics;

import java.util.Random;

public class Generate {

	int size;
	Tile[] tiles;
	Random r = new Random();

	public Generate(int size) {
		tiles = new Tile[size * size];
		for (int i = 0; i < tiles.length; i++) {
			int rand = r.nextInt() % 3;
		}
	}

}
