package net.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.render.Loader;
import net.render.water.WaterTile;

public class TerrainHolder implements Runnable {

	private static GenerateTerrain[] terrains;
	private static boolean[] generated;
	private Loader l;

	private static int SIZE;
	private static List<WaterTile> waters = new ArrayList<WaterTile>();

	public TerrainHolder(int size, Loader l) {
		this.l = l;
		SIZE = size;
		terrains = new GenerateTerrain[size * size];
		generated = new boolean[size * size];
		for (int i = 0; i < generated.length; i++) {
			generated[i] = false;
		}
	}

	private Random r = new Random();

	public void run() {
		OpenSimplexNoise n1 = new OpenSimplexNoise(r.nextInt());
		OpenSimplexNoise n2 = new OpenSimplexNoise(r.nextInt());
		OpenSimplexNoise n3 = new OpenSimplexNoise(r.nextInt());
		int size = (int) (SIZE * GenerateTerrain.SIZE);
		float[][] heights = new float[size][size];
		for (int xa = 0; xa < SIZE; xa++) {
			for (int za = 0; za < SIZE; za++) {
				for (int xb = 0; xb < GenerateTerrain.SIZE; xb++) {
					for (int zb = 0; zb < GenerateTerrain.SIZE; zb++) {
						int x = (int) (xb + (xa * GenerateTerrain.SIZE));
						int z = (int) (zb + (za * GenerateTerrain.SIZE));
						float value = (float) ((n1.eval(x / 1300.0, z / 1045.0, 0.95)
								+ n2.eval(x / 1163.0, z / 1461.0, 0.5) * .5 + n3.eval(x / 200.0, z / 200.0, 0.85) * .25)
								/ (1 + .5 + .25)) * 2;
						heights[(int) ((xb))][(int) (zb)] = value;
					}
				}
				terrains[xa + za * SIZE] = (new GenerateTerrain(xa, za, l, heights));
				if (terrains[xa + za * SIZE].hasWater())
					waters.add(terrains[xa + za * SIZE].getWater());
				generated[xa + za * SIZE] = true;
			}
		}
	}

	public static GenerateTerrain getChunk(float x, float z) {
		int xa = (int) (x / GenerateTerrain.SIZE);
		int za = (int) (z / GenerateTerrain.SIZE);
		int index = (int) (xa + za * SIZE);
		if (index < terrains.length)
			if (xa >= 0 && za >= 0)
				if (generated[index])
					return terrains[index];
		return null;
	}

	public static int getSize() {
		return SIZE;
	}

	public static GenerateTerrain[] getTerrains() {
		return terrains;
	}

	public static boolean[] getGeneratedTerrain() {
		return generated;
	}

	public static List<WaterTile> getWater() {
		return waters;
	}

}
