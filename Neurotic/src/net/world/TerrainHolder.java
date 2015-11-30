package net.world;

import java.util.ArrayList;
import java.util.List;

import net.render.Loader;

public class TerrainHolder {

	private List<GenerateTerrain> terrains;

	private int size;

	public TerrainHolder(int size) {
		this.size = size;
		terrains = new ArrayList<GenerateTerrain>();
	}

	public void generate(Loader l) {
		for (int z = 0; z < size; z++) {
			for (int x = 0; x < size; x++) {
				terrains.add(new GenerateTerrain(x, z, l));
			}
		}
	}

	public GenerateTerrain getChunk(float x, float z) {
		x = x / GenerateTerrain.SIZE;
		z = z / GenerateTerrain.SIZE;
		int index = (int) (x + z * size);
		if (index < terrains.size())
			if (x >= 0 && z >= 0) {
				return terrains.get(index);
			}
		return null;
	}

	public int getSize() {
		return size;
	}

	public List<GenerateTerrain> getTerrains() {
		return terrains;
	}

}
