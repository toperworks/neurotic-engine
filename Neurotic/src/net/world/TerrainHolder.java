package net.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.render.Loader;
import net.render.water.WaterTile;

public class TerrainHolder {

	private GenerateTerrain[] terrains;

	private static int SIZE;
	List<WaterTile> waters = new ArrayList<WaterTile>();

	public TerrainHolder(int size) {
		SIZE = size;
		terrains = new GenerateTerrain[size * size];
	}

	private Random r = new Random();

	public void generate(Loader l) {
		OpenSimplexNoise n1 = new OpenSimplexNoise(r.nextInt());
		OpenSimplexNoise n2 = new OpenSimplexNoise(r.nextInt());
		OpenSimplexNoise n3 = new OpenSimplexNoise(r.nextInt());
		int size = (int) (SIZE * GenerateTerrain.SIZE);
		float[][] heights = new float[size][size];
		for (int x = 0; x < size; x++) {
			for (int z = 0; z < size; z++) {
				float value = (float) ((n1.eval(x / 1300.0, z / 1045.0, 0.95) + n2.eval(x / 1163.0, z / 1461.0, 0.5) * .5
						+ n3.eval(x / 200.0, z / 200.0, 0.85) * .25) / (1 + .5 + .25)) * 2;
				heights[(int) ((x))][(int) (z)] = value;
			}
		}

		for (int x = 0; x < SIZE; x++) {
			for (int z = 0; z < SIZE; z++) {
				terrains[x + z * SIZE] = (new GenerateTerrain(x, z, l, heights));
				waters.add(terrains[x + z * SIZE].getWater());
			}
		}
	}

	public GenerateTerrain getChunk(float x, float z) {
		int xa = (int) (x / GenerateTerrain.SIZE);
		int za = (int) (z / GenerateTerrain.SIZE);
		int index = (int) (xa + za * SIZE);
		if (index < terrains.length)
			if (xa >= 0 && za >= 0)
				return terrains[index];
		return null;
	}

	public static int getSize() {
		return SIZE;
	}

	public GenerateTerrain[] getTerrains() {
		return terrains;
	}

	public List<WaterTile> getWater() {
		return waters;
	}

}
